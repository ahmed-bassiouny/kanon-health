package com.germanitlab.kanonhealth.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eslam on 1/28/17.
 */

public class Info implements Serializable {

/*
"info": {
            "Birthday": "",
            "Streetname": "",
            "House number": "",
            "Zip Code": "",
            "Provinz": "",
            "Country": ""
        },*/


    @SerializedName("Birthday")
    private String birthday;
    @SerializedName("Streetname")
    private String streetname;
    @SerializedName("House number")
    private String houseNumber;
    @SerializedName("Zip Code")
    private String zipCode;
    @SerializedName("Provinz")
    private String provinz;
    @SerializedName("Country")
    private String country;


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getProvinz() {
        return provinz;
    }

    public void setProvinz(String provinz) {
        this.provinz = provinz;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
