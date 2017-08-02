package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by norhan on 8/1/17.
 */

public class User {
    public static final String KEY_ID="user_id";
    public static final String KEY_RATE_NUM = "rate_num";
    public static final String KEY_RATE_PERCENTAGE = "rate_percentage";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_SUPPORTED_LANG = "supported_lang";
    public static final String KEY_IS_AVAILABLE= "is_available";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_FIRST_NAME =      "first_name";
    public static final String KEY_LAST_NAME=    "last_name";
    public static final String KEY_TITLE=    "title";
    public static final String KEY_PHONE =   "phone";
    public static final String KEY_COUNTRY_CODE =   "country_code";
    public static final String KEY_RATE =    "rate";
    public static final String KEY_DOCUMENTS =    "documents";
    public static final String KEY_PASSWORD =    "password";
    public static final String KEY_GENDER =    "gender";
    public static final String KEY_BIRTH_DAY =    "birh_day";
    public static final String KEY_EMAIL =    "email";



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
    private String countryCode;

    @SerializedName(KEY_COUNTRY_CODE)
    private String phone;

    @SerializedName(KEY_FIRST_NAME )
    private String firstName;

    @SerializedName(KEY_LAST_NAME )
    private String lastName;

    @SerializedName(KEY_TITLE )
    private String title;

    @SerializedName(KEY_EMAIL)
    private String email;

    @SerializedName(KEY_BIRTH_DAY)
    private String birthDay;

    @SerializedName(KEY_PASSWORD )
    private String password;

    @SerializedName(KEY_GENDER)
    private String gender;

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
    public static String getKeyRateNum() {
        return KEY_RATE_NUM;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
