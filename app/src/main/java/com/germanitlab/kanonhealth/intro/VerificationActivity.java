package com.germanitlab.kanonhealth.intro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.PasscodeActivty;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.async.SocketCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.CacheJson;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.initialProfile.ProfileDetails;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.main.MainActivity;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserRegisterResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VerificationActivity extends AppCompatActivity {

    private LinearLayout layout;
    private Button btnVerify;
    private ProgressDialog progressDialog;
    private TextView tv_toolbar_mobile_number;
    String number, code;
    String verificationCode;
    PrefManager mPrefManager;

    @BindView(R.id.verification_Code)
    EditText verification_Code;
    Boolean oldUser ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        try {
            Intent intent = getIntent();
            number = intent.getStringExtra("number");
            code = intent.getStringExtra("codeNumber");
            mPrefManager = new PrefManager(this);
            oldUser = intent.getBooleanExtra("oldUser" , false);
            tv_toolbar_mobile_number = (TextView) findViewById(R.id.tv_toolbar_mobile_number);
            tv_toolbar_mobile_number.append("  " + code + " " + number);
            ButterKnife.bind(this);
            verification_Code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    verify();
                    return true;
                }
            });

            initView();
            handelEvent();
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }



    }

    @OnClick(R.id.btn_verification_verify_code)
    public void verify(){
        try {
            if (verification_Code.getText().toString().length() == 0 ) {
                Toast.makeText(getApplicationContext(), "Geben Sie den richtigen Code ein", Toast.LENGTH_LONG).show();
                return;
            } else {
                verificationCode = verification_Code.getText().toString();
            }
            final UserRegisterResponse registerResponse = (UserRegisterResponse) getIntent().getExtras().get(Constants.REGISER_RESPONSE);
            showProgressDialog();
            new HttpCall(VerificationActivity.this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {

                    JSONObject jsonObject = null;
                    try {
                        Log.d("my response server ", response.toString());
                        jsonObject = new JSONObject(response.toString());
                        if (jsonObject.has("status") && jsonObject.getInt("active") == 1) {
                            PrefManager prefManager = new PrefManager(VerificationActivity.this);
                            prefManager.setLogin(true);
                            AppController.getInstance().setClientInfo(registerResponse);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        FirebaseInstanceId.getInstance().deleteInstanceId();
                                        FirebaseInstanceId.getInstance().getToken();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            CacheJson.writeObject(VerificationActivity.this, Constants.REGISER_RESPONSE, registerResponse);
                            joinUser();
                            dismissProgressDialog();
                            if(oldUser) {
                                User user = new User();
                                user.setId(AppController.getInstance().getClientInfo().getUser_id());
                                user.setPassword(AppController.getInstance().getClientInfo().getPassword());
                                new HttpCall(VerificationActivity.this, new ApiResponse() {
                                    @Override
                                    public void onSuccess(Object response) {


                                        mPrefManager.put(mPrefManager.USER_KEY, response.toString());

                                        /*Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();*/
                                        Intent intent = new Intent(VerificationActivity.this, PasscodeActivty.class);
                                        intent.putExtra("checkPassword", false);
                                        intent.putExtra("finish", false);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailed(String error) {
                                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.error_saving_data), Toast.LENGTH_SHORT).show();

                                    }
                                }).editProfile(user);
                            }
                            else {
                                Intent i = new Intent(getApplicationContext(), ProfileDetails.class);
                                //i.putExtra("isfirst", "true");
                                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        } else {
                            dismissProgressDialog();
                            Snackbar snackbar = Snackbar
                                    .make(layout, getResources().getString(R.string.error_message), Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                    } catch (Exception e){
                        Crashlytics.logException(e);
                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailed(String error) {
                    dismissProgressDialog();
                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();
                }
            }).activateUser(registerResponse.getUser_id(), registerResponse.getPassword(), verificationCode.toString());
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void handelEvent() {


    }

    @OnClick(R.id.verifiction_layout)
    public void verificationLayoutClick(View view) {
        if (verification_Code.getText().length() == 0) {
            verification_Code.requestFocus();
            InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(verification_Code, 0);
        }

    }


    private void initView() {
        layout = (LinearLayout) findViewById(R.id.verifiction_layout);
        btnVerify = (Button) findViewById(R.id.btn_verification_verify_code);
    }


    public void joinUser() {

        new SocketCall(getApplicationContext(), new ApiResponse() {
            @Override
            public void onSuccess(Object response) {

                Log.d("Join User1 Response", response.toString());

            }

            @Override
            public void onFailed(String error) {

                Log.e("Join User1 Response", error.toString());

                Helper.showAlertDialog(getApplicationContext(), getString(R.string.warning), getString(R.string.some_wrong), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        joinUser();
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });


            }
        }).joinUser(AppController.getInstance().getClientInfo().getUser_id());
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.waiting_text), true);
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }
}
