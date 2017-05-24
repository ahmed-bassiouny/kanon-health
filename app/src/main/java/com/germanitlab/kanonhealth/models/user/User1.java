package com.germanitlab.kanonhealth.models.user;

import com.germanitlab.kanonhealth.models.doctors.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.germanitlab.kanonhealth.models.messages.Message;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by eslam on 1/28/17.
 */
@DatabaseTable(tableName = "user")
public class User1 extends User implements Serializable {



    @DatabaseField
    @SerializedName("password")
    private String password;
    @DatabaseField
    @SerializedName("qr_url")
    private String qr_url;

    @DatabaseField
    private ArrayList<Message> documents;
    @SerializedName("questions")
    private LinkedHashMap<String, String> questions;
    @DatabaseField
    @SerializedName("info")
    private Info info;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQr_url() {
        return qr_url;
    }

    public void setQr_url(String qr_url) {
        this.qr_url = qr_url;
    }

    public ArrayList<Message> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<Message> documents) {
        this.documents = documents;
    }


    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }


    public  LinkedHashMap<String, String> getQuestions() {
        return questions;
    }

    public void setQuestions( LinkedHashMap<String, String> questions) {
        this.questions = questions;
    }


}
