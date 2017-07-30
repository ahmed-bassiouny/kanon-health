package com.germanitlab.kanonhealth.models;

import java.io.Serializable;

/**
 * Created by andy on 7/26/17.
 */

public class Times implements Serializable {
    private String from;
    private String to;

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
