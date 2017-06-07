package com.germanitlab.kanonhealth.intro;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.inquiry.InquiryActivity;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.main.MainActivity;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class StartQrScan extends AppCompatActivity {
    PrefManager prefManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        new IntentIntegrator(this).initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
//                if (prefManager.getData(Constants.USER_ID) != null) {
//
//                    Intent i = new Intent(this, MainActivity.class);
//                    startActivity(i);
//                }
                finish();

            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                final String key = result.getContents();
                final Dialog dialog = new Dialog(this);

                dialog.setContentView(R.layout.activity_qr_activity);
                dialog.setTitle("Custom Alert Dialog");
                dialog.setCanceledOnTouchOutside(false);
                Button btnDoctor = (Button) dialog.findViewById(R.id.doctor);
                Button btnClinic = (Button) dialog.findViewById(R.id.clinic);
                Button btnUser = (Button) dialog.findViewById(R.id.doctor);
                Button btnCancel = (Button) dialog.findViewById(R.id.cancel);
                btnDoctor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendRequest(key , 2);
                    }
                });
                btnClinic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendRequest(key , 3);
                    }
                });
                btnUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendRequest(key , 1);
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext() , MainActivity.class));
                    }
                });

                dialog.show();
                //Save the User ID in the sh

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void sendRequest(String key, int i) {
        showProgressDialog();
        //Getting the doctor's data
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Intent i = new Intent(getApplicationContext(), InquiryActivity.class);
                i.putExtra("doctor_data", json);
                startActivity(i);
                finish();
                DismissProgressDialog();


            }

            @Override
            public void onFailed(String error) {
                DismissProgressDialog();
                Log.e("My error ", error.toString());
            }
        }).getDoctor(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                , AppController.getInstance().getClientInfo().getPassword(), key , i);
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.waiting_text), true);
    }

    public void DismissProgressDialog() {
        progressDialog.dismiss();
    }
}
