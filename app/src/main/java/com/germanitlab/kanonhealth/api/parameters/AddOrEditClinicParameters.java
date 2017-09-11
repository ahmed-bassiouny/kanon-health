package com.germanitlab.kanonhealth.api.parameters;

import com.germanitlab.kanonhealth.api.models.SupportedLang;
import com.germanitlab.kanonhealth.api.models.WorkingHours;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by norhan on 8/1/17.
 */

public class AddOrEditClinicParameters extends ParentParameters{
    public static final String PARAMETER_NAME="name";
    public static final String PARAMETER_AVATAR ="avatar";
    public static final String PARAMETER_SPECIALITY="specialities";
    public static final String PARAMETER_RATE_NUM="rate_num";
    public static final String  PARAMETER_RATE_PERCENTAGE="rate_percentage";
    public static final String  PARAMETER_ADDRESS="address";
    public static final String  PARAMETER_STREET_NAME="street_name";
    public static final String  PARAMETER_HOUSE_NUMBER="house_number";
    public static final String  PARAMETER_ZIP_CODE= "zip_code";
    public static final String  PARAMETER_CITY= "city";
    public static final String PARAMETER_PROVINCE=  "providence";
    public static final String PARAMETER_COUNTRY="county";
    public static final String PARAMETER_PHONE= "phone";
    public static final String PARAMETER_FAX= "fax";
    public static final String PARAMETER_SUPPORTED_LANG= "languages";
    public static final String PARAMETER_OPEN_TYPE= "open_type";
    public static final String KEY_TIME_TABLE= "working_hours";
    public static final String PARAMETER_DOCTORS= "member_at";



// user id in case of add or clinic id in case of edit


    @SerializedName(PARAMETER_NAME)
    private String name;

    @SerializedName(PARAMETER_AVATAR)
    private String avatar;


    @SerializedName(PARAMETER_SPECIALITY)
    private String speciality;


    @SerializedName(PARAMETER_STREET_NAME)
    private String streetName;

    @SerializedName(PARAMETER_HOUSE_NUMBER )
    private String houseNumber;

    @SerializedName(PARAMETER_ZIP_CODE)
    private String zipCode;

    @SerializedName(PARAMETER_CITY )
    private String city;

    @SerializedName(PARAMETER_PROVINCE)
    private String province;

    @SerializedName(PARAMETER_COUNTRY)
    private String country;

    @SerializedName(PARAMETER_PHONE)
    private String phone;

    @SerializedName(PARAMETER_SUPPORTED_LANG)
    private String supportedLangs;

    @SerializedName(PARAMETER_OPEN_TYPE)
    private String OpenType;

    @SerializedName(PARAMETER_DOCTORS)
    private String memberDoctors;

    @SerializedName(KEY_TIME_TABLE)
    private String timeTable;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
//
    public String getSupportedLangs() {
        return supportedLangs;
    }

    public void setSupportedLangs(String supportedLangs) {
        this.supportedLangs = supportedLangs;
    }

    public String getOpenType() {
        return OpenType;
    }

    public void setOpenType(String openType) {
        OpenType = openType;
    }

    public String getMemberDoctors() {
        return memberDoctors;
    }

    public void setMemberDoctors(String memberDoctors) {
        this.memberDoctors = memberDoctors;
    }

    public ArrayList<WorkingHours> getTimeTable() {
        Gson gson = new Gson();
        ArrayList<WorkingHours> workingHourses = gson.fromJson(timeTable, new TypeToken<List<WorkingHours>>(){}.getType());
        if (workingHourses == null) {
            workingHourses = new ArrayList<>();
        }
        return workingHourses;
    }

    public void setTimeTable(ArrayList<WorkingHours> timeTable) {
        this.timeTable = new Gson().toJson(timeTable);
    }
}
