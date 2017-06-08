package com.germanitlab.kanonhealth.models;

import java.io.Serializable;

/**
 * Created by halima on 07/06/17.
 */

/*
{
"id": "0",
"to": "01:15",
"from": "17:12",
"type": "no",
"status": "open",
"dayweek": "0"
},
 */
public class openTime implements Serializable{
    String id;
    String to;
    String from;
    String type;
    String status;
    String dayweek;


    public void setId(String id) {
        this.id = id;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDayweek(String dayweek) {
        this.dayweek = dayweek;
    }
}
