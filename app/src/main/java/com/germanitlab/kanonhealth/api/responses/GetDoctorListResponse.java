package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by norhan on 8/2/17.
 */

public class GetDoctorListResponse extends ParentResponse{

    @SerializedName(KEY_DATA)
    private
    ArrayList<User> data;

    public ArrayList<User> getData() {
        return data;
    }

    public void setData(ArrayList<User> data) {
        this.data = data;
    }
}
