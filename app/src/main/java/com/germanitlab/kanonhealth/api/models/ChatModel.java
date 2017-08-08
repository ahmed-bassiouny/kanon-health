package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class ChatModel extends UserInfo {

    public static final String KEY_ROOM_ID="room_id";
    public static final String KEY_MESSAGE="massage";
    public static final String KEY_MESSAGE_TYPE="type";
    public static final String KEY_IS_FORWARD="is_forward";
    public static final String KEY_TIME="time";
    public static final String KEY_ISOPEN="is_open";

    @SerializedName(KEY_ROOM_ID)
    private Integer roomID;
    @SerializedName(KEY_MESSAGE)
    private String message;
    @SerializedName(KEY_MESSAGE_TYPE)
    private String type;
    @SerializedName(KEY_IS_FORWARD)
    private String isForward;
    @SerializedName(KEY_TIME)
    private String time;
    @SerializedName(KEY_ISOPEN)
    private int open;
    private int requestID;
    private int haveRate;

    public Integer getRoomID() {
        return roomID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }


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

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getHaveRate() {
        return haveRate;
    }

    public void setHaveRate(int haveRate) {
        this.haveRate = haveRate;
    }
}
