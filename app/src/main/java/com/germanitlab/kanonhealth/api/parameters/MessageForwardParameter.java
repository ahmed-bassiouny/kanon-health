package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 01/08/17.
 */

public class MessageForwardParameter extends MessageOperationParameter {

    public final static String PARAMATER_USER_TO="to";

    // id user receive message (multi users)
    @SerializedName(PARAMATER_USER_TO)
    private String toID;

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

}
