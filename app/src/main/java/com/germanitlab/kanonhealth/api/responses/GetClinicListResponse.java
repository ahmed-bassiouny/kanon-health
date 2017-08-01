package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Clinic;
import com.germanitlab.kanonhealth.api.models.Register;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by norhan on 8/1/17.
 */

public class GetClinicListResponse extends ParentResponse{

    @SerializedName(KEY_DATA)
    private
    ArrayList<Clinic> data;

    public ArrayList<Clinic> getData() {
        return data;
    }

    public void setData(ArrayList<Clinic> data) {
        this.data = data;
    }
}
