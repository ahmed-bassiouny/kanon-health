package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class LastMessageInfo extends ParentModel {

    public static final String KEY_MESSAGE="massage";
    public static final String KEY_MESSAGE_TYPE="type";
    public static final String KEY_IS_FORWARD="is_forward";
    public static final String KEY_TIME="time";

    @SerializedName(KEY_MESSAGE)
    private String message;
    @SerializedName(KEY_MESSAGE_TYPE)
    private String type;
    @SerializedName(KEY_IS_FORWARD)
    private String isForward;
    @SerializedName(KEY_TIME)
    private String time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsForward() {
        return isForward;
    }

    public void setIsForward(String isForward) {
        this.isForward = isForward;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
