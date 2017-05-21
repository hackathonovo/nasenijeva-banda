<?php

namespace App\Admin\Controllers;

use App\Http\Controllers\Controller;
use App\RescueActions;
use App\RescueActionUsers;
use App\Stations;
use App\User;
use Carbon\Carbon;
use Encore\Admin\Facades\Admin;
use Encore\Admin\Layout\Column;
use Encore\Admin\Layout\Content;
use Encore\Admin\Layout\Row;
use Encore\Admin\Widgets\Box;
use Encore\Admin\Widgets\Chart\Bar;
use Encore\Admin\Widgets\Chart\Doughnut;
use Encore\Admin\Widgets\Chart\Line;
use Encore\Admin\Widgets\Chart\Pie;
use Encore\Admin\Widgets\Chart\PolarArea;
use Encore\Admin\Widgets\Chart\Radar;
use Encore\Admin\Widgets\Collapse;
use Encore\Admin\Widgets\InfoBox;
use Encore\Admin\Widgets\Tab;
use Encore\Admin\Widgets\Table;
use Illuminate\Support\Facades\DB;

class ReportsController extends Controller
{
    public function index()
    {
        return Admin::content(function (Content $content) {
            $content->header('Izvještaj');

            $data = User::all();
            $numActions = [];
            $totalHours = [];

            foreach($data as $row) {
                $numActions[$row->id] = DB::table('rescue_action_users')->where(['user_id' => $row->id, 'accepted' => 1])->count();

                $all_rescue_actions_participants = DB::table('rescue_action_users')->where(['user_id' => $row->id, 'accepted' => 1])->get();

                $totalHours[$row->id] = 0;
                foreach($all_rescue_actions_participants as $rescue_actions_participant) {
                    $rescue_action = RescueActions::find($rescue_actions_participant->rescue_action_id);
                    $created_at = Carbon::createFromFormat('Y-m-d H:i:s', $rescue_action->created_at);
                    $end_time = Carbon::createFromFormat('Y-m-d H:i:s', $rescue_action->end_time);
                    $totalHours[$row->id] += $created_at->diffInHours($end_time);
                }
            }

            $content->body(view('reports', ['data' => $data, 'numActions' => $numActions, 'totalHours' => $totalHours]));
        });
    }
}
