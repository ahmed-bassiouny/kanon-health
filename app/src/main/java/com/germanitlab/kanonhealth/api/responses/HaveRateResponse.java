package com.germanitlab.kanonhealth.api.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nour on 13-Sep-17.
 */

public class HaveRateResponse {
    public static final String KEY_STATUS = "status";
    public static final String KEY_REQUEST = "request_id";

    @SerializedName(KEY_STATUS)
    private int status;
    @SerializedName(KEY_REQUEST)
    private int requestId;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
