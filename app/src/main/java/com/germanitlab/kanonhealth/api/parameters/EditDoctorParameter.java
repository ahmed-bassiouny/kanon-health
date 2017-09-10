package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class EditDoctorParameter extends UserAddParameter {
    public static final String PARAMETER_EMAIL = "email";
    public static final String PARAMETER_COUNTRY_CODE = "country_code";
    public static final String PARAMETER_PHONE = "phone";
    public static final String KEY_SPECIALITY = "specialities";
    public static final String KEY_LANG_SUB = "languages";
    @SerializedName(KEY_SPECIALITY)
    private String speciality;

    @SerializedName(KEY_LANG_SUB)
    private String langSub;


    @SerializedName(PARAMETER_EMAIL)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @SerializedName(PARAMETER_COUNTRY_CODE)
    private String countryCode;
    @SerializedName(PARAMETER_PHONE)
    private String phone;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getLangSub() {
        return langSub;
    }

    public void setLangSub(String langSub) {
        this.langSub = langSub;
    }
}
