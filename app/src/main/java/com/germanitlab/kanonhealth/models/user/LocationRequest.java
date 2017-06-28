package com.germanitlab.kanonhealth.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eslam on 1/19/17.
 */

public class LocationRequest implements Serializable {

    @SerializedName("user_id")
    private String userID;
    @SerializedName("password")
    private String password;
    private int specialty_id;
    private int type;
    private int is_array = 1;


    public LocationRequest(String userID, String password, int specialty_id, int type) {
        this.userID = userID;
        this.password = password;
        this.specialty_id = specialty_id;
        this.type = type;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int getId() {
        return specialty_id;
    }

    public void setId(int specialty_id) {
        this.specialty_id = specialty_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
