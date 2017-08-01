package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/1/17.
 */

public class SupportedLang {

    public static final String LANG_TITLE= "lang_title";
    public static final String LANG_ICON = "lang_icon";
    public static final String LONG_SHORT ="long_short";
    public static final String COUNTRY_CODE= "country_code";

    @SerializedName(LANG_TITLE)
    private String langTitle;

    @SerializedName(LANG_ICON)
    private String langIcon;

    @SerializedName(LONG_SHORT)
    private String longShort;

    @SerializedName(COUNTRY_CODE)
    private String countryCode;


}
