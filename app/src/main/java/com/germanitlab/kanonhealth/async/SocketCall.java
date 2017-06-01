package com.germanitlab.kanonhealth.async;

import android.content.Context;
import android.util.Log;

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

        Log.d("DoctorsListRequest", request.toString());
        AppController.getInstance().getSocket().emit("DoctorsList", request);
        AppController.getInstance().getSocket().on("UpdateUsersList", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Log.d("Responce : ", args[0].toString());
//                JSONObject response = (JSONObject) args[0];
//
//                String mJsonString = response.toString();
//                Gson gson = new Gson();
//
//                JsonParser parser = new JsonParser();
//                JsonElement mJson = parser.parse(mJsonString);
//                UserRegisterResponse object = gson.fromJson(mJson, UserRegisterResponse.class);
//
                apiResponse.onSuccess(args[0].toString());
            }
        });
    }

    public void joinUser(int userID) {

        JSONObject request = null;
        try {

            request = new JSONObject();
            request.put("id", userID);

        } catch (JSONException e) {

            Log.d("EX ", e.getLocalizedMessage());
        }

        Log.d("Join user", request.toString());
        AppController.getInstance().getSocket().emit("JoinUser", request);
        AppController.getInstance().getSocket().on("UpdateOnlineusers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Log.d("Join user Responce : ", args[0].toString());
//                JSONObject response = (JSONObject) args[0];
//
//                String mJsonString = response.toString();
//                Gson gson = new Gson();
//
//                JsonParser parser = new JsonParser();
//                JsonElement mJson = parser.parse(mJsonString);
//                UserRegisterResponse object = gson.fromJson(mJson, UserRegisterResponse.class);
//
                apiResponse.onSuccess(args[0].toString());
            }
        });


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

        Log.d(emitKey, request.toString());
        AppController.getInstance().getSocket().emit(emitKey, request);
        AppController.getInstance().getSocket().on(emitKey + "Return", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Log.d("chatWithStatusResponse", args[0].toString());
//                JSONObject response = (JSONObject) args[0];
//
//                String mJsonString = response.toString();
//                Gson gson = new Gson();
//
//                JsonParser parser = new JsonParser();
//                JsonElement mJson = parser.parse(mJsonString);
//                UserRegisterResponse object = gson.fromJson(mJson, UserRegisterResponse.class);
//
                //  apiResponse.onSuccess(args[0].toString());
            }
        });
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

        Log.d("Request DoctorsListRequest", request.toString());
        AppController.getInstance().getSocket().emit("getMessages", request);
        AppController.getInstance().getSocket().on("FetchMsgs", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Log.d("Responce : ", args[0].toString());
//                JSONObject response = (JSONObject) args[0];
//
//                String mJsonString = response.toString();
//                Gson gson = new Gson();
//
//                JsonParser parser = new JsonParser();
//                JsonElement mJson = parser.parse(mJsonString);
//                UserRegisterResponse object = gson.fromJson(mJson, UserRegisterResponse.class);
//
                apiResponse.onSuccess(args[0].toString());
            }
        });

        Log.d(" After Socket", " " + AppController.getInstance().getSocket().connected());

    }

    public void setNewMessage(String to_id, String msg, String type) {

        JSONObject request = null;
        try {

            request = new JSONObject();
            request.put("to_id", to_id);
            request.put("msg", msg);
            request.put("type", type);

        } catch (JSONException e) {

            Log.d("EX ", e.getLocalizedMessage());
        }

        AppController.getInstance().getSocket().emit("ChatMessage", request);
        Log.d("Before request", " " + AppController.getInstance().getSocket().connected());
        AppController.getInstance().getSocket().on("ChatMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Log.d("New Message response : ", args[0].toString());
//                JSONObject response = (JSONObject) args[0];
//
//                String mJsonString = response.toString();
//                Gson gson = new Gson();
//
//                JsonParser parser = new JsonParser();
//                JsonElement mJson = parser.parse(mJsonString);
//                UserRegisterResponse object = gson.fromJson(mJson, UserRegisterResponse.class);
//
                apiResponse.onSuccess(args[0].toString());
            }
        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                apiResponse.onFailed(args[0].toString());
            }
        });

        Log.d(" After Socket", " " + AppController.getInstance().getSocket().connected());

    }

}
