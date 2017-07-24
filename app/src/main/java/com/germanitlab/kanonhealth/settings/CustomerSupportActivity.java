package com.germanitlab.kanonhealth.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.httpchat.HttpChatFragment;

public class CustomerSupportActivity extends AppCompatActivity {

    public final static int supportID=1;
    HttpChatFragment fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        Bundle bundle = new Bundle();
        bundle.putInt("doctorID", supportID);

        fr = new HttpChatFragment();
        fr.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main, fr).commit();

    }
}
