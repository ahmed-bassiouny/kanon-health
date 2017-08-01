package com.germanitlab.kanonhealth.api.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by milad on 7/31/17.
 */

public class ParentResponse implements Serializable {

    public static final String KEY_STATUS = "status";
    public static final String KEY_TYPE = "type";
    public static final String KEY_MSG = "msg";
    public static final String KEY_DATA = "data";

    @SerializedName(KEY_STATUS)
    private Boolean status;

    @SerializedName(KEY_TYPE)
    private String type;

    @SerializedName(KEY_MSG)
    private String msg;


    public Boolean getStatus() {
        if (status == null) {
            status = false;
        }
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
