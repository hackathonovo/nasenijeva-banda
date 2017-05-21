package com.example.hkozak.gss.RescueActions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HKozak on 5/20/2017.
 */

public class RescueActionsResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_level_id")
    @Expose
    private Integer userLevelId;
    @SerializedName("station_id")
    @Expose
    private Integer stationId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("rescue_actions")
    @Expose
    private List<RescueAction> rescueActions = null;

    @Override
    public String toString() {
        return "ActionsResponse -- id " + id + "; name " + name + "; stationid " + stationId;
    }

    public Integer getUserId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserLevelId() {
        return userLevelId;
    }

    public void setUserLevelId(Integer userLevelId) {
        this.userLevelId = userLevelId;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<RescueAction> getRescueActions() {
        return rescueActions;
    }

    public void setRescueActions(List<RescueAction> rescueActions) {
        this.rescueActions = rescueActions;
    }

}
