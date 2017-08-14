package com.germanitlab.kanonhealth.httpchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.germanitlab.kanonhealth.R;

public class HttpChatActivity extends AppCompatActivity {

    int doctorID;
    HttpChatFragment fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_chat);
        doctorID = getIntent().getIntExtra("doctorID", 0);

        if (doctorID == 0) {
            finish();
        }

        Bundle bundle = new Bundle();
        bundle.putInt("doctorID", doctorID);

        fr = new HttpChatFragment();
        fr.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main, fr).commit();

    }

}
