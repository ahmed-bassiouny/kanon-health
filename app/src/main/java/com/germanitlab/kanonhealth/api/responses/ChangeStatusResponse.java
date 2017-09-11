package com.germanitlab.kanonhealth.api.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by norhan on 8/2/17.
 */

public class ChangeStatusResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private
   ArrayList<String> data;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
