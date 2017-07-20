package com.germanitlab.kanonhealth.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eslam on 1/28/17.
 */

public class Info implements Serializable {


    @SerializedName("Birthday")
    private String birthday;
    @SerializedName("streetname")
    private String streetname;
    @SerializedName("house_number")
    private String houseNumber;
    @SerializedName("zip_code")
    private String zip_code;
    @SerializedName("provinz")
    private String provinz;
    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private String city;
    @SerializedName("hotline")
    private String hotline;
    @SerializedName("fax")
    private String fax;
    @SerializedName("website")
    private String website;

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

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

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
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
