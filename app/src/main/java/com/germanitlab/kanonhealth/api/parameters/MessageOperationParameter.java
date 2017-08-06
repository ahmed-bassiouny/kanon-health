package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 01/08/17.
 */

public class MessageOperationParameter extends ParentParameters {

    public final static String PARAMATER_USER_ID = "user_id";
    public final static String PARAMATER_MSG_ID = "msg_id";

    public final static int DELETE = 0;
    public final static int DELIVER = 1;
    public final static int SEEN = 2;

    // id user send message
    @SerializedName(PARAMATER_USER_ID)
    private Integer userID;
    // id message send from me to another user (multi messgaes)
    @SerializedName(PARAMATER_MSG_ID)
    private String messagesID;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getMessagesID() {
        return messagesID;
    }

    public void setMessagesID(String messagesID) {
        this.messagesID = messagesID;
    }

}
