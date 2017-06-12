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

import com.germanitlab.kanonhealth.DoctorProfile;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.chat.ChatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_payment);
        ButterKnife.bind(this);
        initTB();

        doctor = new Gson().fromJson(getIntent().getStringExtra("doctor_data") , User.class);
        /*
        handel data in ui
         */
        handelUI(doctor);
    }

    private void handelUI(User doctorObj) {

        if(doctorObj!=null) {
            tvDoctorName.setText(doctorObj.getName());

            Helper.setImage(this, Constants.CHAT_SERVER_URL
                    + "/" + doctorObj.getAvatar(), ivDoctor, R.drawable.profile_place_holder);

            if (doctorObj.getRate() != null) {
                ratingBar.setRating(Float.parseFloat(doctorObj.getRate()));
            } else {
                ratingBar.setRating(0);
            }
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
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.next)
    public void nextClicked (){
//        util.showProgressDialog();
        final Gson gson = new Gson();
//        new HttpCall(this, new ApiResponse() {
//            @Override
//            public void onSuccess(Object response) {
////                util.dismissProgressDialog();
//                Log.e("Update user response :", "no response found");
//                Intent intent = new Intent(getApplicationContext() , ChatActivity.class);
//                doctor.setIsOpen(1);
//                intent.putExtra("doctor_data" , gson.toJson(doctor));
//                intent.putExtra("from" , true);
//                startActivity(intent);
//                finish();
//            }
//
//            @Override
//            public void onFailed(String error) {
//                Toast.makeText(PaymentActivity.this, "No net", Toast.LENGTH_SHORT).show();
//                Log.e("Error", error);
//            }
//        }).sendSessionRequest(
//                String.valueOf(AppController.getInstance().getClientInfo().getUser_id()),
//                String.valueOf(AppController.getInstance().getClientInfo().getPassword()),
//                String.valueOf(doctor.get_Id())
//                , "free");

//        DoctorProfile.profileActivity.finish();
//        PreRequest.preRequest.finish();
                Intent intent = new Intent(getApplicationContext() , ChatActivity.class);

        if (doctor!=null) {
            doctor.setIsOpen(1);
        }
                intent.putExtra("doctor_data" , gson.toJson(doctor));
                intent.putExtra("from" , true);
                startActivity(intent);
                finish();
    }

//    @OnClick(R.id.cancel)
//    public void cancelClicked(View view){
//        startActivity(new Intent(this , MainActivity.class));
//    }
}
