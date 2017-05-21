package com.example.hkozak.gss.API;

import com.example.hkozak.gss.RescueActions.BroadcastSMSMessage;
import com.example.hkozak.gss.RescueActions.InvitesRequest;
import com.example.hkozak.gss.RescueActions.RescueActionByIDResponse;
import com.example.hkozak.gss.RescueActions.RescueActionsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by HKozak on 5/20/2017.
 */

public interface RescueActionsAPI {

    @GET("rescue_actions/active/")
    Call<RescueActionsResponse> getActiveActions();

    @GET("rescue_actions/{id}")
    Call<RescueActionByIDResponse> getRescueActionById(@Path("id") String id);

    @POST("rescue_actions/invite")
    Call<Void> sendInvites(@Body InvitesRequest request);

    @PUT("rescue_action/{rescueActionId}/invite/accept")
    Call<Void> acceptInvite(@Path("rescueActionId") String actionId);

    @POST("rescue_action/{rescueActionId}/send_message")
    Call<Void> sendBroadcastSMS(@Path("rescueActionId") String actionId, @Body BroadcastSMSMessage message);

    @PUT("/api/rescue_action/{rescueActionId}/close")
    Call<Void> closeAction(@Path("rescueActionId") String actionId);
}
