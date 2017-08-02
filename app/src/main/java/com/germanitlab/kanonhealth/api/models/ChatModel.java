package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 02/08/17.
 */

public class ChatModel {

    public final static String USERMODEL="user";
    public final static String LASTMESSAGEMODEL="last_msg";
    public final static String USEROPTION="is";

    @SerializedName(USERMODEL)
    private ChatUserInfo chatUserInfo;
    @SerializedName(LASTMESSAGEMODEL)
    private LastMessageInfo lastMessageInfo;
    @SerializedName(USEROPTION)
    private UserOption userOption;

    public ChatUserInfo getChatUserInfo() {
        return chatUserInfo;
    }

    public void setChatUserInfo(ChatUserInfo chatUserInfo) {
        this.chatUserInfo = chatUserInfo;
    }

    public LastMessageInfo getLastMessageInfo() {
        return lastMessageInfo;
    }

    public void setLastMessageInfo(LastMessageInfo lastMessageInfo) {
        this.lastMessageInfo = lastMessageInfo;
    }

    public UserOption getUserOption() {
        return userOption;
    }

    public void setUserOption(UserOption userOption) {
        this.userOption = userOption;
    }
}
