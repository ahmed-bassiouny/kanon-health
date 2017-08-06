package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class EditDoctorParameter extends UserAddParameter {
    public static final String PARAMETER_EMAIL = "email";
    public static final String KEY_STREET_NAME = "street_name";
    public static final String KEY_HOUSE_NUMBER = "house_number";
    public static final String KEY_PROVIDENCE = "providence";
    public static final String KEY_ZIP_CODE = "zip_code";


    @SerializedName(PARAMETER_EMAIL)
    private String email;
    @SerializedName(KEY_STREET_NAME)
    private String streetName;
    @SerializedName(KEY_HOUSE_NUMBER)
    private String houseNumber;
    @SerializedName(KEY_PROVIDENCE)
    private String providence;
    @SerializedName(KEY_ZIP_CODE)
    private String zipCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
