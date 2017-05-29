package com.germanitlab.kanonhealth.models.user;

import android.support.annotation.IdRes;

import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.Info;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Geram IT Lab on 18/03/2017.
 */
@DatabaseTable(tableName = "doctors")

public class User implements Serializable {

     /*
   * {
"id": 4,
    "email": null,
    "password": "78052a040e70547a0df844744bcfa1ee",
    "last_login": "2017-05-14 10:46:56",
    "name": ", Amr",
    "first_name": "Amr",
    "last_name": "",
    "avatar": "",
    "subtitle": " ",
    "phone": "4668426666655",
    "country_code": "+49",
    "active": null,
    "is_doc": 1,
    "platform": 3,
    "token": "fESlX7VwypA:APA91bG78yfSbjyayM8ObfRf88UUPUzPoG2ua0nOpTAQjlyK8CRpSoHh_Wqtpr13YE-z3XYEhr8c1sd4IhvlV9wSUvhM-zPW9XsqXnp_jk_OeBT2LB_ggKAYJM2GTzjiwoobz9_Ihq0w",
    "info": "{\"Birthday\":\"\",\"Country\":\"\",\"House number\":\"\",\"Provinz\":\"\",\"Streetname\":\"\",\"Zip Code\":\"\"}",
    "location_lat": null,
    "location_long": null,
    "payment": null,
    "settings": null,
    "questions": null,
    "address": null,
    "birth_date": "Invalid date",
    "speciality": null,
    "about": null,
    "last_online": null,
    "unreaded_count": null,
    "passcode": null,
    "code": "616401",
    "request_type": null,
    "is_open": null,
    "is_clinic": null,
    "parent_id": null,
    "speciality_id": null,
    "speciality_title": null,
    "speciality_icon": null*/

    @DatabaseField(id = true)
    @SerializedName("id")
    private int id;
    @DatabaseField
    @SerializedName("email")
    private String email;
    @DatabaseField
    private String jsonDocument;
    @DatabaseField
    private String jsonInfo;
    @DatabaseField
    @SerializedName("name")
    private String name;
    @DatabaseField
    @SerializedName("first_name")
    private String first_name;
    @DatabaseField
    @SerializedName("password")
    private String password;
    @DatabaseField
    @SerializedName("qr_url")
    private String qr_url;
    @DatabaseField
    @SerializedName("last_name")
    private String last_name;
    @DatabaseField
    @SerializedName("is_my_doctor")
    private String is_my_doctor;
    @DatabaseField
    @SerializedName("avatar")
    private String avatar;
    @DatabaseField
    @SerializedName("subtitle")
    private String subTitle;
    @DatabaseField
    @SerializedName("last_login")
    private String lastLogin;
    @DatabaseField
    @SerializedName("phone")
    private String phone;
    @DatabaseField
    @SerializedName("country_code")
    private String countryCOde;
    @DatabaseField
    @SerializedName("active")
    private String active;
    @DatabaseField
    @SerializedName("is_doc")
    private int isDoc;
    @DatabaseField
    @SerializedName("is_open")
    private int isOpen;
    @DatabaseField
    @SerializedName("platform")
    private String platform;
    @DatabaseField
    @SerializedName("token")
    private String token;
    @DatabaseField
    @SerializedName("location_lat")
    private double location_lat;
    @DatabaseField
    @SerializedName("location_long")
    private double location_long;
    @DatabaseField
    @SerializedName("payment")
    private String payment;
    @DatabaseField
    @SerializedName("settings")
    private String settings;
    @DatabaseField
    @SerializedName("address")
    private String address;
    @DatabaseField
    @SerializedName("birth_date")
    private String birthDate;
    @DatabaseField
    @SerializedName("speciality")
    private String speciality;
    @DatabaseField
    @SerializedName("about")
    private String about;
    @DatabaseField
    @SerializedName("last_online")
    private String lastOnline;
    @DatabaseField
    @SerializedName("unreaded_count")
    private int unreadedMesCount;
    @DatabaseField
    @SerializedName("rate")
    private String rate;
    @DatabaseField
    @SerializedName("comment")
    private String comment;
    @DatabaseField
    private boolean chosen;
    @SerializedName("questions")
    private LinkedHashMap<String, String> questions;
    @SerializedName("info")
    private Info info;
/*
    @ForeignCollectionField(eager = true , foreignColumnName = "document")
*/
    private ArrayList<Message> documents;

    public int get_Id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCOde() {
        return countryCOde;
    }

    public void setCountryCOde(String countryCOde) {
        this.countryCOde = countryCOde;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getIsDoc() {
        return isDoc;
    }

    public void setIsDoc(int isDoc) {
        this.isDoc = isDoc;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public double getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(double location_lat) {
        this.location_lat = location_lat;
    }

    public double getLocation_long() {
        return location_long;
    }

    public void setLocation_long(double location_long) {
        this.location_long = location_long;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth_date() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public int getUnreadedMesCount() {
        return unreadedMesCount;
    }

    public void setUnreadedMesCount(int unreadedMesCount) {
        this.unreadedMesCount = unreadedMesCount;
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

    public String getComment() {
        return comment;
    }


    @Override
    public String toString() {
        return "DoctorResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", lastLogin='" + lastLogin + '\'' +
                ", phone='" + phone + '\'' +
                ", countryCOde='" + countryCOde + '\'' +
                ", active='" + active + '\'' +
                ", isDoc='" + isDoc + '\'' +
                ", platform='" + platform + '\'' +
                ", token='" + token + '\'' +
                ", location_lat=" + location_lat +
                ", location_long=" + location_long +
                ", payment='" + payment + '\'' +
                ", settings='" + settings + '\'' +
                ", address='" + address + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", speciality='" + speciality + '\'' +
                ", about='" + about + '\'' +
                ", lastOnline='" + lastOnline + '\'' +
                ", unreadedMesCount=" + unreadedMesCount +
                '}';
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
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

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }
    public ArrayList<Message> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<Message> documents) {
        this.documents = documents;
    }

    public String getJsonDocument() {
        return jsonDocument;
    }

    public void setJsonDocument(String jsonDocument) {
        this.jsonDocument = jsonDocument;
    }

    public String getJsonInfo() {
        return jsonInfo;
    }

    public void setJsonInfo(String jsonInfo) {
        this.jsonInfo = jsonInfo;
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

    public String getIs_my_doctor() {
        return is_my_doctor;
    }

    public void setIs_my_doctor(String is_my_doctor) {
        this.is_my_doctor = is_my_doctor;
    }
}



