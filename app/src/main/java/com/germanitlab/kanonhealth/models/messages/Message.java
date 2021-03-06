package com.germanitlab.kanonhealth.models.messages;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by sreejeshpillai on 10/05/15.
 */
@DatabaseTable(tableName = "message")
public class Message implements Serializable {

    @DatabaseField
    private int user_id;
    @DatabaseField
    private int to;
    @DatabaseField
    public static final int TYPE_MESSAGE = 0;
    @DatabaseField
    private String sent_at;
    @DatabaseField
    private int to_id;
    @DatabaseField
    private String type;
    @DatabaseField
    private int from_id;
    @DatabaseField
    private String msg;
    @DatabaseField
    private boolean mine;
    @DatabaseField
    private boolean loaded, loading;
    @DatabaseField
    private int status;
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private int seen;
    @DatabaseField
    private int privacy;
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private Bitmap locationBitmap;
    @DatabaseField
    private String category;
    @DatabaseField
    private String doctor;
    @DatabaseField
    private String diagnose;
    @DatabaseField
    private String report;
    @DatabaseField
    private String comment;
    @DatabaseField
    private String date;
    @DatabaseField
    private String imageText;
    @DatabaseField
    private int is_url;
    @DatabaseField
    private String is_document;
    @DatabaseField
    private int is_forward;
    @DatabaseField
    private int is_delivered;
    @DatabaseField
    private boolean is_send=true;



    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
    public int getIs_url() {
        return is_url;
    }

    public void setIs_url(int is_url) {
        this.is_url = is_url;
    }


    public String getIs_document() {
        return is_document;
    }

    public void setIs_document(String is_document) {
        this.is_document = is_document;
    }

    public int getIs_forward() {
        return is_forward;
    }

    public void setIs_forward(int is_forward) {
        this.is_forward = is_forward;
    }

    public int getIs_delivered() {
        return is_delivered;
    }

    public void setIs_delivered(int is_delivered) {
        this.is_delivered = is_delivered;
    }



    public Message() {
    }

    public int getId() {
        return id;
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

    public Bitmap getLocationBitmap() {
        return locationBitmap;
    }

    public void setLocationBitmap(Bitmap locationBitmap) {
        this.locationBitmap = locationBitmap;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public int get_Id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public static int getTypeMessage() {
        return TYPE_MESSAGE;
    }

    public String getSent_at() {
        return sent_at;
    }

    public void setSent_at(String sent_at) {
        this.sent_at = sent_at;
    }

    public int getTo_id() {
        return to_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }


    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }


    public String getType() {
        return type;
    }


    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sent_at='" + sent_at + '\'' +
                ", to_id=" + to_id +
                ", type='" + type + '\'' +
                ", from_id=" + from_id +
                ", msg='" + msg + '\'' +
                ", mine=" + mine +
                ", loaded=" + loaded +
                ", loading=" + loading +
                ", status=" + status +
                ", id=" + id +
                ", seen=" + seen +
                ", locationBitmap=" + locationBitmap +
                '}';
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public boolean getIs_send() {
        return is_send;
    }

    public void setIs_send(boolean is_send) {
        this.is_send = is_send;
    }
}