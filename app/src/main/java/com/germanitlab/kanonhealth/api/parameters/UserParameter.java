package com.germanitlab.kanonhealth.api.parameters;

import android.text.TextUtils;

import com.germanitlab.kanonhealth.helpers.DateHelper;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by bassiouny on 02/08/17.
 */

public  abstract class UserParameter extends ParentParameters {

    public static final String PARAMETER_USER_ID = "user_id";
    public static final String PARAMETER_TITLE = "title";
    public static final String PARAMETER_FIRST_NAME = "first_name";
    public static final String PARAMETER_lAST_NAME = "last_name";
    public static final String PARAMETER_BIRTHDAY = "birh_day";
    public static final String PARAMETER_GENDER = "gender";
    public static final String PARAMETER_AVATAR = "avatar";

    @SerializedName(PARAMETER_USER_ID)
    private Integer userID;
    // title user like mrs , mr , doc etc
    @SerializedName(PARAMETER_TITLE)
    private String title;
    @SerializedName(PARAMETER_FIRST_NAME)
    private String firstName;
    @SerializedName(PARAMETER_lAST_NAME)
    private String lastName;
    @SerializedName(PARAMETER_BIRTHDAY)
    private String birthday;
    @SerializedName(PARAMETER_GENDER)
    private String gender;


    public Integer getUserID() {
        if(userID==null) {
            userID = 0;
        }
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
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

    public String getBirthday() {
        String displayString = "";
        Date bd = new Date();
        if (!TextUtils.isEmpty(birthday)) {
            bd = DateHelper.FromServerDateStringToServer(birthday);
        }
        displayString = DateHelper.FromDisplayDateToBirthDateString(bd);
        return displayString;
    }

    public void setBirthday(String birthday) {
        this.birthday = DateHelper.FromDisplayDateToServerString(DateHelper.FromBirthDateStringToDisplay(birthday));
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
