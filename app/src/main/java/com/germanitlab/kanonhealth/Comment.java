package com.germanitlab.kanonhealth;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.responses.RateDoctorResponse;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.httpchat.HttpChatActivity;
import com.germanitlab.kanonhealth.ormLite.UserRepository;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Comment extends AppCompatActivity {

    // doctor info

    @BindView(R.id.txt_doctor_name)
    TextView txt_doctor_name;

    //comment info
    @BindView(R.id.rb_doctor_rate)
    RatingBar rb_doctor_rate;
    @BindView(R.id.edt_comment)
    EditText edt_comment;

    @BindView(R.id.img_chat_user_avatar)
    ImageView img_chat_user_avatar;


    @BindView(R.id.img_back)
    ImageView img_back;

    String doc_id = "";
    String req_id = "";
    UserRepository userRepository;
    UserInfo doctor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        userRepository = new UserRepository();
        doctor = new UserInfo();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {
            doc_id = getIntent().getStringExtra("doc_id");
            req_id = getIntent().getStringExtra("request_id");
            doctor.setUserID(Integer.valueOf(doc_id));
            //doctor = userRepository.get(doctor);
            txt_doctor_name.setText(doctor.getFullName());
            if (doctor.getAvatar() != null && !doctor.getAvatar().isEmpty()) {
                ImageHelper.setImage(img_chat_user_avatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + doctor.getAvatar());
            }

        } catch (Exception e) {
            doc_id = "";
            req_id = "";
        }
        edt_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                submit();
                return true;
            }
        });
    }

    private boolean validationInput() {
        if (edt_comment.getText().toString().trim().isEmpty() || rb_doctor_rate.getRating() <= 0.00 || doc_id.isEmpty()) {
            Toast.makeText(this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.submit)
    public void submit() {
        try {
            if (validationInput()) {

                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PrefManager prefManager = new PrefManager(Comment.this);
                        RateDoctorResponse result = ApiHelper.rateDoctor(Comment.this, prefManager.getData(PrefManager.USER_ID), doctor.getUserID().toString(), req_id, edt_comment.getText().toString(), String.valueOf(rb_doctor_rate.getRating()));
                        if (result != null) {
                            if (result.getData()) {
                                Toast.makeText(Comment.this, R.string.thanks_for_comment, Toast.LENGTH_SHORT).show();
                                //doctor.setHave_rate(1);
                                //userRepository.update(doctor);
                                Intent intent = new Intent(getApplicationContext(), HttpChatActivity.class);
                                intent.putExtra("doctorID", Integer.valueOf(doc_id));
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Comment.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Comment.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                })).run();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("Comment Tag", "Comment Excepotion", e);
        }

    }

    @Override
    public void onBackPressed() {
        // doctor.setHave_rate(1);
        //userRepository.update(doctor);
        Intent intent = new Intent(this, HttpChatActivity.class);
        intent.putExtra("doctorID", Integer.valueOf(doc_id));
        startActivity(intent);
        finish();

    }
}
