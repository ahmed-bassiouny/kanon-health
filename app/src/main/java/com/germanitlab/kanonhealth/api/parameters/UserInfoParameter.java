package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class UserInfoParameter extends ParentParameters {
    public static final String PARAMETER_USERID = "user_id";


    @SerializedName(PARAMETER_USERID)
    private Integer userID;
    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }


}
