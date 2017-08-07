package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/7/17.
 */

public class AddClinicParameters extends AddOrEditClinicParameters{
    public static final String PARAMETER_ID ="user_id";

    @SerializedName(PARAMETER_ID)
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
