package com.germanitlab.kanonhealth.intro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.initialProfile.CountriesCodes;
import com.germanitlab.kanonhealth.initialProfile.CountryActivty;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.UserRegisterResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class SignupActivity extends AppCompatActivity implements ApiResponse {


    Button btnSignUp;
    ProgressDialog progressDialog;
    private EditText etMobileNumber, etPostelCode;
    private LinearLayout layout;
    private TextView select_country;
    Boolean found;
    String country, code;
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Intent intent = getIntent();
        Constants.COUNTRY_CODES.clear();
        Gson gson = new Gson();
        constants=new Constants();
        List<CountriesCodes> codess = gson.fromJson(constants.CountryCode ,  new TypeToken<List<CountriesCodes>>(){}.getType());
        found = true;
        for (CountriesCodes countriescooed: codess) {
            Constants.COUNTRY_CODES.put(countriescooed.getName() , countriescooed.getDial_code());

        }
/*
        Constants.COUNTRY_CODES.clear();
        Constants.COUNTRY_CODES.put("مصر", "+20");
        Constants.COUNTRY_CODES.put("Germany", "+49");
        Constants.COUNTRY_CODES.put("USA", "+10");
        Constants.COUNTRY_CODES.put("JER", "+50");
        Constants.COUNTRY_CODES.put("Kore", "+58");
        Constants.COUNTRY_CODES.put("Dew", "+74");
        Constants.COUNTRY_CODES.put("SWQ", "+84");
*/

        initView();
        handelEvent();
        try {
            code = intent.getStringExtra("codeC");
            etPostelCode.setText(code);
            country = intent.getStringExtra("country");
            select_country.setText(intent.getStringExtra("country"));
            if (code == null || select_country == null) {
                code = "+49";
                etPostelCode.setText("+49");
                select_country.setText("Germany");
                found = true;
            }
        } catch (Exception e) {
            etPostelCode.setText("+49");
            select_country.setText("Germany");
        }
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
                if (temp == "") {
                } else {
                    int number = Integer.parseInt(temp);
                }
                for (Map.Entry<String, String> e : Constants.COUNTRY_CODES.entrySet()) {
                    if (e.getValue().toLowerCase().equals("+" + temp)) {
                        select_country.setText(e.getKey().toString());
                        country = e.getKey().toString();
                        code = e.getValue().toString();
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
                startActivity(intent);
            }
        });
        etMobileNumber.requestFocus();
    }

    private void handelEvent() {

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (found && (!select_country.equals("") || !select_country.equals(null)) && code != null &&  !etMobileNumber.getText().equals("") &&  etMobileNumber.getText().length()>=9)
                    registerUser(etMobileNumber.getText().toString(), code.toString());
                else
                    Toast.makeText(SignupActivity.this, getResources().getText(R.string.Invalid_country), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void registerUser(String phone, String countryCode) {
        showProgressDialog();
        new HttpCall(SignupActivity.this, this).registerUser(phone, countryCode);
    }


    @Override
    public void onSuccess(Object response) {
        //-- Response : {"password":"831558a6e65a225c710b084742f407b6","user_id":97,"sucess":true}

        Log.d("Response", response.toString());
        UserRegisterResponse registerResponse = (UserRegisterResponse) response;
        JSONObject jsonObject = null;
        Boolean oldUser = false  ;
        try {
            jsonObject = new JSONObject(response.toString());
            if(jsonObject.getBoolean("is_exist"))
            {
                oldUser = true ;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (registerResponse.isSucess()) {
            dismissProgressDialog();
            Intent intent = new Intent(SignupActivity.this, VerificationActivity.class);
            intent.putExtra("number", etMobileNumber.getText().toString());
            intent.putExtra("codeNumber", code.toString());
            intent.putExtra(Constants.REGISER_RESPONSE, registerResponse);
            intent.putExtra("oldUser" , oldUser);
            startActivity(intent);
            finish();

        } else {

            dismissProgressDialog();
            Snackbar snackbar = Snackbar
                    .make(layout, getResources().getString(R.string.error_message), Snackbar.LENGTH_LONG);
            snackbar.show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFailed(String error) {


    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.waiting_text), true);
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

}
