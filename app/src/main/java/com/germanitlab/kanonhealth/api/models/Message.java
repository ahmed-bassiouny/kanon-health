package com.germanitlab.kanonhealth.api.models;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by bassiouny on 01/08/17.
 */
@DatabaseTable(tableName = "message")
public class Message extends ParentModel {

    //region key name
    public static final String KEY_FROMID="from_id";
    public static final String KEY_TOID="to_id";
    public static final String KEY_MESSAGE = "massage";
    public static final String KEY_STATUS = "status";
    public static final String KEY_TYPE = "type";
    public static final String KEY_MEDIA = "image";
    public static final String KEY_IS_FORWARD = "is_forward";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_ID = "massage_id";

    // endregion

    //region attribute
    // id of message
    @SerializedName(KEY_ID)
    @DatabaseField(id = true)
    private Integer messageID;
    // body of messgae or url of media
    @SerializedName(KEY_MESSAGE)
    @DatabaseField
    private String message;
    // status of message => 1 delivered to server  , 2 delivered to user , 3 seen by user
    @SerializedName(KEY_STATUS)
    @DatabaseField
    private Integer status;

    // url of image or video or audio , etc
    @SerializedName(KEY_MEDIA)
    @DatabaseField
    private String media;
    // type of messgae image or video or audio , etc
    @SerializedName(KEY_TYPE)
    @DatabaseField
    private String type;
    //
    @SerializedName(KEY_IS_FORWARD)
    @DatabaseField
    private Integer forward;
    // date time of message (UTC)
    @SerializedName(KEY_CREATED_AT)
    @DatabaseField
    private String dateTime;

    @SerializedName(KEY_FROMID)
    @DatabaseField
    private Integer fromID;
    @SerializedName(KEY_TOID)
    @DatabaseField
    private Integer toID;

    //endregion

    //region constraints message type
    public final static String MESSAGE_TYPE_TEXT="text";
    public final static String MESSAGE_TYPE_IMAGE="image";
    public final static String MESSAGE_TYPE_VIDEO="video";
    public final static String MESSAGE_TYPE_AUDIO="audio";
    public final static String MESSAGE_TYPE_LOCATION="location";
    public final static String MESSAGE_TYPE_UNDEFINED="undefined";

    public final static int MESSAGE_TYPE_TEXT_NUM=1;
    public final static int MESSAGE_TYPE_IMAGE_NUM=2;
    public final static int MESSAGE_TYPE_VIDEO_NUM=3;
    public final static int MESSAGE_TYPE_AUDIO_NUM=4;
    public final static int MESSAGE_TYPE_LOCATION_NUM=5;
    public final static int MESSAGE_TYPE_UNDEFINED_NUM=6;

    public final static int SEEN=3;
    public final static int DELIVERED=2;
    public final static int SENT=1;
    //endregion

    // region Setter and Getter

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        if(status==null || status==0)
            status= SENT;
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getType() {
        if(type==null || type.isEmpty())
            type=MESSAGE_TYPE_TEXT;
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getForward() {
        if(forward==null)
        {
            forward = 0;
        }
        return forward;
    }

    public void setForward(Integer forward) {
        this.forward = forward;
    }

    public String getDateTime() {
        String formattedDate;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dateTime);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            formattedDate=dateTime;
        }
        return formattedDate;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getMessageID() {
        return messageID;
    }

    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }

    public Integer getFromID() {
        return fromID;
    }

    public void setFromID(Integer fromID) {
        this.fromID = fromID;
    }

    public Integer getToID() {
        return toID;
    }

    public void setToID(Integer toID) {
        this.toID = toID;
    }


//endregion
}
