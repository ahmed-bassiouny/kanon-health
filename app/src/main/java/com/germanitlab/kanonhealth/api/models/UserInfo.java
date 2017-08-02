package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

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
    public static final String KEY_DOCTOR_ID="doctor_id";
    public static final String KEY_CLINICS="clinics";
    public static final String KEY_DOCUMENTS="documents";
    public static final String KEY_EMAIL =    "email";
    public static final String KEY_RATE_NUM = "rate_num";
    public static final String KEY_RATE_PERCENTAGE = "rate_percentage";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_SUPPORTED_LANG = "supported_lang";
    public static final String KEY_IS_AVAILABLE= "is_available";


    public static final int DOCTOR=2;
    public static final int PATIENT=1;

    @SerializedName(KEY_USERID)
    private Integer userID;

    @SerializedName(KEY_DOCTOR_ID)
    private Integer doctorID;
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
    @SerializedName(KEY_IS_AVAILABLE)
    private Integer isAvailable;

    @SerializedName(KEY_DOCUMENTS )
    private ArrayList<Document> documents;

    @SerializedName(KEY_CLINICS)
    private ArrayList<Clinic> clinics;

    @SerializedName(KEY_EMAIL)
    private String email;

    @SerializedName(KEY_RATE_NUM)
    private Float rateNum;

    @SerializedName(KEY_RATE_PERCENTAGE)
    private HashMap<String, String> ratePercentage;

    @SerializedName(KEY_ADDRESS)
    private String address;

    @SerializedName(KEY_SUPPORTED_LANG)

    //private ArrayList<SupportedLang> supportedLangs;


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
            userType=PATIENT;
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
    public Integer getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Integer available) {
        isAvailable = available;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    /*public ArrayList<SupportedLang> getSupportedLangs() {
        return supportedLangs;
    }

    public void setSupportedLangs(ArrayList<SupportedLang> supportedLangs) {
        this.supportedLangs = supportedLangs;
    }*/

    public static String getKeyUserid() {
        return KEY_USERID;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<Document> documents) {
        this.documents = documents;
    }

    public ArrayList<Clinic> getClinics() {
        return clinics;
    }

    public void setClinics(ArrayList<Clinic> clinics) {
        this.clinics = clinics;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    public int getIsOpen(){
        return 0;
    }
    public void setIsOpen(int i){

    }
}
