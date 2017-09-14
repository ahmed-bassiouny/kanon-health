package com.germanitlab.kanonhealth.httpchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.main.MainActivity;

public class HttpChatActivity extends AppCompatActivity {

    int doctorID;
    HttpChatFragment fr;
    UserInfo userInfo;
    String userType;
    boolean handleBackButton=false;
    int type=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_chat);
        doctorID = getIntent().getIntExtra("doctorID", 0);
        type=getIntent().getIntExtra("type",0);


        userInfo = new UserInfo();
        userInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
        if (userInfo == null) {
            // this case get from notification
            userType = getIntent().getStringExtra("userType");
            handleBackButton=true;
            if (userType.equals("Clinic"))
                getClinicInfo();
            else
                getUserInfo();
        } else {
            if (doctorID == 0 || type==0) {
                finish();
            }
            showFragment();
        }
    }

    private void getUserInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                userInfo = ApiHelper.getUserInfo(HttpChatActivity.this, String.valueOf(doctorID));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (doctorID == 0) {
                            finish();
                        }
                        showFragment();
                    }
                });

            }
        }).start();
    }

    private void getClinicInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                userInfo = ApiHelper.postGetClinic(doctorID, HttpChatActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (doctorID == 0) {
                            finish();
                        }
                    }
                });

            }
        }).start();
    }

    private void showFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("doctorID", doctorID);
        bundle.putSerializable("userInfo", userInfo);
        bundle.putInt("type",type);
        fr = new HttpChatFragment();
        fr.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main, fr).commit();
    }

    @Override
    public void onBackPressed() {
        if(handleBackButton){
            startActivity(new Intent(HttpChatActivity.this, MainActivity.class));
        }else {
            super.onBackPressed();
        }
    }
}
