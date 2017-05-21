<?php

namespace App\Http\Controllers;

use App\User;
use Illuminate\Http\Request;
use Laravel\Socialite\Facades\Socialite;

class LoginController extends Controller
{
    /**
     * @Post("/api/login", as="api.login")
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function login(Request $request)
    {
        $credentials = $request->only(['email', 'password']);

        if ($token = \JWTAuth::attempt($credentials)) {
            $user = User::where('email', $credentials['email'])->first();
            $user['token'] = $token;

            return response()->json($user, 200);
        }

        return response()->json([], 400);
    }

    /**
     * Supports Facebook and Google social login.
     *
     * @Get("/api/login/{driver}/{token}", as="api.social.login")
     *
     * @param $driver
     * @param $token
     * @return mixed
     */
    public function socialLogin($driver, $token)
    {
        $userFromToken = Socialite::driver($driver)->stateless()->userFromToken($token);

        $user = User::updateOrCreate(['email' => $userFromToken->email], (array) $userFromToken);
        $user['token'] = \JWTAuth::fromUser($user);

        return $user;
    }
}
