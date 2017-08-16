package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.ClinicEdit;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.helpers.InternetFilesOperations;
import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/1/17.
 */

public class EditClinicResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private
    ClinicEdit data;

    public ClinicEdit getData() {
        return data;
    }

    public void setData(ClinicEdit data) {
        this.data = data;
    }
}
