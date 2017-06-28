package com.germanitlab.kanonhealth.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Geram IT Lab on 24/04/2017.
 */

public class RequsetToken implements Serializable {
    @SerializedName("user_id")
    private String userID;
    @SerializedName("password")
    private String password;
    @SerializedName("is_array")
    private int isArray;
    @SerializedName("token")
    private String token;


    public RequsetToken(String userID, String password, int isArray, String token) {
        this.userID = userID;
        this.password = password;
        this.isArray = isArray;
        this.token = token;
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

    public int getIsArray() {
        return isArray;
    }

    public void setIsArray(int isArray) {
        this.isArray = isArray;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
