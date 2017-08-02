package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.User;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by norhan on 8/2/17.
 */

public class GetDoctorListResponse extends ParentResponse{

    @SerializedName(KEY_DATA)
    private
    ArrayList<UserInfo> data;

    public ArrayList<UserInfo> getData() {
        return data;
    }

    public void setData(ArrayList<UserInfo> data) {
        this.data = data;
    }
}
