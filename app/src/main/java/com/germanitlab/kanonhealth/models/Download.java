package com.germanitlab.kanonhealth.models;

/**
 * Created by Milad Metias on 7/9/17.
 */

public class Download {

    private String url;
    private Runnable callback;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Runnable getCallback() {
        return callback;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }
}
