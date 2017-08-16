package com.germanitlab.kanonhealth.api.models;

import android.text.TextUtils;

import com.germanitlab.kanonhealth.helpers.DateHelper;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by bassiouny on 02/08/17.
 */

@DatabaseTable
public class ClinicEdit extends ParentModel {

    //region key
    public static final String KEY_CLINIC_ID= "clinic_id";
    public static final String KEY_OPEN_TYPE = "open_type";
    public static final String KEY_CITY = "city";
    public static final String KEY_COUNTRY = "county";
    public static final String KEY_HOUSE_NUMBER = "house_number";
    public static final String KEY_MEMBER_AT = "member_at";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_PROVIDENCE = "providence";
    public static final String KEY_SPECIALITY = "speciality";
    public static final String KEY_STREET_NAME = "street_name";
    public static final String KEY_LANG_SUB = "lang_sub";
    public static final String KEY_ZIP_CODE = "zip_code";


    // endregion

    // region Attributes
    @SerializedName(KEY_CLINIC_ID)
    private String clinicId;

    @SerializedName(KEY_OPEN_TYPE)
    private String openType;

    @SerializedName(KEY_CITY)
    private String city;

    @SerializedName(KEY_COUNTRY)
    private String country;

    @SerializedName(KEY_HOUSE_NUMBER )
    private String houseNumber;

    @SerializedName(KEY_MEMBER_AT )
    private String memberAt;

    @SerializedName(KEY_NAME)
    private String name;

    @SerializedName(KEY_PHONE)
    private String phone;

    @SerializedName(KEY_PROVIDENCE)
    private String providence;

    @SerializedName(KEY_SPECIALITY)
    private String speciality;
    @SerializedName(KEY_STREET_NAME )
    private String streetName;

    @SerializedName(KEY_LANG_SUB)
    private String langSub;

    @SerializedName(KEY_ZIP_CODE)
    private String zipCode;


    // endregion

    public static String getKeyClinicId() {
        return KEY_CLINIC_ID;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getMemberAt() {
        return memberAt;
    }

    public void setMemberAt(String memberAt) {
        this.memberAt = memberAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvidence() {
        return providence;
    }

    public void setProvidence(String providence) {
        this.providence = providence;
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

    public String getLangSub() {
        return langSub;
    }

    public void setLangSub(String langSub) {
        this.langSub = langSub;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    // region setter and getter


    //endregion
}
