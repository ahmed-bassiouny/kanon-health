package com.germanitlab.kanonhealth.api.parameters;

import com.germanitlab.kanonhealth.api.ApiUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by milad on 8/1/17.
 */

public class ParentParameters implements Serializable {

    public String toJson() {
        return (new Gson()).toJson(this);
    }


    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> result = new HashMap<>();
        try {
            result = ApiUtils.jsonToMap(new JSONObject(toJson()));
        } catch (Exception e) {

        } finally {
            return result;
        }
    }
}
