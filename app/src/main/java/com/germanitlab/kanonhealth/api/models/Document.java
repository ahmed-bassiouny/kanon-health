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
    public static final String KEY_CREATED_AT = "created_at";


    @SerializedName(KEY_DOCUMENT_ID)
    private Integer documentId;

    @SerializedName(KEY_TYPE)
    private String type;

    @SerializedName(KEY_DOCUMENT)
    private String document;

    @SerializedName(KEY_IMAGE)
    private String media;

    @SerializedName(KEY_PRIVACY)
    private Integer privacy;
    // date time of message (UTC)
    @SerializedName(KEY_CREATED_AT)
    private String dateTime;

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


    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
