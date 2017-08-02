package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class UserInfoResponse  extends ParentResponse{
    @SerializedName(KEY_DATA)
    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
