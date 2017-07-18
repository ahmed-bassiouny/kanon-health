package com.germanitlab.kanonhealth.httpchat;

/**
 * Created by bassiouny on 28/06/17.
 */

public class MessageRequest {

    private int user_id;
    private int id;
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
        this.id = to;
        this.to=to;
    }

    public MessageRequest(int user_id, String password,int to){
        this.user_id = user_id;
        this.id = to;
        this.to=to;
        this.password=password;
    }
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;

    }

    public int getTo() {
        return id;
    }

    public void setTo(int to) {
        this.id = to;
    }

}
