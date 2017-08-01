package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 01/08/17.
 */

public class MessagesParamater extends ParentParameters{

    public final static String PARAMATER_USER_ID="from_id";
    public final static String PARAMATER_TO_ID="to_id";

    @SerializedName(PARAMATER_USER_ID)
    private int userID;
    @SerializedName(PARAMATER_TO_ID)
    private int toID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getToID() {
        return toID;
    }

    public void setToID(int toID) {
        this.toID = toID;
    }
}
