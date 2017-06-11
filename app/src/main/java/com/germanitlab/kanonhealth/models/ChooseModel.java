package com.germanitlab.kanonhealth.models;

import com.google.gson.annotations.SerializedName;

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
public class ChooseModel implements Serializable{

    // language
    private int lang_id;
    private String lang_title;
    private String lang_icon;
    private String long_short;

    // Specialist
    private int speciality_id;
    private String speciality_title;
    private String speciality_icon;

    // Member at
    private int id;
    private String avatar;

    public int getIdMember() {
        return id;
    }

    public void setIdMember(int id) {
        this.id = id;
    }

    public String getAvatarMember() {
        return avatar;
    }

    public void setAvatarMember(String avatar) {
        this.avatar = avatar;
    }

    public String getLast_nameMember() {
        return last_name;
    }

    public void setLast_nameMember(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_nameMember() {
        return first_name;
    }

    public void setFirst_nameMember(String first_name) {
        this.first_name = first_name;
    }

    private String last_name;
    private String first_name;

    private boolean is_my_choise=false;


    public int getLang_id() {
        return lang_id;
    }

    public void setLang_id(int lang_id) {
        this.lang_id = lang_id;
    }

    public String getLang_title() {
        return lang_title;
    }

    public void setLang_title(String lang_title) {
        this.lang_title = lang_title;
    }

    public String getLang_icon() {
        return lang_icon;
    }

    public void setLang_icon(String lang_icon) {
        this.lang_icon = lang_icon;
    }

    public String getLong_short() {
        return long_short;
    }

    public void setLong_short(String long_short) {
        this.long_short = long_short;
    }

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

    public boolean getIsMyChoise(){
        return is_my_choise;
    }
    public void setIsMyChoise(boolean is_my_choise){
        this.is_my_choise=is_my_choise;
    }


}
