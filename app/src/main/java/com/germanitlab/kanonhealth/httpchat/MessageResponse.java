package com.germanitlab.kanonhealth.httpchat;

import com.germanitlab.kanonhealth.models.messages.Message;

import java.util.List;

/**
 * Created by bassiouny on 28/06/17.
 */

public class MessageResponse {

    private int status;

    private List<Message> messages;

    public List<Message> getMessage() {
        return messages;
    }

    public void setMessage(List<Message> message) {
        this.messages = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
