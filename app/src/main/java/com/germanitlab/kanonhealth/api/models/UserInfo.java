package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bassiouny on 02/08/17.
 */

public class UserInfo extends ParentModel{

    public static final String KEY_USERID="user_id";
    public static final String KEY_USERTYPE="user_type";
    public static final String KEY_FIRSTNAME="first_name";
    public static final String KEY_LASTNAME="last_name";
    public static final String KEY_TITLE="title";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_COUNTRY_CODE="country_code";
    public static final String KEY_PHONE="phone";
    public static final String KEY_GENDER="gender";
    public static final String KEY_BIRTHDAY="birh_day";
    public static final String KEY_AVATAR="avatar";
    public static final String KEY_DOCTOR="doctor";
    public static final String KEY_CLINICS="clinics";
    public static final String KEY_DOCUMENTS="documents";

    public static final int DOCTOR=2;
    public static final int PATIENT=1;

    @SerializedName(KEY_USERID)
    private Integer userID;
    // usertype => 1 patient , 2 doctor
    @SerializedName(KEY_USERTYPE)
    private Integer userType;
    @SerializedName(KEY_FIRSTNAME)
    private String  firstName;
    @SerializedName(KEY_LASTNAME)
    private String  lastName;
    @SerializedName(KEY_TITLE)
    private String  title;
    @SerializedName(KEY_PASSWORD)
    private String  password;
    @SerializedName(KEY_COUNTRY_CODE)
    private String  country_code;
    @SerializedName(KEY_PHONE)
    private String  phone;
    @SerializedName(KEY_GENDER)
    private String  gender;
    @SerializedName(KEY_BIRTHDAY)
    private String  birh_day;
    @SerializedName(KEY_AVATAR)
    private String  avatar;
    /*
    // i need some data to make model
    * clinic
    * document
    * doctor
    * */

    public Integer getUserID() {
        if(userID==null)
            userID=0;
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getUserType() {
        if(userType==null)
            userType=1;
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirh_day() {
        return birh_day;
    }

    public void setBirh_day(String birh_day) {
        this.birh_day = birh_day;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
