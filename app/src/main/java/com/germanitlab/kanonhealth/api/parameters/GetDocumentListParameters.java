package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/2/17.
 */

public class GetDocumentListParameters extends ParentParameters{

    public static final String PARAMETER_USER_ID ="user_id";

    @SerializedName(PARAMETER_USER_ID )
    private Integer userId;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
