package com.example.hkozak.gss.RescueActions;

import com.example.hkozak.gss.User.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HKozak on 5/21/2017.
 */

public class RescueActionByIDResponse {

    @SerializedName("rescue_action")
    @Expose
    private RescueAction rescueAction;
    @SerializedName("all")
    @Expose
    private List<User> all = null;

    public RescueAction getRescueAction() {
        return rescueAction;
    }

    public List<User> getUsersNotInvited() {
        return all;
    }


}