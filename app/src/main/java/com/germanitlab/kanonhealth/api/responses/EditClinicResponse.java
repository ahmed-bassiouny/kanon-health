package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Clinic;
import com.germanitlab.kanonhealth.helpers.InternetFilesOperations;
import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/1/17.
 */

public class EditClinicResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private
    Integer data;

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }
}
