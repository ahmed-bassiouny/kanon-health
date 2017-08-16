package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by norhan on 8/1/17.
 */

public class AddClinicResponse extends ParentResponse {

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
