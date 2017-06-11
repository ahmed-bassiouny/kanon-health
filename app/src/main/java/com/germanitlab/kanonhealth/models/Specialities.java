package com.germanitlab.kanonhealth.models;

import java.io.Serializable;

/**
 * Created by halima on 07/06/17.
 */

/*
{
"speciality_id": 1,
"speciality_icon": "spcs_icons/spc-icon.png",
"speciality_title": "Dentist"
},

 */
public class Specialities implements Serializable{
    private int speciality_id;
    private String speciality_icon;
    private String speciality_title;
    private boolean is_my_specialities=false;

    public void setIs_my_specialities(boolean is_my_specialities) {
        this.is_my_specialities = is_my_specialities;
    }

    public boolean is_my_specialities() {
        return is_my_specialities;
    }

    public int getId() {
        return speciality_id;
    }

    public String getAvatar() {
        return speciality_icon;
    }

    public String getTitle() {
        return speciality_title;
    }
}
