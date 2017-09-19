package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class UserInfoParameter extends ParentParameters {
    public static final String PARAMETER_USERID = "user_id";

    public static final int CHATDOCTOR=1;
    public static final int CHATCLINIC=2;
    public static final int CHATUSER=3;
    public static final int CHATANOTHER=4;
    public static final int CHATALL=5;


    @SerializedName(PARAMETER_USERID)
    private String  userID;

    public String  getUserID() {
        return userID;
    }

    public void setUserID(String  userID) {
        this.userID = userID;
    }


}
