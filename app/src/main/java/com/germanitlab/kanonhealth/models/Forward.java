package com.germanitlab.kanonhealth.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Geram IT Lab on 04/05/2017.
 */

public class Forward implements Serializable {
    private String user_id;
    private String to;
    private String msg_id;
    private String password;

    public Forward(String user_id, String password, String msg_id, String to_id) {
        this.user_id = user_id;
        this.password = password;
        this.msg_id = msg_id;
        this.to = to_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getTo_id() {
        return to;
    }

    public void setTo_id(String to_id) {
        this.to = to_id;
    }
}
