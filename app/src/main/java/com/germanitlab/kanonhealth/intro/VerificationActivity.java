package com.germanitlab.kanonhealth.intro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.PasscodeActivty;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Register;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.helpers.CacheJson;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.initialProfile.ProfileDetails;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
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

    @BindView(R.id.verification_Code)
    EditText verification_Code;
    Boolean oldUser;
    Util util ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        util = Util.getInstance(this);
        util.setupUI(findViewById(R.id.verifiction_layout) , this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        try {
            Intent intent = getIntent();
            number = intent.getStringExtra("number");
            code = intent.getStringExtra("codeNumber");
            oldUser = intent.getBooleanExtra("oldUser", false);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.btn_verification_verify_code)
    public void verify() {
            if (verification_Code.getText().toString().length() == 0 || verification_Code.getText().toString().length() <6) {
                Toast.makeText(getApplicationContext(), R.string.enter_the_correct_code, Toast.LENGTH_LONG).show();
                return;
            }else {
                verificationCode = verification_Code.getText().toString();
            }
            final Register register= (Register) getIntent().getExtras().get(Constants.REGISER_RESPONSE);

        PrefHelper.put(getApplicationContext(),PrefHelper.KEY_IS_LOGIN,true);
        PrefHelper.put(getApplicationContext(),PrefHelper.KEY_USER_ID, register.getId());
        PrefHelper.put(getApplicationContext(),PrefHelper.KEY_USER_PASSWORD, register.getPassword());

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
        try {
            CacheJson.writeObject(VerificationActivity.this, Constants.REGISER_RESPONSE, register);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (oldUser) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserInfo userInfo= ApiHelper.getUserInfo(VerificationActivity.this,String.valueOf(register.getId()));
                        if(userInfo==null || !userInfo.getUserID().equals(register.getId())){
                            finish();
                        }else{
                            PrefHelper.put(getApplicationContext(),PrefHelper.KEY_USER_KEY, userInfo);
                            if(userInfo.getUserType()==UserInfo.DOCTOR)
                                PrefHelper.put(getApplicationContext(),PrefHelper.KEY_IS_DOCTOR, true);
                            Intent intent = new Intent(VerificationActivity.this, PasscodeActivty.class);
                            intent.putExtra("checkPassword", false);
                            intent.putExtra("finish", false);
                            intent.putExtra("has_back", false);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).start();

            } else {
                Intent i = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(i);
                finish();


            }
    }

    private void handelEvent() {


    }

    @OnClick(R.id.verification_Code)
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





    @Override
    public void onBackPressed() {
        System.out.println("no action");
    }
}
