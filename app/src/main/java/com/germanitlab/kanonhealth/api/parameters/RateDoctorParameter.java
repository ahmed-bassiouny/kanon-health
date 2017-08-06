package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class RateDoctorParameter extends ParentParameters {
    public static final String PARAMETER_USER_ID = "user_id";
    public static final String PARAMETER_DOCTOR_ID = "doctor_id";
    public static final String PARAMETER_REQUEST_ID = "req_id";
    public static final String PARAMETER_COMMENT = "comment";
    public static final String PARAMETER_RATE = "rate";

    @SerializedName(PARAMETER_USER_ID)
    private String userId;

    @SerializedName(PARAMETER_DOCTOR_ID)
    private String doctorId;

    @SerializedName(PARAMETER_REQUEST_ID)
    private String requestId;

    @SerializedName(PARAMETER_COMMENT)
    private String comment;

    @SerializedName(PARAMETER_RATE)
    private String rate;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
