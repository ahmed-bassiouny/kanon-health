package com.germanitlab.kanonhealth.models;

import com.germanitlab.kanonhealth.models.user.User;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Geram IT Lab on 23/05/2017.
 */

public class Table implements Serializable {
    private String id;
    private String type;
    private String from;
    private String to;
    private String dayweek;

    @DatabaseField(foreign = true, foreignAutoRefresh = true ,columnName = "id")
    private User uid ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDayweek() {
        return dayweek;
    }

    public void setDayweek(String dayweek) {
        this.dayweek = dayweek;
    }

    public User getUid() {
        return uid;
    }

    public void setUid(User uid) {
        this.uid = uid;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
