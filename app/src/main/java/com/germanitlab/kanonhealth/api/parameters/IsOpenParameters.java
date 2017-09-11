package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed on 8/28/17.
 */

public class IsOpenParameters extends ParentParameters {
    public static final String PARAMETER_USER_ID = "user_id";
    public static final String PARAMETER_DOC_ID = "doc_id";
    public static final String PARAMETER_CLINIC_ID = "clinic_id";

    @SerializedName(PARAMETER_USER_ID)
    private int userId;
    @SerializedName(PARAMETER_DOC_ID)
    private int docId;
    @SerializedName(PARAMETER_CLINIC_ID)
    private int clinicId;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }
}
