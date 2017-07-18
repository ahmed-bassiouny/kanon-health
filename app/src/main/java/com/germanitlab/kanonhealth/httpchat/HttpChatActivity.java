package com.germanitlab.kanonhealth.httpchat;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.models.user.User;

public class HttpChatActivity extends AppCompatActivity {

    String doctorName = "";
    String doctorUrl = "";
    int doctorID;
    boolean iamDoctor;
    User doctor;
    PrefManager prefManager;
    HttpChatFragment fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_chat);
        doctorID = getIntent().getIntExtra("doctorID", 0);
        doctorName = getIntent().getStringExtra("doctorName");
        doctorUrl = getIntent().getStringExtra("doctorUrl");
        iamDoctor = getIntent().getBooleanExtra("iamDoctor", false);

        if (doctorID == 0)
            finish();

        Bundle bundle = new Bundle();
        bundle.putInt("doctorID", doctorID);
        bundle.putString("doctorName", doctorName);
        bundle.putString("doctorUrl", doctorUrl);
        bundle.putBoolean("iamDoctor", iamDoctor);

        fr = new HttpChatFragment();
        fr.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main, fr).commit();

    }

}
