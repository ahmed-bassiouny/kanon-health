package com.germanitlab.kanonhealth.models;

/**
 * Created by halima on 22/06/17.
 */

/*
"documents":[
{
"id": 82,
"msg": "Hi",
"type": "text"
},
{
"id": 84,
"msg": "Hi",
"type": "text"
}
],
 */

public class Document {
    int id;
    String msg;
    String type;

    public void setId(int id) {
        this.id = id;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public String getType() {
        return type;
    }
}
