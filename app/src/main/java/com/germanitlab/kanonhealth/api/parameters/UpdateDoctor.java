package com.germanitlab.kanonhealth.api.parameters;

import com.germanitlab.kanonhealth.api.models.SupportedLang;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by norhan on 8/1/17.
 */

public class UpdateDoctor extends ParentParameters{
    public static final String PARAMETER_DOCTOR_ID="doc_id";
    public static final String PARAMETER_ADDRESS="address";
    public static final String PARAMETER_SUPPORTED_LANG= "supported_lang";

    @SerializedName(PARAMETER_DOCTOR_ID)
    private Integer userId;

    @SerializedName(PARAMETER_ADDRESS)
    private String address;

    @SerializedName(PARAMETER_SUPPORTED_LANG)
    private ArrayList<SupportedLang> supported_lang;


}
