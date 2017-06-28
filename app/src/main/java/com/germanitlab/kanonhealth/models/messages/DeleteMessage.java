package com.germanitlab.kanonhealth.models.messages;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Attia Gamea on 05/02/17.
 */

public class DeleteMessage implements Serializable {

    //  user_id,password,with_id
    @SerializedName("user_id")
    private int userID;
    @SerializedName("password")
    private String password;
    @SerializedName("message_id")
    private String withId;

    public DeleteMessage(int userID, String password, String withId) {
        this.userID = userID;
        this.password = password;
        this.withId = withId;
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

    public String getWithId() {
        return withId;
    }

    public void setWithId(String withId) {
        this.withId = withId;
    }
}
