package com.germanitlab.kanonhealth.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Geram IT Lab on 04/05/2017.
 */

public class Forward implements Serializable {
    private String user_id;
    private String password;
    private List<Integer> msg_id;
    private List<Integer> to;

    public Forward(String user_id, String password, List<Integer> msg_id, List<Integer> to_id) {
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

    public List<Integer> getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(List<Integer> msg_id) {
        this.msg_id = msg_id;
    }

    public List<Integer> getTo_id() {
        return to;
    }

    public void setTo_id(List<Integer> to_id) {
        this.to = to_id;
    }
}
