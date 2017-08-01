package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Message;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bassiouny on 01/08/17.
 */

public class MessageSendResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private Message data;

    public Message getData() {
        return data;
    }
    public void setData(Message data) {
        this.data = data;
    }
}
