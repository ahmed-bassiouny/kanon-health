package com.germanitlab.kanonhealth.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Register;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.initialProfile.CountryActivty;
import com.mukesh.countrypicker.Country;
import java.util.regex.Pattern;


public class SignupActivity extends ParentActivity {


    Button btnSignUp;
    private EditText etMobileNumber, etPostelCode;
    private LinearLayout layout;
    private TextView select_country;
    Boolean found;
    String country, code;
    Constants constants;
    public SignupActivity signupActivity;
    final int REQUEST_CODE = 10;
    Util util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        util = Util.getInstance(this);
        util.setupUI(findViewById(R.id.signup_layout), this);
        signupActivity = this;
        try {
            constants = new Constants();
            found = true;
            initView();
            handelEvent();
            getCountryFromSIM();

            etPostelCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    found = false;

                    String temp = etPostelCode.getText().toString().replaceAll(Pattern.quote("+"), "");

                    for (Country c : Country.getAllCountries()) {
                        if (c.getDialCode().toLowerCase().equals("+" + temp)) {
                            select_country.setText(c.getName());
                            country = c.getName();
                            code = c.getDialCode();
                            found = true;
                        }
                    }
                    if (!found) {
                        select_country.setText(getResources().getString(R.string.Invalid_country));
                        country = "";
                        code = "";
                    }

                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

        if (savedInstanceState != null)
            etMobileNumber.setText(savedInstanceState.getString("phone"));
    }

    private void getCountryFromSIM() {
        String countryAndCode;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            countryAndCode = telephonyManager.getSimCountryIso();
            if (!TextUtils.isEmpty(countryAndCode)) {
                setCountryAndCode(countryAndCode);
            } else {
                getCountryFromNetwork();
            }

        } else {
            getCountryFromNetwork();
        }
    }

    private void getCountryFromNetwork() {
        //   util.showProgressDialog();
        showProgressBar();

        new Thread(new Runnable() {
            @Override
            public void run() {

                String result = ApiHelper.getNetworkCountryCode();

                if (result.equals("")) {
                    getCountryFromLocal();
                } else {
                    setCountryAndCode(result);
                }
                hideProgressBar();
            }

        }).start();
            }

