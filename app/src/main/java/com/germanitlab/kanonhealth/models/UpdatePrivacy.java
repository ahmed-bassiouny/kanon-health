package com.germanitlab.kanonhealth.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Geram IT Lab on 25/04/2017.
 */

public class UpdatePrivacy implements Serializable{
    @SerializedName("user_id")
    private String userID;
    @SerializedName("password")
    private String password;
    private int msg_id;
    private int privacy;


    public UpdatePrivacy(String userID, String password, int msg_id, int privacy) {
        this.userID = userID;
        this.password = password;
        this.msg_id = msg_id;
        this.privacy = privacy;
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
        return msg_id;
    }

    public void setId(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getType() {
        return privacy;
    }

    public void setType(int type) {
        this.privacy = type;
    }
}
