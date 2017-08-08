package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by norhan on 8/1/17.
 */

@DatabaseTable(tableName = "document")
public class Document {
    public static final String KEY_DOCUMENT_ID ="document_id";
    public static final String KEY_TYPE="type";
    public static final String KEY_DOCUMENT="document";
    public static final String KEY_IMAGE="image";
    public static final String KEY_PRIVACY="privacy";
    public static final String KEY_CREATED_AT = "created_at";


    @SerializedName(KEY_DOCUMENT_ID)
    @DatabaseField
    private Integer documentId;

    @SerializedName(KEY_TYPE)
    @DatabaseField
    private String type;

    @SerializedName(KEY_DOCUMENT)
    @DatabaseField
    private String document;

    @SerializedName(KEY_IMAGE)
    @DatabaseField
    private String media;

    @SerializedName(KEY_PRIVACY)
    @DatabaseField
    private Integer privacy;
    // date time of message (UTC)
    @SerializedName(KEY_CREATED_AT)
    @DatabaseField
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
