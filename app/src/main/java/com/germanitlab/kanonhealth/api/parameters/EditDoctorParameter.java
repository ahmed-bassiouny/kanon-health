package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class EditDoctorParameter extends UserAddParameter {
    public static final String PARAMETER_EMAIL = "email";


    @SerializedName(PARAMETER_EMAIL)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
