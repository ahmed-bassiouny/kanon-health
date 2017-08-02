package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/2/17.
 */

public class DocumentPrivacyParameters extends ParentParameters{

    public static final String PARAMETER_DOCUMENT_ID="document_id";
    public static final String PARAMETER_PRIVACY= "privacy";

    @SerializedName(PARAMETER_DOCUMENT_ID)
    private Integer documentId;

    @SerializedName(PARAMETER_PRIVACY)
    private Integer privacy;

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }
}
