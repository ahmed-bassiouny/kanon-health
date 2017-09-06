package com.germanitlab.kanonhealth.api.responses;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by norhan on 8/1/17.
 */

public class OpenSessionResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private IsOpenResponse data;


    public IsOpenResponse getData() {
        return data;
    }

    public void setData(IsOpenResponse data) {
        this.data = data;
    }
}
