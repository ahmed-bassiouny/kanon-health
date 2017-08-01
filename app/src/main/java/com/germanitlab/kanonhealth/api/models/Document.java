package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/1/17.
 */

public class Document {
    public static final String KEY_DOCUMENT_ID ="document_id";
    public static final String KEY_TYPE="type";
    public static final String KEY_DOCUMENT="document";
    public static final String KEY_IMAGE="image";
    public static final String KEY_PRIVACY="privacy";

    @SerializedName(KEY_DOCUMENT_ID)
    private Integer documentId;

    @SerializedName(KEY_TYPE)
    private String type;

    @SerializedName(KEY_DOCUMENT)
    private String document;

    @SerializedName(KEY_IMAGE)
    private String image;

    @SerializedName(KEY_PRIVACY)
    private Integer privacy;

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }
}
