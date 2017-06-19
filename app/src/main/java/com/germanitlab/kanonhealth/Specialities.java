package com.germanitlab.kanonhealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
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
                try {
                    Gson gson = new Gson();
                    String json = gson.toJson(response);
                    Log.e("returned msg ", json);
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
                Log.e("error in returned json", error);
            }
        }).getSpecialities((String.valueOf(AppController.getInstance().getClientInfo().getUser_id()))
                , AppController.getInstance().getClientInfo().getPassword());
    }
}
