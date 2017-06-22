package com.germanitlab.kanonhealth.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.Comment;
import com.germanitlab.kanonhealth.DoctorProfile;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.chat.ChatActivity;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.google.gson.Gson;

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
    User doctor ;
    private String type;
    PrefManager prefManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_payment);
        ButterKnife.bind(this);
        initTB();
        prefManager = new PrefManager(this);
        try {
            //        doctor = new Gson().fromJson(getIntent().getStringExtra("doctor_data") , User.class);

            doctor= (User) getIntent().getSerializableExtra("doctor_data");
        /*
        handel data in ui
         */
            handelUI(doctor);
        }catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }

    }

    private void handelUI(User doctorObj) {
        try {
            if(doctorObj!=null) {
                tvDoctorName.setText(doctorObj.getName());

                Helper.setImage(this, Constants.CHAT_SERVER_URL
                        + "/" + doctorObj.getAvatar(), ivDoctor, R.drawable.placeholder);

                if (doctorObj.getRate() != null) {
                    ratingBar.setRating(Float.parseFloat(doctorObj.getRate()));
                } else {
                    ratingBar.setRating(0);
                }
            }
        }catch (Exception e) {
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
    public void nextClicked (){
        try {
            if(doctor.isClinic==1)
            {
                type="3";
            }else {
                type="2";
            }

            new HttpCall(PaymentActivity.this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
//                    Intent intent = new Intent(PaymentActivity.this, Comment.class);
//                    intent.putExtra("doc_id", String.valueOf(doctor.get_Id()));
//                    startActivity(intent);
                }

                @Override
                public void onFailed(String error) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }).sendSessionRequest(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD),
                    String.valueOf(doctor.getId()),
                    type);



        final Gson gson = new Gson();
            Intent intent = new Intent(getApplicationContext() , ChatActivity.class);

            if (doctor!=null) {
                doctor.setIsOpen(1);
            }
            intent.putExtra("doctor_data" , gson.toJson(doctor));
            intent.putExtra("from" , true);
            startActivity(intent);
            finish();
        }catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }

    }

//    @OnClick(R.id.cancel)
//    public void cancelClicked(View view){
//        startActivity(new Intent(this , MainActivity.class));
//    }
}
