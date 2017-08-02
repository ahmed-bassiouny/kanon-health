package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class EditPatientParameter extends UserParameter {

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
