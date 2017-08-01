package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Language;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bassiouny on 01/08/17.
 */

public class LanguageResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private ArrayList<Language> data;

    public ArrayList<Language> getData() {
        return data;
    }
    public void setData(ArrayList<Language> data) {
        this.data = data;
    }
}
