package com.germanitlab.kanonhealth.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mo on 3/1/17.
 */

public class UploadImageResponse implements Serializable {

    @SerializedName("file_url")
    private String file_url;

    @SerializedName("Status")
    private int status;


    public int getStatus() {
        return status;
    }

    public String getFile_url() {
        return file_url;
    }


}
