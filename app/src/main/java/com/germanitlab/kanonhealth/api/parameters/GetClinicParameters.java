package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/1/17.
 */

public class GetClinicParameters extends ParentParameters{

    public static final String PARAMETER_CLINIC_ID ="clinic_id";

    @SerializedName(PARAMETER_CLINIC_ID )
    private Integer clinicId;


    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }
}
