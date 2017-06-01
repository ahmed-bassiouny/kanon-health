package com.germanitlab.kanonhealth.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.germanitlab.kanonhealth.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreRequest extends AppCompatActivity {
    String doctorJson;

    public static Activity preRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_request);
        preRequest=this;

        ButterKnife.bind(this);
        Intent intent = getIntent();
        doctorJson = intent.getStringExtra("doctor_data");

    }
    @OnClick(R.id.image)
    public void toPayment(View view)
    {
        Intent intent = new Intent(this , PaymentActivity.class);
        intent.putExtra("doctor_data" , doctorJson);
        startActivity(intent);
    }
}
