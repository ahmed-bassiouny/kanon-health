package com.germanitlab.kanonhealth.models;

import com.germanitlab.kanonhealth.models.user.User;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Geram IT Lab on 23/05/2017.
 */

public class Table implements Serializable {
    private List<Times> sat;
    private List<Times> sun;
    private List<Times> mon;
    private List<Times> tues;
    private List<Times> wedn;
    private List<Times> thurs;
    private List<Times> fri;

    @DatabaseField(foreign = true, foreignAutoRefresh = true ,columnName = "id")
    private User uid ;

    public List<Times> getSat() {
        return sat;
    }

    public void setSat(List<Times> sat) {
        this.sat = sat;
    }

    public List<Times> getSun() {
        return sun;
    }

    public void setSun(List<Times> sun) {
        this.sun = sun;
    }

    public List<Times> getMon() {
        return mon;
    }

    public void setMon(List<Times> mon) {
        this.mon = mon;
    }

    public List<Times> getTues() {
        return tues;
    }

    public void setTues(List<Times> tues) {
        this.tues = tues;
    }

    public List<Times> getWedn() {
        return wedn;
    }

    public void setWedn(List<Times> wedn) {
        this.wedn = wedn;
    }

    public List<Times> getThurs() {
        return thurs;
    }

    public void setThurs(List<Times> thurs) {
        this.thurs = thurs;
    }

    public List<Times> getFri() {
        return fri;
    }

    public void setFri(List<Times> fri) {
        this.fri = fri;
    }
}
