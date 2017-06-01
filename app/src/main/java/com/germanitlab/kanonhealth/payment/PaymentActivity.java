package com.germanitlab.kanonhealth.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.germanitlab.kanonhealth.DoctorProfile;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.chat.ChatActivity;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.main.MainActivity;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.profile.ProfileActivity;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentActivity extends AppCompatActivity {
    String doctorJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        doctorJson = intent.getStringExtra("doctor_data");
    }

    @OnClick(R.id.next)
    public void nextClicked (){
        Gson gson = new Gson();
        User doctor = gson.fromJson(doctorJson , User.class);
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                Log.e("Update user response :", "no response found");

            }

            @Override
            public void onFailed(String error) {
                Log.e("Error", error);
            }
        }).sendPayment(String.valueOf(AppController.getInstance().getClientInfo().getUser_id()), String.valueOf(AppController.getInstance().getClientInfo().getUser_id()),String.valueOf(doctor.get_Id()) , "free");
        Intent intent = new Intent(this , ChatActivity.class);
        intent.putExtra("doctor_data" , doctorJson);
        intent.putExtra("from" , true);
        startActivity(intent);
        finish();
        DoctorProfile.profileActivity.finish();
        PreRequest.preRequest.finish();

    }

    @OnClick(R.id.cancel)
    public void cancelClicked(View view){
        startActivity(new Intent(this , MainActivity.class));
    }
}
