package com.germanitlab.kanonhealth.httpchat;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.germanitlab.kanonhealth.R;

public class HttpChatActivity extends AppCompatActivity {

    String doctorName = "";
    String doctorUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_chat);
        int doctorID = getIntent().getIntExtra("doctorID",0);
        String doctorName = getIntent().getStringExtra("doctorName");
        String doctorUrl = getIntent().getStringExtra("doctorUrl");
        if (doctorID==0)
            finish();

        Bundle bundle = new Bundle();
        bundle.putInt("doctorID", doctorID);
        bundle.putString("doctorName",doctorName);
        bundle.putString("doctorUrl",doctorUrl);

        HttpChatFragment fr = new HttpChatFragment();
        fr.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main, fr).commit();

    }
}
