package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by ahmed on 25/09/17.
 */

public class ForwardParameter extends ParentParameters  {

    public final static String PARAMATER_USER_ID = "user_id";
    public final static String PARAMATER_MSG_ID = "msg_id";
    public final static String PARAMATER_TO_ID = "to";

    @SerializedName(PARAMATER_USER_ID)
    private Integer userID;
    @SerializedName(PARAMATER_MSG_ID)
    private String messagesID;
    @SerializedName(PARAMATER_TO_ID)
    private String toID;

    public static final int DOCUMENT = 1;
    public static final int MESSAGE_DOCTOR = 2;
    public static final int MESSAGE_CLINIC= 3;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }


    public void setMessagesID(HashMap<String, Integer> messagesID) {
        Gson g = new Gson();
        String temp = g.toJson(messagesID);
        temp=temp.replace("=",",");
        this.messagesID = temp;
    }


    public void setToID(HashMap<String, Integer> toID) {
        Gson g = new Gson();
        String temp = g.toJson(toID);
        temp=temp.replace("=",",");
        this.toID = temp;
    }
}
