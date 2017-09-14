package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class TokenAddParameter extends ParentParameters {

    public static final String PARAMETER_USER_ID = "user_id";
    public static final String PARAMETER_TOKEN = "token";
    public static final String PARAMETER_TYPE = "type";

    @SerializedName(PARAMETER_USER_ID)
    private String userId;

    @SerializedName(PARAMETER_TOKEN)
    private String token;
    @SerializedName(PARAMETER_TYPE)
    private String type="android";


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
