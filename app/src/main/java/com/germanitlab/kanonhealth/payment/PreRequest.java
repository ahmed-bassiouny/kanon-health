package com.germanitlab.kanonhealth.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.helpers.PrefHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreRequest extends AppCompatActivity {
    String doctorJson;

    public static Activity preRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_pre_request);
            preRequest = this;

            ButterKnife.bind(this);
            Intent intent = getIntent();
            doctorJson = intent.getStringExtra("doctor_data");
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.image)
    public void toPayment(View view) {
        try {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("doctor_data", doctorJson);
            PrefHelper.put(getBaseContext() , PrefHelper.KEY_USER_INTENT , doctorJson);
            startActivity(intent);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }

    }
}
