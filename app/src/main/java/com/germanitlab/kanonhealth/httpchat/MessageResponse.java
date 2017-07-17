package com.germanitlab.kanonhealth.httpchat;

import com.germanitlab.kanonhealth.models.messages.Message;

import java.util.List;

/**
 * Created by bassiouny on 28/06/17.
 */

public class MessageResponse {

    private Integer status;

    private List<Message> messages;

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }

    private Message msg;

    public List<Message> getMessage() {
        return messages;
    }

    public void setMessage(List<Message> message) {
        this.messages = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
