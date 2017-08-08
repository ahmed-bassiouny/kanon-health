package com.germanitlab.kanonhealth.intro;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.DoctorProfileActivity;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.models.WebLogin;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class StartQrScan extends AppCompatActivity {
    PrefManager prefManager;
    ProgressDialog progressDialog;
    Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        new IntentIntegrator(this).initiateScan();
        util = Util.getInstance(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        try {
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, R.string.cancelled, Toast.LENGTH_LONG).show();
//                if (prefManager.getData(Constants.USER_ID) != null) {
//
//                    Intent i = new Intent(this, MainActivity.class);
//                    startActivity(i);
//                }
                    finish();

                } else if (result.getContents().startsWith("WEBLOGIN_")) {
                    sendQrCode(result.getContents().replace("WEBLOGIN_", ""));
                } else {
                    final String key = result.getContents();
                    sendRequest(key);

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

    private void sendRequest(String key) {
        util.showProgressDialog();
        //Getting the doctor's data
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                try {
                    UserInfoResponse userInfoResponse = (UserInfoResponse) response;
                    Intent intent = new Intent(StartQrScan.this, DoctorProfileActivity.class);
                    intent.putExtra("doctor_data", userInfoResponse.getUser());
                    intent.putExtra("tab", "");
                    startActivity(intent);
                    finish();
                    util.dismissProgressDialog();
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                util.dismissProgressDialog();
                Log.e("My error ", error.toString());
            }
        }).getDoctor(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), key);
    }


    public void sendQrCode(String qrCode) {
        try {

            WebLogin webLogin = new WebLogin();
            webLogin.setUser_id(prefManager.getData(PrefManager.USER_ID));
            webLogin.setPassword(prefManager.getData(PrefManager.USER_PASSWORD));
            webLogin.setCode(qrCode);

            new HttpCall(StartQrScan.this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    StartQrScan.this.finish();
                }

                @Override
                public void onFailed(String error) {
                    Toast.makeText(StartQrScan.this, R.string.message_not_send, Toast.LENGTH_SHORT).show();
                }
            }).sendQrCode(webLogin);

        } catch (Exception e) {
            Log.e("Httpcall", "sen web login : ", e);
            Crashlytics.logException(e);
        }
    }
}
