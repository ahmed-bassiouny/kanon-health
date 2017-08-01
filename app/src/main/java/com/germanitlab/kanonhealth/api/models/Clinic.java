package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by norhan on 8/1/17.
 */

public class Clinic {
    public static final String KEY_ID = "ID";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_SPECIALITY = "speciality";
    public static final String KEY_RATE_NUM = "rate_num";
    public static final String KEY_RATE_PERCENTAGE = "rate_percentage";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_STREET_NAME = "streetname";
    public static final String KEY_HOUSE_NUMBER = "house_number";
    public static final String KEY_ZIP_CODE = "zip_code";
    public static final String KEY_CITY = "city";
    public static final String KEY_PROVINCE = "province";
    public static final String KEY_COUNTRY = "county";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_FAX = "fax";
    public static final String KEY_SUPPORTED_LANG = "supported_lang";


    @SerializedName(KEY_ID)
    private Integer id;

    @SerializedName(KEY_USER_ID)
    private Integer userId;

    @SerializedName(KEY_NAME)
    private String name;

    @SerializedName(KEY_EMAIL)
    private String email;

    @SerializedName(KEY_AVATAR)
    private String avatar;

    @SerializedName(KEY_SPECIALITY)
    private String speciality;

    @SerializedName(KEY_RATE_NUM)
    private Float rateNum;

    @SerializedName(KEY_RATE_PERCENTAGE)
    private HashMap<String, String> ratePercentage;

    @SerializedName(KEY_ADDRESS)
    private String address;

    @SerializedName(KEY_STREET_NAME)
    private String streetName;

    @SerializedName(KEY_HOUSE_NUMBER )
    private String houseNumber;

    @SerializedName(KEY_ZIP_CODE)
    private String zipCode;

    @SerializedName(KEY_CITY )
    private String city;

    @SerializedName(KEY_PROVINCE)
    private String province;

    @SerializedName(KEY_COUNTRY)
    private String country;

    @SerializedName(KEY_PHONE)
    private String phone;

    @SerializedName(KEY_FAX)
    private String fax;

    @SerializedName(KEY_SUPPORTED_LANG)
    private ArrayList<SupportedLang> supportedLangs;



}

