package com.germanitlab.kanonhealth.models;

import java.io.Serializable;

/**
 * Created by sreejeshpillai on 10/05/15.
 */
public class WebLogin implements Serializable {

    private String user_id;
    private String password;
    private String code;

    @Override
    public String toString() {
        return "WebLogin{" +
                "user_id='" + getUser_id() + '\'' +
                ", password=" + getPassword() +
                ", code='" + getCode() + '\'' +
                '}';
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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