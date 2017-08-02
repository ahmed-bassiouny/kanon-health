package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class CloseSessionParameters extends ParentParameters {

    public static final String PARAMETER_REQUEST_ID = "id_request";

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    // chat id to close it
    @SerializedName(PARAMETER_REQUEST_ID)
    private int requestID;


}
