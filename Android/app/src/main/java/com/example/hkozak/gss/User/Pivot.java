package com.example.hkozak.gss.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HKozak on 5/21/2017.
 */

public class Pivot {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("rescue_action_id")
    @Expose
    private Integer rescueActionId;
    @SerializedName("accepted")
    @Expose
    private Integer accepted;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRescueActionId() {
        return rescueActionId;
    }

    public void setRescueActionId(Integer rescueActionId) {
        this.rescueActionId = rescueActionId;
    }

    public Integer getAccepted() {
        return accepted;
    }

    public void setAccepted(Integer accepted) {
        this.accepted = accepted;
    }

}