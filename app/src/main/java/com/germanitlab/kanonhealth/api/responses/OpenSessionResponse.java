package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Clinic;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by norhan on 8/1/17.
 */

public class OpenSessionResponse extends ParentResponse {

    public static final String KEY_REQUEST_ID = "request_id";

    @SerializedName(KEY_DATA)
    private
    HashMap<String, String> data;


    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
