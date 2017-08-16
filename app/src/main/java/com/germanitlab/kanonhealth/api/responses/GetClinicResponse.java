package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/1/17.
 */

public class GetClinicResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private
    UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
