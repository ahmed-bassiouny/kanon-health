package com.germanitlab.kanonhealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.google.gson.Gson;

public class Specialities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialities);
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                Gson gson = new Gson();
                String json =gson.toJson(response);
                Log.e("returned msg " , json);
            }

            @Override
            public void onFailed(String error) {
                Log.e("error in returned json" , error);
            }
        }).getSpecialities((String.valueOf(AppController.getInstance().getClientInfo().getUser_id()))
                , AppController.getInstance().getClientInfo().getPassword());
    }
}
