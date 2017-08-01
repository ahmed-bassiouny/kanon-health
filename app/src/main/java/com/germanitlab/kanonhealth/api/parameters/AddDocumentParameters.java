package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/1/17.
 */

public class AddDocumentParameters extends ParentParameters {
    public static final String PARAMETER_USER_ID ="user_id";
    public static final String PARAMETER_TYPE="type";
    public static final String PARAMETER_DOCUMENT="document";
    public static final String PARAMETER_IMAGE="image";

    @SerializedName(PARAMETER_USER_ID)
    private Integer userId;

    @SerializedName(PARAMETER_TYPE)
    private String type;

    @SerializedName(PARAMETER_DOCUMENT)
    private String document;

    @SerializedName(PARAMETER_IMAGE)
    private String image;

    public static String getParameterUserId() {
        return PARAMETER_USER_ID;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}
