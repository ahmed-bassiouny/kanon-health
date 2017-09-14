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
    public static final String KEY_STREET_NAME = "street_name";
    public static final String KEY_HOUSE_NUMBER = "house_number";
    public static final String KEY_PROVIDENCE = "providence";
    public static final String KEY_ZIP_CODE = "zip_code";
    public static final String KEY_WEIGHT_UNIT = "weight_unit";
    public static final String KEY_WEIGHT_VALUE = "weight";
    public static final String KEY_HEIGHT_UNIT = "height_unit";
    public static final String KEY_HEIGHT_VALUE = "height";
    public static final String KEY_BLOOD = "blood_type";


    @SerializedName(PARAMETER_USER_ID)
    private Integer userID;
    // title user like mrs , mr , doc etc
    @SerializedName(PARAMETER_TITLE)
    private String title;
    @SerializedName(KEY_WEIGHT_UNIT)
    private String weight_unit;
    @SerializedName(KEY_WEIGHT_VALUE)
    private Double weight_value;
    @SerializedName(KEY_HEIGHT_UNIT)
    private String height_unit;
    @SerializedName(KEY_HEIGHT_VALUE)
    private Double height_value;
    @SerializedName(KEY_BLOOD)
    private String blood;


    @SerializedName(PARAMETER_FIRST_NAME)
    private String firstName;
    @SerializedName(PARAMETER_lAST_NAME)
    private String lastName;
    @SerializedName(PARAMETER_BIRTHDAY)
    private String birthday;
    @SerializedName(PARAMETER_GENDER)
    private String gender;
    @SerializedName(KEY_STREET_NAME)
    private String streetName;
    @SerializedName(KEY_HOUSE_NUMBER)
    private String houseNumber;
    @SerializedName(KEY_PROVIDENCE)
    private String providence;
    @SerializedName(KEY_ZIP_CODE)
    private String zipCode;

    @SerializedName(PARAMETER_AVATAR)
    private String avatar;


    public Integer getUserID() {
        if(userID==null) {
            userID = 0;
        }
        return userID;
    }

    public String getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public Double getWeight_value() {
        return weight_value;
    }

    public void setWeight_value(Double weight_value) {
        this.weight_value = weight_value;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public Double getHeight_value() {
        return height_value;
    }

    public void setHeight_value(Double height_value) {
        this.height_value = height_value;
    }

    public String getHeight_unit() {
        return height_unit;
    }

    public void setHeight_unit(String height_unit) {
        this.height_unit = height_unit;
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

    public static String getParameterUserId() {
        return PARAMETER_USER_ID;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getProvidence() {
        return providence;
    }

    public void setProvidence(String providence) {
        this.providence = providence;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
