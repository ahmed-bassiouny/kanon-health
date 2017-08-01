package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Speciality;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bassiouny on 01/08/17.
 */

public class SpecialityResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private ArrayList<Speciality> data;

    public ArrayList<Speciality> getData() {
        return data;
    }
    public void setData(ArrayList<Speciality> data) {
        this.data = data;
    }
}
