package com.germanitlab.kanonhealth.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by halima on 07/06/17.
 */

public class StatusRequestModel  {

    @SerializedName("user_id")
    private String userID;
    @SerializedName("password")
    private String password;
    @SerializedName("is_available")
    private String is_available;


    public StatusRequestModel(String id, String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public StatusRequestModel(String userID, String password, int isArray) {
        this.userID = userID;
        this.password = password;
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

    public String getIs_available() {
        return is_available;
    }

}
