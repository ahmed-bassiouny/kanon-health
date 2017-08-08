package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 08/08/17.
 */

public class Comment extends ParentModel {
    /*
    * */
    public static final String KEY_COMMENT="comment";
    public static final String KEY_RATE="rate";
    public static final String KEY_USER_TYPE="user_type";
    public static final String KEY_USER_TITLE="title";
    public static final String KEY_USER_FIRST_NAME="first_name";
    public static final String KEY_USER_LAST_NAME="last_name";
    public static final String KEY_USER_AVATAR="avatar";

    @SerializedName(KEY_COMMENT)
    private String comment;
    @SerializedName(KEY_RATE)
    private Float rate;
    @SerializedName(KEY_USER_TYPE)
    private Integer userType;
    @SerializedName(KEY_USER_TITLE)
    private String title;
    @SerializedName(KEY_USER_FIRST_NAME)
    private String firstName;
    @SerializedName(KEY_USER_LAST_NAME)
    private String lastName;
    @SerializedName(KEY_USER_AVATAR)
    private String avatar;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getRate() {
        if(rate==null)
            rate=0.0f;
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
