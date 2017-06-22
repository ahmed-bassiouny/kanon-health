package com.germanitlab.kanonhealth.application;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.SocketCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.CacheJson;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.models.user.UserRegisterResponse;
import com.google.gson.Gson;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import io.fabric.sdk.android.Fabric;

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
        try {
            Fabric.with(this, new Crashlytics());
            StrictMode.VmPolicy.Builder sbuilder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(sbuilder.build());

            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
            appComponent.inject(this);
            mInstance = this;

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

            //Picasso configration
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
            Picasso built = builder.build();
            built.setIndicatorsEnabled(true);
            built.setLoggingEnabled(true);
            Picasso.setSingletonInstance(built);
        } catch (URISyntaxException e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.d("Ex", e.getLocalizedMessage());
        }

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public Socket getSocket() {
        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

        return mSocket;
    }

    public UserRegisterResponse getClientInfo() {

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

        if (clientInfo == null) {
            PrefManager prefManager = new PrefManager(getApplicationContext());
            if (prefManager.isLogin()) {
                Gson gson = new Gson();
                UserInfoResponse userInfoResponse = gson.fromJson(prefManager.getData(PrefManager.USER_KEY), UserInfoResponse.class);
                clientInfo = new UserRegisterResponse();
                clientInfo.setUser_id(userInfoResponse.getUser().get_Id());
                clientInfo.setPassword(userInfoResponse.getUser().getPassword());
                clientInfo.setIs_exist(true);
                clientInfo.setSucess(true);
            }
        }
        return clientInfo;
    }

    public void setClientInfo(UserRegisterResponse clientInfo) {
        this.clientInfo = clientInfo;
    }

}
