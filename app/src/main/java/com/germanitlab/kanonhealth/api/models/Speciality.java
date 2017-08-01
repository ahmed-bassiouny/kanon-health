package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 01/08/17.
 */

public class Speciality extends ParentModel {


    public static final String KEY_ID="id";
    public static final String KEY_TITLE="speciality_title";
    public static final String KEY_ICON="speciality_icon";

    @SerializedName(KEY_ID)
    private Integer specialityID;
    // name of speciality
    @SerializedName(KEY_TITLE)
    private String title;
    // speciality photo
    @SerializedName(KEY_ICON)
    private String image;

    public Integer getSpecialityID() {
        return specialityID;
    }

    public void setSpecialityID(Integer specialityID) {
        this.specialityID = specialityID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
