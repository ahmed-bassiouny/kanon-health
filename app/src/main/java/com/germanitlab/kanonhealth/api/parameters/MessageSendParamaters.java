package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 01/08/17.
 */

public class MessageSendParamaters extends ParentParameters {

    public static final String PARAMATER_FROM_ID="from_id";
    public static final String PARAMATER_TO_ID="to_id";
    public static final String PARAMATER_MESSAGE="massage";
    public static final String PARAMATER_TYPE="type";
    public static final String PARAMATER_IS_FORWARD="is_forward";
    public static final String PARAMATER_MEDIA="image";

    // id user send message
    @SerializedName(PARAMATER_FROM_ID)
    private int fromID;
    // id user receive message
    @SerializedName(PARAMATER_TO_ID)
    private int toID;
    // body of messgae or url of media
    @SerializedName(PARAMATER_MESSAGE)
    private String message;
    // type message text or video etc
    @SerializedName(PARAMATER_TYPE)
    private String typeMessage;
    //
    @SerializedName(PARAMATER_IS_FORWARD)
    private int isForward;
    // media send to server uri (from mobile) video or image etc
    @SerializedName(PARAMATER_MEDIA)
    private String media;

    public int getFromID() {
        return fromID;
    }

    public void setFromID(int fromID) {
        this.fromID = fromID;
    }

    public int getToID() {
        return toID;
    }

    public void setToID(int toID) {
        this.toID = toID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public int getIsForward() {
        return isForward;
    }

    public void setIsForward(int isForward) {
        this.isForward = isForward;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

}
