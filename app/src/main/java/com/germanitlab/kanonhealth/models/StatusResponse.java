package com.germanitlab.kanonhealth.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eslam on 2/4/17.
 */

public class StatusResponse implements Serializable {

    private int status;
    private String is_available;


    public String getIs_available() {
        return is_available;
    }
}
