package com.germanitlab.kanonhealth.models.doctors;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mo on 2/9/17.
 */

public class DoctorRequest implements Serializable {
    @SerializedName("user_id")
    private String userID;
    @SerializedName("password")
    private String password;
    @SerializedName("key")
    private String Key;
    @SerializedName("doc_id")
    private String doc_id;
    @SerializedName("id")
    private String id;
    private int entity_type;
    private int is_object = 1;


    public DoctorRequest(String userID, String password, String Key) {
        this.userID = userID;
        this.password = password;
        this.Key = Key;
    }

    public DoctorRequest(String userID, String password, String Key, String doc_id) {
        this.userID = userID;
        this.password = password;
        this.Key = Key;
        this.doc_id = doc_id;
    }
    public DoctorRequest(String userID, String password, String Key, String doc_id , String id) {
        this.userID = userID;
        this.password = password;
        this.Key = Key;
        this.doc_id = doc_id;
        this.id = id ;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
