package com.germanitlab.kanonhealth.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Geram IT Lab on 25/04/2017.
 */

public class Payment implements Serializable {
    @SerializedName("user_id")
    private String id;
    @SerializedName("password")
    private String password;
    @SerializedName("type")
    private String type;
    @SerializedName("doc_id")
    private String doc_id;

    public Payment(String id, String password, String doc_id, String type) {
        this.id = id;
        this.password = password;
        this.type = type;
        this.doc_id = doc_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }
}
