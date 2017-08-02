package com.germanitlab.kanonhealth.api.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/2/17.
 */

public class DocumentPrivacyResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private
    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
