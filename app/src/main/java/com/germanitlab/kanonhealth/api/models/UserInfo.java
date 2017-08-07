package com.germanitlab.kanonhealth.api.models;

import android.text.TextUtils;

import com.germanitlab.kanonhealth.helpers.DateHelper;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by bassiouny on 02/08/17.
 */

public class UserInfo extends ParentModel {

    //region key
    public static final String KEY_USERID = "user_id";
    public static final String KEY_USERTYPE = "user_type";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_COUNTRY_CODE = "country_code";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_BIRTHDAY = "birh_day";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_DOCTOR_ID = "doctor_id";
    public static final String KEY_DOCUMENTS = "documents";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_RATE_NUM = "rate_num";
    public static final String KEY_RATE_PERCENTAGE = "rate_percentage";
    public static final String KEY_SUPPORTED_LANG = "supported_lang";
    public static final String KEY_IS_AVAILABLE = "is_available";
    public static final String KEY_ACTIVATED = "activated";
    public static final String KEY_SPECIALITIES = "specialitys";
    public static final String Clinics = "member_at";
    public static final String KEY_STREET_NAME = "street_name";
    public static final String KEY_HOUSE_NUMBER = "house_number";
    public static final String KEY_PROVIDENCE = "providence";
    public static final String KEY_ZIP_CODE = "zip_code";
    public static final String KEY_QUESTIONS_ANSWERS = "questions_answers";

    // endregion

    // region constraints
    public static final int CLINIC = 3;
    public static final int DOCTOR = 2;
    public static final int PATIENT = 1;
    // endregion

    // region Attributes
    @SerializedName(KEY_USERID)
    private Integer userID;
    @SerializedName(KEY_DOCTOR_ID)
    private Integer doctorID;
    // usertype => 1 patient , 2 doctor
    @SerializedName(KEY_USERTYPE)
    private Integer userType;
    @SerializedName(KEY_FIRSTNAME)
    private String  firstName="";
    @SerializedName(KEY_LASTNAME)
    private String  lastName="";
    @SerializedName(KEY_TITLE)
    private String  title="";
    @SerializedName(KEY_PASSWORD)
    private String password;
    @SerializedName(KEY_COUNTRY_CODE)
    private String country_code;
    @SerializedName(KEY_PHONE)
    private String phone;
    @SerializedName(KEY_GENDER)
    private String gender;
    @SerializedName(KEY_BIRTHDAY)
    private String birthday;
    @SerializedName(KEY_AVATAR)
    private String avatar;
    @SerializedName(KEY_IS_AVAILABLE)
    private Integer isAvailable;
    @SerializedName(KEY_DOCUMENTS)
    private ArrayList<Document> documents;
    @SerializedName(KEY_EMAIL)
    private String email;
    @SerializedName(KEY_RATE_NUM)
    private Float rateNum;
    @SerializedName(KEY_RATE_PERCENTAGE)
    private HashMap<String, Integer> ratePercentage;
    @SerializedName(KEY_STREET_NAME)
    private String streetName;
    @SerializedName(KEY_HOUSE_NUMBER)
    private String houseNumber;
    @SerializedName(KEY_PROVIDENCE)
    private String providence;
    @SerializedName(KEY_ZIP_CODE)
    private String zipCode;
    @SerializedName(KEY_SUPPORTED_LANG)
    private ArrayList<SupportedLang> supportedLangs;

    @SerializedName(KEY_ACTIVATED)
    private Integer isActive;

    @SerializedName(KEY_SPECIALITIES)
    private ArrayList<Speciality> specialities;
    @SerializedName(Clinics)
    private ArrayList<Clinic> clinics;

    private LinkedHashMap<String, String> questionsAnswers;
    // endregion


    // region setter and getter
    public Integer getUserID() {
        if (userID == null)
            userID = 0;
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getUserType() {
        if (userType == null)
            userType = PATIENT;
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
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getAvailable() {
        if(isAvailable==null)
            isAvailable=0;
        return isAvailable;
    }

    public void setAvailable(Integer available) {
        isAvailable = available;
    }


    public ArrayList<SupportedLang> getSupportedLangs() {
        return supportedLangs;
    }

    public void setSupportedLangs(ArrayList<SupportedLang> supportedLangs) {
        this.supportedLangs = supportedLangs;
    }

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
        if(Clinics==null) {
            clinics = new ArrayList<>();
        }
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

    public HashMap<String, Integer> getRatePercentage() {
        return ratePercentage;
    }

    public void setRatePercentage(HashMap<String, Integer> ratePercentage) {
        this.ratePercentage = ratePercentage;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public ArrayList<Speciality> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(ArrayList<Speciality> specialities) {
        this.specialities = specialities;
    }

    public String getFullName() {
        String fullName = "";
        if (!TextUtils.isEmpty(title)) {
            fullName += title;
        }
        if (!TextUtils.isEmpty(firstName)) {
            fullName += " " + firstName;
        }
        if (!TextUtils.isEmpty(lastName)) {
            fullName += " " + lastName;
        }
        return fullName.trim();
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

    public LinkedHashMap<String, String> getQuestionsAnswers() {
        if (questionsAnswers == null) {
            questionsAnswers = new LinkedHashMap<>();
        }
        return questionsAnswers;
    }

    public void setQuestionsAnswers(LinkedHashMap<String, String> questionsAnswers) {
        this.questionsAnswers = questionsAnswers;
    }
    //endregion
}
