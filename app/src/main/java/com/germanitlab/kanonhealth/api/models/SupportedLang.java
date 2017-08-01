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

    public static String getLangTitle() {
        return LANG_TITLE;
    }

    public void setLangTitle(String langTitle) {
        this.langTitle = langTitle;
    }

    public String getLangIcon() {
        return langIcon;
    }

    public void setLangIcon(String langIcon) {
        this.langIcon = langIcon;
    }

    public String getLongShort() {
        return longShort;
    }

    public void setLongShort(String longShort) {
        this.longShort = longShort;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
