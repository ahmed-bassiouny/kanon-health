package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class OpenSessionParameters extends ParentParameters {

    public static final String PARAMETER_USERID = "user_id";
    public static final String PARAMETER_DOCTOR_ID = "doc_id";

    @SerializedName(PARAMETER_USERID)
    private String userID;
    @SerializedName(PARAMETER_DOCTOR_ID)
    private String doctorID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
}
