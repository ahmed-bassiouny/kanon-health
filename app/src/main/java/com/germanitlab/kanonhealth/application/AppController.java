package com.germanitlab.kanonhealth.application;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import io.fabric.sdk.android.Fabric;

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

        // DON'T COPY THIS CODE TO YOUR PROJECT! This is just example of ALL options using.
// See the sample project how to use ImageLoader correctly.
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(5) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

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
