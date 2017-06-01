package com.germanitlab.kanonhealth.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.germanitlab.kanonhealth.async.SocketCall;
import com.germanitlab.kanonhealth.helpers.CacheJson;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.models.user.UserRegisterResponse;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by eslam on 1/10/17.
 */

public class AppController extends Application {

    private Socket mSocket;
    private SocketCall socketCall;

    private static AppController mInstance;
    private UserRegisterResponse clientInfo;

    protected AppComponent appComponent;


    public static AppController get(Context context) {
        return (AppController) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
        mInstance = this;

        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
            mSocket.connect();
            mSocket.on(mSocket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    try {
                        JSONObject request = null;
                        int i = AppController.getInstance().getClientInfo().getUser_id();
                        String s = String.valueOf(AppController.getInstance().getClientInfo().getUser_id());
                        request = new JSONObject();
                        request.put("id", AppController.getInstance().getClientInfo().getUser_id());
                        Log.d("Join user", request.toString());
                        AppController.getInstance().getSocket().emit("JoinUser", request);
                        mSocket.emit("JoinUser", AppController.getInstance().getClientInfo().getUser_id());


                    } catch (Exception e) {

                        Log.d("EX ", e.toString());
                    }


                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });
            Log.d("Socket", " " + mSocket.connected());
        } catch (URISyntaxException e) {

            Log.d("Ex", e.getLocalizedMessage());
        }

        if (clientInfo == null) {
            if (CacheJson.fileExists(mInstance, Constants.REGISER_RESPONSE)) {

                try {

                    Log.d("Client Info : ", "Read client info form cach ");
                    clientInfo = (UserRegisterResponse) CacheJson.readObject(mInstance, Constants.REGISER_RESPONSE);

                } catch (Exception e) {

                    clientInfo = null;
                    Log.e("Ex", " " + e.getLocalizedMessage());
                }
            }
        }


    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public Socket getSocket() {
        if (mSocket.connected()) {
            return mSocket;
        } else {
            mSocket.connect();
            try {
                socketCall.joinUser(AppController.getInstance().getClientInfo().getUser_id());
            } catch (Exception e) {

            }
        }
        Log.d("Soket ", " " + mSocket.connected());
        return mSocket;
    }

    public UserRegisterResponse getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(UserRegisterResponse clientInfo) {
        this.clientInfo = clientInfo;
    }

}
