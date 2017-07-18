package com.germanitlab.kanonhealth.httpchat;

/**
 * Created by bassiouny on 28/06/17.
 */

public class MessageRequest {

    private int user_id;
    private int to;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public MessageRequest(int user_id, int to) {
        this.user_id = user_id;
        this.to=to;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int from_id) {
        this.user_id = from_id;

    }

    public int getTo() {
        return to;
    }

    public void setTo(int id) {
        this.to = id;
    }

}
