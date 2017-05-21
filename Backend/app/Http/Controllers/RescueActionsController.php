<?php

namespace App\Http\Controllers;

use App\RescueActions;
use App\RescueActionUsers;
use App\User;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\DB;
use sngrl\PhpFirebaseCloudMessaging\Client;
use sngrl\PhpFirebaseCloudMessaging\Message;
use sngrl\PhpFirebaseCloudMessaging\Notification;
use sngrl\PhpFirebaseCloudMessaging\Recipient\Topic;
use Twilio;

class RescueActionsController extends Controller
{
    private $client;

    public function __construct()
    {
        $this->client = new Client();
        $this->client->setApiKey(env('FIREBASE_API_KEY'));
        $this->client->injectGuzzleHttpClient(new \GuzzleHttp\Client());
    }

    /**
     * @Get("/api/rescue_actions")
     *
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $user = \JWTAuth::parseToken()->authenticate();

        return User::find($user->id)->with('rescue_actions')->get();
    }

    /**
     * @Get("/api/rescue_actions/active")
     *
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function getActive()
    {
        $user = \JWTAuth::parseToken()->authenticate();

        return User::find($user->id)->with(['rescue_actions' => function ($query) {
            $query->whereNull('rescue_actions.end_time');
        }, 'rescue_actions.users'])->first();
    }

    /**
     * @Get("/api/rescue_actions/{id}")
     *
     * Display the specified resource.
     *
     * @param $id
     * @return \Illuminate\Http\Response
     * @internal param RescueActions $rescueActions
     */
    public function show($id)
    {
        $obj = RescueActions::find($id);
        $invited = $obj->users->pluck('id')->toArray();

        $others = User::all()->filter(function ($user) use ($invited) { return !in_array($user->id, $invited); })->values();

        return [
            'rescue_action' => $obj,
            'all' => $others,
        ];
    }

    /**
     * @Post("/api/rescue_actions/invite")
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function storeInvite(Request $request)
    {
        $user_ids = $request->get('user_ids');
        $rescue_action_id = $request->get('rescue_action_id');

        try {

            DB::beginTransaction();

            foreach ($user_ids as $user_id) {
                $data['user_id'] = $user_id;
                $data['rescue_action_id'] = $rescue_action_id;

                RescueActionUsers::create($data);

                $user = User::find($user_id);
                $rescueAction = RescueActions::find($rescue_action_id);
                foreach($user->phones as $phone) {
                    Twilio::message($phone->number, 'PAŽNJA! Poziv za pristup akciji ' . $rescueAction->name);
                }
            }

        } catch (Exception $e) {
            DB::rollBack();

            return response()->json($e->getMessage(), Response::HTTP_BAD_REQUEST);
        }

        DB::commit();

        return response()->json('', Response::HTTP_CREATED);
    }

    /**
     * @Put("/api/rescue_action/{rescueActionId}/invite/accept")
     *
     * @param $rescueActionId
     * @return mixed
     */
    public function respondToInvite($rescueActionId)
    {
        $user = \JWTAuth::parseToken()->authenticate();

        foreach(RescueActions::find($rescueActionId)->leader->phones as $phone) {
            Twilio::message($phone->number, 'Član ' . $user->name . ' potvrđuje dolazak u akciju');
        }

        return RescueActionUsers::where(['user_id' => $user->id, 'rescue_action_id' => $rescueActionId])->update(['accepted' => true]);
    }

    /**
     * @Post("/api/rescue_action/{rescueActionId}/send_message")
     *
     * @param Request $request
     * @param $rescueActionId
     * @return \Psr\Http\Message\ResponseInterface
     */
    public function sendPush(Request $request, $rescueActionId)
    {
        $message = new Message();
        $message->setPriority('high');
        $message->addRecipient(new Topic($rescueActionId));
        $message->setNotification(new Notification(RescueActions::find($rescueActionId)->first()->name, $request->message));

        return response()->json($this->client->send($message), 200);
    }

    /**
     * @Put("/api/rescue_action/{rescueActionId}/close")
     *
     * @param $rescueActionId
     * @return mixed
     */
    public function closeRescueAction($rescueActionId)
    {
        return response()->json([], RescueActions::find($rescueActionId)->update(['end_time' => Carbon::now('Europe/Zagreb')]) ? 200 : 400);
    }

    public function getAllPersonal()
    {

    }
}
