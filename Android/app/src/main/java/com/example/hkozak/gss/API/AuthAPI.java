package com.example.hkozak.gss.API;

import com.example.hkozak.gss.User.Credentials;
import com.example.hkozak.gss.User.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by HKozak on 5/20/2017.
 */

public interface AuthAPI {

    @POST("login/")
    Call<User> login(@Body Credentials credentials);
}
