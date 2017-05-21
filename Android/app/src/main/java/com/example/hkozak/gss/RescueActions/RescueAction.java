package com.example.hkozak.gss.RescueActions;

/**
 * Created by HKozak on 5/20/2017.
 */

import android.content.Context;
import android.preference.PreferenceManager;

import com.example.hkozak.gss.Constants;
import com.example.hkozak.gss.User.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RescueAction {

    public static final int TYPE_ACTION_FOR_LEADER = 1;
    public static final int TYPE_ACTION_FOR_USER = 2;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("leader_id")
    @Expose
    private Integer leaderId;
    @SerializedName("action_type_id")
    @Expose
    private Integer actionTypeId;
    @SerializedName("station_id")
    @Expose
    private Integer stationId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("end_time")
    @Expose
    private Object endTime;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("users")
    @Expose
    private List<User> users = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public Integer getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(Integer actionTypeId) {
        this.actionTypeId = actionTypeId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getActionType(int userID) {
        if (leaderId == null) return -1;
        return userID == leaderId ? TYPE_ACTION_FOR_LEADER : TYPE_ACTION_FOR_USER;
    }
}

