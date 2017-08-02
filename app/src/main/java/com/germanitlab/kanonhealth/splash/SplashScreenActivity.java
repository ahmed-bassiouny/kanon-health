package com.germanitlab.kanonhealth.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.PasscodeActivty;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.initialProfile.ProfileDetails;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.intro.SignupActivity;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.models.user.UserRegisterResponse;
import com.google.gson.Gson;


public class SplashScreenActivity extends AppCompatActivity {


    private long timeOut = 1000;
    private String storedDoctor;
    private boolean isLogin;
    private String storedUser;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash_screen);
        try {
            prefManager = new PrefManager(this);
            storedUser = prefManager.getData(prefManager.USER_KEY);
            isLogin = prefManager.isLogin();

            Log.e("User1 =", storedUser.equals("") ? storedUser : "Empty User1");
            Log.e("isLogin", String.valueOf(isLogin));


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    // to check passcode
                    if (isLogin && !storedUser.equals("") && prefManager.getData(PrefManager.PASSCODE).length() == 6) {
                        if (Helper.isNetworkAvailable(SplashScreenActivity.this))
                            loadData();
                        else {
                            Intent intent = new Intent(SplashScreenActivity.this, PasscodeActivty.class);
                            intent.putExtra("checkPassword", true);
                            intent.putExtra("finish", false);
                            intent.putExtra("has_back", false);
                            startActivity(intent);
                            finish();
                        }

                    } // registered but not filled my data
                    else if (isLogin && storedUser.equals("")) {

                        startActivity(new Intent(SplashScreenActivity.this, ProfileDetails.class));
                        finish();
                    }
                    // all data saved but no passcode set
                    else if (isLogin && prefManager.getData(PrefManager.PASSCODE).length() != 6) {

                        Intent intent = new Intent(SplashScreenActivity.this, PasscodeActivty.class);
                        intent.putExtra("checkPassword", false);
                        intent.putExtra("finish", false);
                        intent.putExtra("has_back", false);
                        startActivity(intent);
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, SignupActivity.class));
                        finish();
                    }
                }
            }, timeOut);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }

    }



    private void loadData() {
        try {
            UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
            userRegisterResponse.setUser_id(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));
            userRegisterResponse.setPassword(prefManager.getData(PrefManager.USER_PASSWORD));
            if (!Helper.isNetworkAvailable(this)) {
                Toast.makeText(this, R.string.error_connection, Toast.LENGTH_SHORT).show();
                return;
            }
            /*new HttpCall(this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    if (response != null) {
                        Gson gson = new Gson();
                        new PrefManager(SplashScreenActivity.this).put(PrefManager.USER_KEY, gson.toJson(response));
                        UserInfoResponse userInfoResponse = (UserInfoResponse) response;
                        new PrefManager(SplashScreenActivity.this).put(PrefManager.IS_DOCTOR, userInfoResponse.getUser().getIsDoc() == 1);
                        Intent intent = new Intent(SplashScreenActivity.this, PasscodeActivty.class);
                        intent.putExtra("checkPassword", true);
                        intent.putExtra("finish", false);
                        intent.putExtra("has_back", false);
                        startActivity(intent);
                        finish();

                    } else {
                        onFailed("response is null");

                    }
                }

                @Override
                public void onFailed(String error) {
                    Intent intent = new Intent(SplashScreenActivity.this, PasscodeActivty.class);
                    intent.putExtra("checkPassword", true);
                    intent.putExtra("finish", false);
                    intent.putExtra("has_back", false);
                    startActivity(intent);
                    Log.e("Splash", error);
                    finish();
                }
            }).getProfile(userRegisterResponse);*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserInfo userInfo= ApiHelper.getUserInfo(SplashScreenActivity.this,Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));

                    if(userInfo !=null){
                        Gson gson = new Gson();
                        new PrefManager(SplashScreenActivity.this).put(PrefManager.USER_KEY, gson.toJson(userInfo));
                        if(userInfo.getUserType()==UserInfo.DOCTOR) {
                            new PrefManager(SplashScreenActivity.this).put(PrefManager.IS_DOCTOR, true);
                        }else{
                            new PrefManager(SplashScreenActivity.this).put(PrefManager.IS_DOCTOR, false);
                        }
                    }
                    Intent intent = new Intent(SplashScreenActivity.this, PasscodeActivty.class);
                    intent.putExtra("checkPassword", true);
                    intent.putExtra("finish", false);
                    intent.putExtra("has_back", false);
                    startActivity(intent);
                    finish();
                }
            }).start();



        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.sorry_missing_data_please_contact_support), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

}
