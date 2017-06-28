package com.germanitlab.kanonhealth.initialProfile;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Geram IT Lab on 12/04/2017.
 */

public class CountriesCodes implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("dial_code")
    private String dial_code;
    @SerializedName("code")
    private String code;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDial_code() {
        return dial_code;
    }

    public void setDial_code(String dial_code) {
        this.dial_code = dial_code;
    }

    public String getCode() {
        return code.toLowerCase();
    }

    public void setCode(String code) {
        this.code = code;
    }
}
