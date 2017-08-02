package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class OpenSessionParameters extends ParentParameters {

    public static final String PARAMETER_USERID = "user_id";
    public static final String PARAMETER_DOCTOR_ID = "doc_id";

    @SerializedName(PARAMETER_USERID)
    private int userID;
    @SerializedName(PARAMETER_DOCTOR_ID)
    private int doctorID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }
}
