package com.germanitlab.kanonhealth.async;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by eslam on 1/14/17.
 */

public class SocketCall {

    private Context activity;
    private ApiResponse apiResponse;

    public SocketCall(Context activity) {

        // AppController.getInstance().getSocket().connect();
        this.activity = activity;
    }

    public SocketCall(Context activity, ApiResponse apiResponse) {

        // AppController.getInstance().getSocket().connect();
        this.activity = activity;
        this.apiResponse = apiResponse;
    }

    public void getDoctors(String user_id, String password) {

        JSONObject request = null;
        try {

            request = new JSONObject();
            request.put("user_id", user_id);
            request.put("password", password);
            request.put("is_array", 1);

        } catch (JSONException e) {

            Log.d("EX ", e.getLocalizedMessage());
        }
    }

    public void joinUser(int userID) {

        JSONObject request = null;
        try {

            request = new JSONObject();
            request.put("id", userID);

        } catch (JSONException e) {

            Log.d("EX ", e.getLocalizedMessage());
        }

    }


    //============================Open chat message :
    public void chatWithStatus(String emitKey, int userID) {

        JSONObject request = null;
        try {

            request = new JSONObject();
            request.put("user_id", userID);

        } catch (JSONException e) {

            Log.d("EX ", e.getLocalizedMessage());
        }

    }


    ///-- Featch all message
    public void fetchMessage(String to_id, String page_num) {

        JSONObject request = null;
        try {

            request = new JSONObject();
            request.put("to_id", to_id);
            request.put("page_num", page_num);
            request.put("is_array", 1);

        } catch (JSONException e) {

            Log.d("EX ", e.getLocalizedMessage());
        }


    }


}
