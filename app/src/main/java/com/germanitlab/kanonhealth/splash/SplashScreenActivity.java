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
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.initialProfile.ProfileDetails;
import com.germanitlab.kanonhealth.intro.SignupActivity;
import com.google.gson.Gson;


public class SplashScreenActivity extends AppCompatActivity {


    private long timeOut = 1000;
    private boolean isLogin;
    private String storedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash_screen);
        try {
            storedUser = PrefHelper.get(SplashScreenActivity.this , PrefHelper.KEY_USER_KEY , "");
            isLogin = PrefHelper.get(SplashScreenActivity.this , PrefHelper.KEY_IS_LOGIN , false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    // to check passcode
                    if (isLogin && !storedUser.equals("") && PrefHelper.get(SplashScreenActivity.this , PrefHelper.KEY_PASSCODE, "").length() == 6) {
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
                    else if (isLogin && PrefHelper.get(SplashScreenActivity.this , PrefHelper.KEY_PASSCODE, "").length() != 6) {

                        Intent intent = new Intent(SplashScreenActivity.this, PasscodeActivty.class);
                        intent.putExtra("checkPassword", false);
                        intent.putExtra("finish", false);
                        intent.putExtra("has_back", false);
                        startActivity(intent);
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, SignupActivity.class));
                        PrefHelper.put(SplashScreenActivity.this , PrefHelper.KEY_IS_OLD , false);
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserInfo userInfo= ApiHelper.getUserInfo(SplashScreenActivity.this,String.valueOf(PrefHelper.get(SplashScreenActivity.this , PrefHelper.KEY_USER_ID ,-1)));

                    if(userInfo !=null){
                        Gson gson = new Gson();
                        PrefHelper.put(SplashScreenActivity.this , PrefHelper.KEY_USER_KEY , gson.toJson(userInfo));
                        if(userInfo.getUserType()==UserInfo.DOCTOR) {
                            PrefHelper.put(SplashScreenActivity.this , PrefHelper.KEY_IS_DOCTOR , true);
                        }else{
                            PrefHelper.put(SplashScreenActivity.this , PrefHelper.KEY_IS_DOCTOR , false);
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
