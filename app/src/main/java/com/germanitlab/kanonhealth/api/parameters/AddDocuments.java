package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/1/17.
 */

public class AddDocuments extends ParentParameters {
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

}
