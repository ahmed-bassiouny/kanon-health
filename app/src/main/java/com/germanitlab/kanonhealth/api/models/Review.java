package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bassiouny on 08/08/17.
 */

public class Review extends ParentModel {

    public static final String KEY_RATE="rate";
    public static final String KEY_RATE_COUNT="rate_count";
    public static final String KEY_RATE_PERCENTAGE="rate_persenge";
    public static final String KEY_COMMENTS="comments";


    @SerializedName(KEY_RATE)
    private Float rate;
    @SerializedName(KEY_RATE_COUNT)
    private Integer rateCount;
    @SerializedName(KEY_RATE_PERCENTAGE)
    private ArrayList<HashMap<String, Integer>> ratePercentage;
    @SerializedName(KEY_COMMENTS)
    private ArrayList<Comment> comments;


    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Integer getRateCount() {
        return rateCount;
    }

    public void setRateCount(Integer rateCount) {
        this.rateCount = rateCount;
    }

    public ArrayList<HashMap<String, Integer>> getRatePercentage() {
        return ratePercentage;
    }

    public void setRatePercentage(ArrayList<HashMap<String, Integer>>ratePercentage) {
        this.ratePercentage = ratePercentage;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
