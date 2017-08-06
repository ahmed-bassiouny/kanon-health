package com.germanitlab.kanonhealth.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.httpchat.HttpChatActivity;
import com.germanitlab.kanonhealth.models.Payment;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.ormLite.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.iv_doctor)
    CircleImageView ivDoctor;

    @BindView(R.id.tv_name)
    TextView tvDoctorName;

    @BindView(R.id.tv_special)
    TextView tvDoctorSpecial;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.rb_free)
    RadioButton rbFree;

    @BindView(R.id.rb_euro)
    RadioButton rbEuro;

    @BindView(R.id.text_voucher)
    EditText etVoucher;

    @BindView(R.id.rb_voucher)
    RadioButton rbVoucher;

    @BindView(R.id.rg_payment)
    RadioGroup rgPayment;
    User doctor;
    private String type;
    PrefManager prefManager;
    List<RadioButton> radioButtons = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_payment);
        ButterKnife.bind(this);
        initTB();
        prefManager = new PrefManager(this);
        try {
            radioButtons = new ArrayList<RadioButton>();
            radioButtons.add((RadioButton) findViewById(R.id.rb_paypal));
            radioButtons.add((RadioButton) findViewById(R.id.rb_voucher));
            radioButtons.add((RadioButton) findViewById(R.id.rb_euro));
            radioButtons.add((RadioButton) findViewById(R.id.rb_free));
            handleRadioButtons();
            //        doctor = new Gson().fromJson(getIntent().getStringExtra("doctor_data") , User.class);
            doctor = new Gson().fromJson(prefManager.getData(PrefManager.USER_INTENT), User.class);
            if (doctor.getIsDoc() == 1 && prefManager.get(PrefManager.IS_DOCTOR)) {
                rbFree.setVisibility(View.VISIBLE);
            } else {
                rbFree.setVisibility(View.GONE);
            }
        /*
        handel data in ui
         */
            handelUI(doctor);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }

    }

    private void handleRadioButtons() {
        for (RadioButton button : radioButtons) {

            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) processRadioButtonClick(buttonView);
                }
            });

        }
    }

    private void processRadioButtonClick(CompoundButton buttonView) {

        for (RadioButton button : radioButtons) {

            if (button != buttonView) button.setChecked(false);
        }

    }

    private void handelUI(User doctorObj) {
        try {
            if (doctorObj != null) {
                tvDoctorName.setText(doctorObj.getFullName());

                ImageHelper.setImage(ivDoctor, Constants.CHAT_SERVER_URL + "/" + doctorObj.getAvatar(), R.drawable.placeholder);

                if (doctorObj.getRate() != null) {
                    ratingBar.setRating(Float.parseFloat(doctorObj.getRate()));
                } else {
                    ratingBar.setRating(0);
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }


    }

    private void initTB() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.payment));
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
//                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.next)
    public void nextClicked() {
        try {
            if (rgPayment.getCheckedRadioButtonId() == -1) {
                if (!rbVoucher.isChecked()) {
                    Toast.makeText(this, R.string.please_choose_one_of_these_methods, Toast.LENGTH_SHORT).show();
                    return;
                } else if (rbVoucher.isChecked() && etVoucher.getText().length() <= 0) {
                    Toast.makeText(this, R.string.please_enter_voucher_code, Toast.LENGTH_SHORT).show();
                    return;
                }

            }
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    int requestId = ApiHelper.openSession(PaymentActivity.this, prefManager.getData(PrefManager.USER_ID), String.valueOf(doctor.getId()));
                    if (requestId != -1) {
                        Intent intent = new Intent(PaymentActivity.this, HttpChatActivity.class);
                        intent.putExtra("doctorID", doctor.get_Id());
                        doctor.setIsOpen(1);
                        doctor.setRequest_id(requestId);
                        new UserRepository(PaymentActivity.this).update(doctor);
                        startActivity(intent);
                        finish();
                    }
                }
            })).run();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }

    }

//    @OnClick(R.id.cancel)
//    public void cancelClicked(View view){
//        startActivity(new Intent(this , MainActivity.class));
//    }
}
