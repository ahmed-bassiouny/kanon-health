package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 01/08/17.
 */

public class Language extends ParentModel {

    public final static String KEY_ID ="lang_id";
    public final static String KEY_TITLE ="lang_title";
    public final static String KEY_ICON ="lang_icon";
    public final static String KEY_LANG_SHORT ="long_short";
    public final static String KEY_COUNTRY_CODE ="country_code";

    @SerializedName(KEY_ID)
    private Integer languageID;
    @SerializedName(KEY_TITLE)
    private String languageTitle;
    @SerializedName(KEY_ICON)
    private String languageIcon;
    // shourtcut name of country like Akan => ak
    @SerializedName(KEY_LANG_SHORT)
    private String languageLongShort;
    @SerializedName(KEY_COUNTRY_CODE)
    private String languageCountryCode;

    public Integer getLanguageID() {
        if(languageID==null)
            languageID=0;
        return languageID;
    }

    public void setLanguageID(Integer languageID) {
        this.languageID = languageID;
    }

    public String getLanguageTitle() {
        return languageTitle;
    }

    public void setLanguageTitle(String languageTitle) {
        this.languageTitle = languageTitle;
    }

    public String getLanguageIcon() {
        return languageIcon;
    }

    public void setLanguageIcon(String languageIcon) {
        this.languageIcon = languageIcon;
    }

    public String getLanguageLongShort() {
        return languageLongShort;
    }

    public void setLanguageLongShort(String languageLongShort) {
        this.languageLongShort = languageLongShort;
    }

    public String getLanguageCountryCode() {
        return languageCountryCode;
    }

    public void setLanguageCountryCode(String languageCountryCode) {
        this.languageCountryCode = languageCountryCode;
    }
}
