package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/17/17.
 */

public class DoctorWorkingHoursParameters  extends ParentParameters{
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_OPEN_TYPE = "open_type";
    public static final String KEY_SATURDAY = "saturday";
    public static final String KEY_SUNDAY = "sunday";
    public static final String KEY_MONDAY = "monday";
    public static final String KEY_TUESDAY = "tuesday";
    public static final String KEY_WEDNESDAY = "wednesday";
    public static final String KEY_THURSDAY = "thursday";
    public static final String KEY_FRIDAY = "friday";


    @SerializedName(KEY_SATURDAY)
    private String saturday;
    @SerializedName(KEY_SUNDAY)
    private String sunday;
    @SerializedName(KEY_MONDAY)
    private String monday;
    @SerializedName(KEY_TUESDAY)
    private String tuesday;
    @SerializedName(KEY_WEDNESDAY)
    private String wednesday;
    @SerializedName(KEY_THURSDAY)
    private String thursday;
    @SerializedName(KEY_FRIDAY)
    private String friday;
    @SerializedName(KEY_USER_ID)
    private String userId;
    @SerializedName(KEY_OPEN_TYPE)
    private String OpenType;


    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public static String getKeyUserId() {
        return KEY_USER_ID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenType() {
        return OpenType;
    }

    public void setOpenType(String openType) {
        OpenType = openType;
    }
}
