package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by milad on 8/1/17.
 */

public class ParentParameters implements Serializable {

    public String toJson() {
        return (new Gson()).toJson(this);
    }
}
