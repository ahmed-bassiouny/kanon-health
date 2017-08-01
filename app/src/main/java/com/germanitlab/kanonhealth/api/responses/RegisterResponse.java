package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Register;
import com.google.gson.annotations.SerializedName;

/**
 * Created by milad on 7/31/17.
 */

public class RegisterResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private
    Register data;

    public Register getData() {
        return data;
    }

    public void setData(Register data) {
        this.data = data;
    }
}
