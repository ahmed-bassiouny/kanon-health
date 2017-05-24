package com.germanitlab.kanonhealth.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eslam on 1/14/17.
 */

public class ActivateAccountRequest implements Serializable {

    //  user_id,password,code
    @SerializedName("user_id")
    private int userID;
    @SerializedName("password")
    private String password;
    @SerializedName("code")
    private String code;

    public ActivateAccountRequest(int userID, String password, String code) {
        this.userID = userID;
        this.password = password;
        this.code = code;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
