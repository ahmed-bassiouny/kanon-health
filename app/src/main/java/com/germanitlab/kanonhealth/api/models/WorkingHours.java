package com.germanitlab.kanonhealth.api.models;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by norhan on 8/10/17.
 */

public class WorkingHours  implements Serializable {

    public static final String KEY_SATURDAY = "SAT";
    public static final String KEY_SUNDAY = "SUN";
    public static final String KEY_MONDAY = "MON";
    public static final String KEY_TUESDAY = "TUE";
    public static final String KEY_WEDNESDAY = "WED";
    public static final String KEY_THURSDAY = "THU";
    public static final String KEY_FRIDAY = "FRI";


    @SerializedName(KEY_SATURDAY)
    private List<Times> saturday;
    @SerializedName(KEY_SUNDAY)
    private List<Times> sunday;
    @SerializedName(KEY_MONDAY)
    private List<Times> monday;
    @SerializedName(KEY_TUESDAY)
    private List<Times> tuesday;
    @SerializedName(KEY_WEDNESDAY)
    private List<Times> wednesday;
    @SerializedName(KEY_THURSDAY)
    private List<Times> thursday;
    @SerializedName(KEY_FRIDAY)
    private List<Times> friday;


    public List<Times> getSaturday() {
        if(saturday==null)
            saturday=new ArrayList<>();
        return saturday;
    }

    public void setSaturday(List<Times> saturday) {
        this.saturday = saturday;
    }

    public List<Times> getSunday() {
        if(sunday==null)
            sunday= new ArrayList<>();
        return sunday;
    }

    public void setSunday(List<Times> sunday) {
        this.sunday = sunday;
    }

    public List<Times> getMonday() {
        if(monday==null)
            monday= new ArrayList<>();
        return monday;
    }

    public void setMonday(List<Times> monday) {
        this.monday = monday;
    }

    public List<Times> getTuesday() {
        if(tuesday==null)
            tuesday= new ArrayList<>();
        return tuesday;
    }

    public void setTuesday(List<Times> tuesday) {
        this.tuesday = tuesday;
    }

    public List<Times> getWednesday() {
        if(wednesday==null)
            wednesday= new ArrayList<>();
        return wednesday;
    }

    public void setWednesday(List<Times> wednesday) {
        this.wednesday = wednesday;
    }

    public List<Times> getThursday() {
        if(thursday==null)
            thursday= new ArrayList<>();
        return thursday;
    }

    public void setThursday(List<Times> thursday) {
        this.thursday = thursday;
    }

    public List<Times> getFriday() {
        if(friday==null)
            friday= new ArrayList<>();
        return friday;
    }

    public void setFriday(List<Times> friday) {
        this.friday = friday;
    }
}
