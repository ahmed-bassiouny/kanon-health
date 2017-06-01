package com.germanitlab.kanonhealth.interfaces;

/**
 * Created by eslam on 11/17/16.
 */
public interface ApiResponse {

    public void onSuccess(Object response);

    public void onFailed(String error);


}
