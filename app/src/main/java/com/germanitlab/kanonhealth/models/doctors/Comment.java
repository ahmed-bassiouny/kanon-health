package com.germanitlab.kanonhealth.models.doctors;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 06/06/17.
 */

public class Comment {
    @SerializedName("user_id")
    private String userID;
    @SerializedName("password")
    private String password;
    @SerializedName("key")
    private String Key;
    @SerializedName("doc_id")
    private String doc_id;
    @SerializedName("comment")
    private String comment;
    @SerializedName("rate")
    private String rate;

    public Comment(String userID, String password, String key, String doc_id, String comment, String rate) {
        this.userID = userID;
        this.password = password;
        this.Key = key;
        this.doc_id = doc_id;
        this.comment = comment;
        this.rate = rate;
    }
}
