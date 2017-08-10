package com.germanitlab.kanonhealth.api.models;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by norhan on 8/10/17.
 */

public class WorkingHours  implements Serializable {

    public static final String KEY_SATURDAY = "saturday";
    public static final String KEY_SUNDAY = "sunday";
    public static final String KEY_MONDAY = "monday";
    public static final String KEY_TUESDAY = "tuesday";
    public static final String KEY_WEDNESDAY = "wednesday";
    public static final String KEY_THURSDAY = "thursday";
    public static final String KEY_FRIDAY = "friday";
    public static final String KEY_OPEN_TYPE = "open_type";


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

    @SerializedName(KEY_OPEN_TYPE)
    private Integer OpenType;

    public List<Times> getSaturday() {
        return saturday;
    }

    public void setSaturday(List<Times> saturday) {
        this.saturday = saturday;
    }

    public List<Times> getSunday() {
        return sunday;
    }

    public void setSunday(List<Times> sunday) {
        this.sunday = sunday;
    }

    public List<Times> getMonday() {
        return monday;
    }

    public void setMonday(List<Times> monday) {
        this.monday = monday;
    }

    public List<Times> getTuesday() {
        return tuesday;
    }

    public void setTuesday(List<Times> tuesday) {
        this.tuesday = tuesday;
    }

    public List<Times> getWednesday() {
        return wednesday;
    }

    public void setWednesday(List<Times> wednesday) {
        this.wednesday = wednesday;
    }

    public List<Times> getThursday() {
        return thursday;
    }

    public void setThursday(List<Times> thursday) {
        this.thursday = thursday;
    }

    public List<Times> getFriday() {
        return friday;
    }

    public void setFriday(List<Times> friday) {
        this.friday = friday;
    }

    public Integer getOpenType() {
        return OpenType;
    }

    public void setOpenType(Integer openType) {
        OpenType = openType;
    }
}
