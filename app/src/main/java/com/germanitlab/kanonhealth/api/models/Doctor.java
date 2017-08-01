package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by norhan on 8/1/17.
 */

public class Doctor {
    public static final String KEY_ID="ID";
    public static final String KEY_RATE_NUM = "rate_num";
    public static final String KEY_RATE_PERCENTAGE = "rate_percentage";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_SUPPORTED_LANG = "supported_lang";
    public static final String KEY_IS_AVAILABLE= "is_available";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_FIRST_NAME =      "first_name";
    public static final String KEY_LAST_NAME=    "last_name";
    public static final String KEY_PHONE =   "phone";
    public static final String KEY_RATE =    "rate";
    public static final String KEY_DOCUMENTS =    "documents";



    @SerializedName(KEY_ID)
    private Integer id;

    @SerializedName(KEY_RATE_NUM)
    private Float rateNum;

    @SerializedName(KEY_RATE_PERCENTAGE)
    private HashMap<String, String> ratePercentage;

    @SerializedName(KEY_ADDRESS)
    private String address;

    @SerializedName(KEY_IS_AVAILABLE)
    private Boolean isAvailable;

    @SerializedName(KEY_SUPPORTED_LANG)
    private ArrayList<SupportedLang> supportedLangs;

    @SerializedName(KEY_AVATAR)
    private String avatar;

    @SerializedName(KEY_PHONE)
    private String phone;

    @SerializedName(KEY_FIRST_NAME )
    private String firstName;

    @SerializedName(KEY_LAST_NAME )
    private String lastName;

    @SerializedName(KEY_DOCUMENTS )
    private ArrayList<Document> documents;

    @SerializedName(KEY_RATE)
    private ArrayList<String> rate;


    public static String getKeyId() {
        return KEY_ID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getRateNum() {
        return rateNum;
    }

    public void setRateNum(Float rateNum) {
        this.rateNum = rateNum;
    }

    public HashMap<String, String> getRatePercentage() {
        return ratePercentage;
    }

    public void setRatePercentage(HashMap<String, String> ratePercentage) {
        this.ratePercentage = ratePercentage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public ArrayList<SupportedLang> getSupportedLangs() {
        return supportedLangs;
    }

    public void setSupportedLangs(ArrayList<SupportedLang> supportedLangs) {
        this.supportedLangs = supportedLangs;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<Document> documents) {
        this.documents = documents;
    }

    public ArrayList<String> getRate() {
        return rate;
    }

    public void setRate(ArrayList<String> rate) {
        this.rate = rate;
    }
}
