package com.germanitlab.kanonhealth.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eslam on 1/18/17.
 */

public class UserRegisterRequest implements Serializable {

    @SerializedName("phone")
    private String phone;
    @SerializedName("country_code")
    private String countryCode;
    @SerializedName("platform")
    private String platform;

    public UserRegisterRequest(String phone, String countryCode, String platform) {
        this.phone = phone;
        this.countryCode = countryCode;
        this.platform = platform;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
