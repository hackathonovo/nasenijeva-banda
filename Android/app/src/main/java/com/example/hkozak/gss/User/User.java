package com.example.hkozak.gss.User;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HKozak on 5/20/2017.
 */

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_level_id")
    @Expose
    private Integer userLevelId;
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
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("phones")
    @Expose
    public List<Phone> phones;
    @SerializedName("specialities")
    @Expose
    public List<Speciality> specialities;
    @SerializedName("station")
    @Expose
    public Station station;
    @SerializedName("user_level")
    @Expose
    public UserLevel level;
    @SerializedName("availability")
    @Expose
    public String availability;
    @SerializedName("pivot")
    @Expose
    public Pivot pivot;

    public boolean selected = false;

    @Override
    public String toString() {
        return name + "; " + email + "; " + token;
    }

    public Integer getId() {
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
