package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by bassiouny on 02/08/17.
 */

public class ChatResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private ArrayList<ChatModel> chatModels;

    public ArrayList<ChatModel> getChatModels() {
        return chatModels;
    }

    public void setChatModels(ArrayList<ChatModel> chatModels) {
        this.chatModels = chatModels;
    }
}