//        new HttpCall(this, new ApiResponse() {
//            @Override
//            public void onSuccess(Object response) {
//                try {
//                    String countryAndCode = ((JsonObject) response).get("countryCode").toString().substring(1, ((JsonObject) response).get("countryCode").toString().length() - 1);
//
//                    util.dismissProgressDialog();
//                } catch (Exception e) {
//
//                    util.dismissProgressDialog();
//                }
//
//            }
//
//            @Override
//            public void onFailed(String error) {
//                getCountryFromLocal();
//                util.dismissProgressDialog();
//            }
//        }).getLocation();



    private void getCountryFromLocal() {
        String countryAndCode;
        countryAndCode = getResources().getConfiguration().locale.getCountry();
        if (!TextUtils.isEmpty(countryAndCode))
            setCountryAndCode(countryAndCode);
        else
            setDefaultCountry();

    }

    private void setDefaultCountry() {
        etPostelCode.setText("+49");
        select_country.setText("germany");
        country = "germany";
        code = "+49";
        found = true;
    }

    private void setCountryAndCode(String countryAndCode) {
        final Country countryObject = Country.getCountryByISO(countryAndCode);
        if (countryObject != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    etPostelCode.setText(countryObject.getDialCode());
                    select_country.setText(countryObject.getName());
                    country = countryObject.getName();
                    code = countryObject.getDialCode();
                    found = true;
                }
            });

        } else
            setDefaultCountry();
    }


    private void initView() {
        select_country = (TextView) findViewById(R.id.select_country);
        layout = (LinearLayout) findViewById(R.id.signup_layout);
        etMobileNumber = (EditText) findViewById(R.id.et_signup_mobile_number);
        etPostelCode = (EditText) findViewById(R.id.et_sigup_country_code);
        btnSignUp = (Button) findViewById(R.id.btn_signup_mobile_continue);
        select_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CountryActivty.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        etMobileNumber.requestFocus();
        etMobileNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendData();
                return true;
            }
        });

    }

    private void handelEvent() {

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

    }


    private void sendData() {

        if (Helper.isNetworkAvailable(this)) {
            //internet is connected do something
            if (found && (!select_country.equals("") || !select_country.equals(null)) && code != null && !etMobileNumber.getText().equals("") && etMobileNumber.getText().length() >= 8 && etMobileNumber.getText().length() <= 15) {
                showProgressBar();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Register register = ApiHelper.postRegister(code.toString(), etMobileNumber.getText().toString(), SignupActivity.this);
                        if (register != null) {
                            PrefHelper.put(getApplicationContext(),PrefHelper.KEY_USER_ID, register.getId());
                            PrefHelper.put(getApplicationContext(),PrefHelper.KEY_USER_PASSWORD, register.getPassword());

                            Intent intent = new Intent(SignupActivity.this, VerificationActivity.class);
                            intent.putExtra("number", etMobileNumber.getText().toString());
                            intent.putExtra("codeNumber", code.toString());
                            intent.putExtra(Constants.REGISER_RESPONSE, register);
                            intent.putExtra("oldUser", register.getExists());
                            startActivity(intent);
                            finish();
                        }
                        hideProgressBar();
                    }
                }).start();

            /*AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Register result = ApiHelper.postRegister(code.toString(), etMobileNumber.getText().toString(), SignupActivity.this);
                    Log.d("", "");
                }
            });*/
            } else {
                Toast.makeText(SignupActivity.this, getResources().getText(R.string.error_please_enter_number), Toast.LENGTH_SHORT).show();
            }

        } else {
            //do something, net is not connected
            Toast.makeText(SignupActivity.this, getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }

    }



//    @Override
//    public void onSuccess(Object response) {
//        //-- Response : {"password":"831558a6e65a225c710b084742f407b6","user_id":97,"sucess":true}
//        try {
//
//            Log.d("Response", response.toString());
//            UserRegisterResponse registerResponse = (UserRegisterResponse) response;
//            if (String.valueOf(registerResponse.getUser_id()).isEmpty() || registerResponse.getUser_id() == 0 || String.valueOf(registerResponse.getUser_id()).equals(null)) {
//                Toast.makeText(this, getResources().getText(R.string.error_in_registeration), Toast.LENGTH_SHORT).show();
//                System.exit(0);
//            }
//            prefManager.put(PrefManager.USER_ID, String.valueOf(registerResponse.getUser_id()));
//            prefManager.put(PrefManager.USER_PASSWORD, String.valueOf(registerResponse.getPassword()));
//            JSONObject jsonObject = null;
//            Boolean oldUser = false;
//            if (registerResponse.is_exist()) {
//                oldUser = true;
//            }
//            if (registerResponse.isSucess()) {
//                util.dismissProgressDialog();
//                Intent intent = new Intent(SignupActivity.this, VerificationActivity.class);
//                intent.putExtra("number", etMobileNumber.getText().toString());
//                intent.putExtra("codeNumber", code.toString());
//                intent.putExtra(Constants.REGISER_RESPONSE, registerResponse);
//                intent.putExtra("oldUser", oldUser);
//                startActivity(intent);
//
//            } else {
//
//                util.dismissProgressDialog();
//                Snackbar snackbar = Snackbar
//                        .make(layout, getResources().getString(R.string.error_message), Snackbar.LENGTH_LONG);
//                snackbar.show();
//
//            }
//        } catch (Exception e) {
//            Crashlytics.logException(e);
//            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
//        }
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            code = data.getStringExtra("codeC");
            etPostelCode.setText(code);
            country = data.getStringExtra("country");
            select_country.setText(data.getStringExtra("country"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("phone", etMobileNumber.getText().toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("phone", etMobileNumber.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }
}
