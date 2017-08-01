package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by milad on 7/31/17.
 */

public class Register extends ParentModel {

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EXISTS = "exists";

    @SerializedName(KEY_USER_ID)
    private Integer id;

    @SerializedName(KEY_PASSWORD)
    private String password;

    @SerializedName(KEY_EXISTS)
    private Boolean exists;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }
}
