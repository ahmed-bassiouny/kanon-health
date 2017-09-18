package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by bassiouny on 02/08/17.
 */

@DatabaseTable()
public class ChatModel extends UserInfo {

    public static final String KEY_ROOM_ID="room_id";
    public static final String KEY_MESSAGE="massage";
    public static final String KEY_MESSAGE_TYPE="type";
    public static final String KEY_IS_FORWARD="is_forward";
    public static final String KEY_TIME="time";

    @SerializedName(KEY_ROOM_ID)
    @DatabaseField
    private Integer roomID;
    @SerializedName(KEY_MESSAGE)
    @DatabaseField
    private String message;
    @SerializedName(KEY_MESSAGE_TYPE)
    @DatabaseField
    private String type;
    @SerializedName(KEY_IS_FORWARD)
    @DatabaseField
    private String isForward;
    @SerializedName(KEY_TIME)
    @DatabaseField
    private String time;


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
        String formattedDate;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(time);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            formattedDate=time;
        }
        return formattedDate;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
