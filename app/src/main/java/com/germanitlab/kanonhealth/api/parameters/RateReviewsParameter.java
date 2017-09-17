package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nour on 17-Sep-17.
 */

public class RateReviewsParameter extends ParentParameters {
    public static final String PARAMETER_DOCTOR_ID = "doctor_id";
    public static final String PARAMETER_CLINIC_ID = "clinic_id";

    @SerializedName(PARAMETER_DOCTOR_ID)
    private String  doctorID;

    @SerializedName(PARAMETER_CLINIC_ID)
    private String  clinicID;

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getClinicID() {
        return clinicID;
    }

    public void setClinicID(String clinicID) {
        this.clinicID = clinicID;
    }
}
