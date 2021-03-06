package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/1/17.
 */

public class ChangeStatusParameters extends ParentParameters{

    public static final String PARAMETER_USER_ID ="user_id";
    public static final String PARAMETER_IS_AVAILABLE= "is_available";

    @SerializedName(PARAMETER_USER_ID)
    private Integer userId;

    @SerializedName(PARAMETER_IS_AVAILABLE)
    private Integer isAvailable;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Integer available) {
        isAvailable = available;
    }
}
