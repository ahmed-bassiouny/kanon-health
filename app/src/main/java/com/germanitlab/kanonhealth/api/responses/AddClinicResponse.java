package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Clinic;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by norhan on 8/1/17.
 */

public class AddClinicResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private
    Clinic data;

    public Clinic getData() {
        return data;
    }

    public void setData(Clinic data) {
        this.data = data;
    }
}
