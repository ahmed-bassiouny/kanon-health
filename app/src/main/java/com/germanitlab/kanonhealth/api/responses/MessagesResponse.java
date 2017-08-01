package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Message;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bassiouny on 01/08/17.
 */

public class MessagesResponse extends ParentResponse {
    @SerializedName(KEY_DATA)
    private ArrayList<Message> data;

    public ArrayList<Message> getData() {
        return data;
    }
    public void setData(ArrayList<Message> data) {
        this.data = data;
    }
}
