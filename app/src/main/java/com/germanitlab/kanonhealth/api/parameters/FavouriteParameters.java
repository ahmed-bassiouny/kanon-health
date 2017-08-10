package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 10/08/17.
 */

public class FavouriteParameters extends ParentParameters {

    public final static String PARAMETER_FROM_ID = "user_id";
    public final static String PARAMETER_TO_ID = "to_id";
    public final static String PARAMETER_TO_TYPE = "to_type";
    public final static String PARAMETER_TYPE = "type";

    // user use application
    @SerializedName(PARAMETER_FROM_ID)
    private String userId;
    // doctor or clinic
    @SerializedName(PARAMETER_TO_ID)
    private String toId;
    // type of user i will add him to my favourite , doctor or clinic and i will use constaint UserInfo
    @SerializedName(PARAMETER_TO_TYPE)
    private int toType;
    // type of operation true => add to my favourite , false => remove from my favourite
    @SerializedName(PARAMETER_TYPE)
    private boolean type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public int getToType() {
        return toType;
    }

    public void setToType(int toType) {
        this.toType = toType;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
