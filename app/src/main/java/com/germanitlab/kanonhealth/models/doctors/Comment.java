package com.germanitlab.kanonhealth.models.doctors;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 06/06/17.
 */

public class Comment {
    @SerializedName("user_id")
    private int userID;
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

    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;

    @SerializedName("avatar")
    private String avatar;
    @SerializedName("country_flag")
    private String country_flag;


    public Comment(int userID, String password, String key, String doc_id, String comment, String rate) {
        this.userID = userID;
        this.password = password;
        this.Key = key;
        this.doc_id = doc_id;
        this.comment = comment;
        this.rate = rate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCountry_flag() {
        return country_flag;
    }

    public void setCountry_flag(String country_flag) {
        this.country_flag = country_flag;
    }

    public Comment(int userID, String password, String key, String doc_id) {
        this.userID = userID;
        this.password = password;
        this.Key = key;
        this.doc_id = doc_id;

    }
}
