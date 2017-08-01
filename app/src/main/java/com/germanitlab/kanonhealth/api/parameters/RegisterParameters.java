package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by milad on 7/31/17.
 */

public class RegisterParameters extends ParentParameters {

    public static final String PARAMETER_COUNTRY_CODE = "country_code";
    public static final String PARAMETER_PHONE = "phone";


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
}
