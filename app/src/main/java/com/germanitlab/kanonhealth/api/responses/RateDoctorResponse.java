package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Clinic;
import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/1/17.
 */

public class RateDoctorResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private
    boolean data;

    public boolean getData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
