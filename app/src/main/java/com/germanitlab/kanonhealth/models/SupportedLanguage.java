package com.germanitlab.kanonhealth.models;

import java.io.Serializable;

/**
 * Created by halima on 07/06/17.
 */

public class SupportedLanguage implements Serializable {
    /*
    "lang_id": 1,
"lang_icon": "spcs_icons/spc-icon.png",
"lang_title": "Arabic"
     */
    int lang_id;
    String lang_icon;
    String lang_title;


    public int getLang_id() {
        return lang_id;
    }

    public String getLang_icon() {
        return lang_icon;
    }

    public String getLang_title() {
        return lang_title;
    }
}
