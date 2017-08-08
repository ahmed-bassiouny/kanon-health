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
    private HashMap<String, Integer> ratePercentage;
    @SerializedName(KEY_COMMENTS)
    private ArrayList<Comment> comments;

}
