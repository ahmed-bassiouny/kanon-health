package com.germanitlab.kanonhealth.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eslam on 1/27/17.
 */

public class Login implements Serializable {

    @SerializedName("user_id")
    private String id;
    @SerializedName("password")
    private String password;
    @SerializedName("qr_code")
    private String code;


    public Login(String id, String password, String code) {
        this.id = id;
        this.password = password;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
