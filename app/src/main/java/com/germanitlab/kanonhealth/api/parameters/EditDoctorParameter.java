package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class EditDoctorParameter extends UserAddParameter {
    public static final String PARAMETER_EMAIL = "email";
    public static final String PARAMETER_ADDRESS = "address";

    @SerializedName(PARAMETER_EMAIL)
    private String email;
    @SerializedName(PARAMETER_ADDRESS)
    private String address;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
