package com.germanitlab.kanonhealth.httpchat;

/**
 * Created by bassiouny on 18/07/17.
 */

public class MessageRequestSeen {
    private int from_id;
    private int to_id;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public MessageRequestSeen(int from_id, String password, int to_id){
        this.from_id = from_id;
        this.to_id = to_id;
        this.password=password;
    }
    public MessageRequestSeen(int from_id, int to_id){
        this.from_id = from_id;
        this.to_id = to_id;
    }
    public int getUser_id() {
        return from_id;
    }

    public void setUser_id(int user_id) {
        this.from_id = user_id;

    }

    public int getTo() {
        return to_id;
    }

    public void setTo(int to) {
        this.to_id = to;
    }

}
