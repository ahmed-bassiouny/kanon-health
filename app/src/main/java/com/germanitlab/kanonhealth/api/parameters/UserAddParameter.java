package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class UserAddParameter extends UserParameter {

    public static final String PARAMETER_PASSWORD = "password";

    @SerializedName(PARAMETER_PASSWORD)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
