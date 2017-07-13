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
import com.germanitlab.kanonhealth.helpers.Constants;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.fabric.sdk.android.Fabric;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by eslam on 1/10/17.
 */

public class AppController extends Application {


    private static AppController mInstance;
    //   private UserRegisterResponse clientInfo;
    PrefManager prefManager;
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

            prefManager = new PrefManager(this);
            Fabric.with(this, new Crashlytics());
            StrictMode.VmPolicy.Builder sbuilder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(sbuilder.build());

            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
            appComponent.inject(this);
            mInstance = this;


/*            if (clientInfo == null) {
                if (CacheJson.fileExists(mInstance, Constants.REGISER_RESPONSE)) {

                    try {

                        Log.d("Client Info : ", "Read client info form cach ");
                        clientInfo = (UserRegisterResponse) CacheJson.readObject(mInstance, Constants.REGISER_RESPONSE);

                    } catch (Exception e) {

                        clientInfo = null;
                        Log.e("Ex", " " + e.getLocalizedMessage());
                    }
                }
            }*/



    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }



  /*  public UserRegisterResponse getClientInfo() {

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
*/
   /* public void setClientInfo(UserRegisterResponse clientInfo) {
        this.clientInfo = clientInfo;
    }*/

}
