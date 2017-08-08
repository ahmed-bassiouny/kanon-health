package com.germanitlab.kanonhealth.intro;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ProgressHelper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.inquiry.InquiryActivity;
import com.germanitlab.kanonhealth.main.MainActivity;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QrActivity extends AppCompatActivity {
    /*PrefManager prefManager;
    @BindView(R.id.button_qr_scanning)
    ImageButton buttonQrScanning;
    UserInfo doctor;
    @BindView(R.id.layout)
    LinearLayout linearLayout;
    @BindView(R.id.error)
    TextView errortxt;

    ProgressDialog progressDialog;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    Util util ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, R.string.cancelled, Toast.LENGTH_LONG).show();
                    if (prefManager.getData(Constants.USER_ID) != null) {

                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }
                    finish();

                } else {
                    final String key = result.getContents();
                    sendRequest(key);
                    //Getting the doctor's data
                    //Save the User ID in the sh

                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    public void sendRequest(String key ) {
        ProgressHelper.showProgressBar(QrActivity.this);
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                try {
                    doctor = (User) response;
                    Gson gson = new Gson();
                    String json = gson.toJson(doctor);
                    prefManager.put(prefManager.DOCTOR_KEY, json);
                    Intent i = new Intent(getApplicationContext(), InquiryActivity.class);
                    i.putExtra("doctor_data", json);
                    startActivity(i);
                    ProgressHelper.hideProgressBar();
                    finish();

                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailed(String error) {
                util.dismissProgressDialog();
                Log.e("My error ", error.toString());
                linearLayout.setVisibility(View.GONE);
                errortxt.setVisibility(View.VISIBLE);
            }
        }).getDoctor(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), key);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            prefManager = new PrefManager(this);
            util = Util.getInstance(this);
            String doctorId = prefManager.getData("doctor_id");
            Log.e("Splash User id", doctorId);
            if (doctorId.equals("")) {
                setContentView(R.layout.activity_qr_layout);
                ButterKnife.bind(this);
            } else {
                new IntentIntegrator(this).initiateScan();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.button_qr_scanning)
    public void buttonQrOnClicked() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //Asking for the camera permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                //Opening the QR Scanner
                new IntentIntegrator(this).initiateScan();
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_CAMERA: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        new IntentIntegrator(this).initiateScan();
                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }

                // other 'case' lines to check for other
                // permissions this app might request
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }*/
}