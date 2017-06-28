package com.germanitlab.kanonhealth.models;

import java.io.Serializable;

/**
 * Created by eslam on 12/29/16.
 */

public class Speciality implements Serializable {
    private int speciality_id;
    private String speciality_title, speciality_icon;

    public int getSpeciality_id() {
        return speciality_id;
    }

    public void setSpeciality_id(int speciality_id) {
        this.speciality_id = speciality_id;
    }

    public String getSpeciality_title() {
        return speciality_title;
    }

    public void setSpeciality_title(String speciality_title) {
        this.speciality_title = speciality_title;
    }

    public String getSpeciality_icon() {
        return speciality_icon;
    }

    public void setSpeciality_icon(String speciality_icon) {
        this.speciality_icon = speciality_icon;
    }
}
