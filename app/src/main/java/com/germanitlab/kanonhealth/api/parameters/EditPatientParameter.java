package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class EditPatientParameter extends UserParameter {

    public static final String PARAMETER_COUNTRY_CODE = "country_code";

    @SerializedName(PARAMETER_COUNTRY_CODE)
    private String countryCode;

    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
