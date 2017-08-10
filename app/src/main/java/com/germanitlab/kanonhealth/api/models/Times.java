package com.germanitlab.kanonhealth.api.models;

import java.io.Serializable;

/**
 * Created by norhan on 8/10/17.
 */

public class Times  implements Serializable {
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