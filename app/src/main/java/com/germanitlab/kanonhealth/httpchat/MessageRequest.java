package com.germanitlab.kanonhealth.httpchat;

/**
 * Created by bassiouny on 28/06/17.
 */

public class MessageRequest {

    private int user_id;
    private int to;

    public MessageRequest(int user_id, int to) {
        this.user_id = user_id;
        this.to = to;
    }

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
}
