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


    public int getId() {
        return lang_id;
    }

    public String getAvatar() {
        return lang_icon;
    }

    public String getName() {
        return lang_title;
    }
}
