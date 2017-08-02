package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class ChatUserInfo extends ParentModel {

    public static final String KEY_ROOM_ID="room_id";
    public static final String KEY_FIRST_NAME="first_name";
    public static final String KEY_LAST_NAME="last_name";
    public static final String KEY_TITLE="title";
    public static final String KEY_AVATAR="avatar";

    @SerializedName(KEY_ROOM_ID)
    private Integer roomID;
    @SerializedName(KEY_FIRST_NAME)
    private String firstName;
    @SerializedName(KEY_LAST_NAME)
    private String lastName;
    @SerializedName(KEY_TITLE)
    private String title;
    @SerializedName(KEY_AVATAR)
    private String avatar;


    private Integer userID;
    private Integer userType;


    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getUserType() {
        if(userType==null)
            userType=UserInfo.PATIENT;
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getRoomID() {
        return roomID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
