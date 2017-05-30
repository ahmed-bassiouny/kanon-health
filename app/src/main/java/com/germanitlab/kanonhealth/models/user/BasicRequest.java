
package com.germanitlab.kanonhealth.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eslam on 1/19/17.
 */

public class BasicRequest implements Serializable {

    @SerializedName("user_id")
    private String userID;
    @SerializedName("password")
    private String password;
    @SerializedName("is_array")
    private int isArray;

    private int is_object = 1;

    public BasicRequest(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public BasicRequest(String userID, String password, int isArray) {
        this.userID = userID;
        this.password = password;
        this.isArray = isArray;
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
}
