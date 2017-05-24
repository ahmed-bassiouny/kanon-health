package com.germanitlab.kanonhealth.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eslam on 1/14/17.
 */

public class UserRegisterResponse implements Serializable {

    //-- Response : {"password":"831558a6e65a225c710b084742f407b6","user_id":97,"sucess":true}


    @SerializedName("password")
    private String password;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("sucess")
    private boolean sucess;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isSucess() {
        return sucess;
    }

    public void setSucess(boolean sucess) {
        this.sucess = sucess;
    }

    @Override
    public String toString() {
        return "UserRegisterResponse{" +
                "password='" + password + '\'' +
                ", user_id='" + user_id + '\'' +
                ", sucess=" + sucess +
                '}';
    }
}
