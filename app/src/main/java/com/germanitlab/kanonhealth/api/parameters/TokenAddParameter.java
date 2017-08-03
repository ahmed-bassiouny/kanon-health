package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class TokenAddParameter extends UserParameter {

    public static final String PARAMETER_USER_ID = "user_id";
    public static final String PARAMETER_TOKEN = "token";

    @SerializedName(PARAMETER_USER_ID)
    private String userId;

    @SerializedName(PARAMETER_TOKEN)
    private String token;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
