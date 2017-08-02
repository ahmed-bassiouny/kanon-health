package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class UserOption {
    public static final String KEY_ISOPEN="open";
    public static final String KEY_ISAVAILABLE="available";

    @SerializedName(KEY_ISOPEN)
    private Integer isOpen;
    @SerializedName(KEY_ISAVAILABLE)
    private Integer isAvailable;

    public Integer getOpen() {
        if(isOpen==null)
            isOpen=0;
        return isOpen;
    }

    public void setOpen(Integer open) {
        isOpen = open;
    }

    public Integer getAvailable() {
        if(isAvailable==null)
            isAvailable=0;
        return isAvailable;
    }

    public void setAvailable(Integer available) {
        isAvailable = available;
    }
}
