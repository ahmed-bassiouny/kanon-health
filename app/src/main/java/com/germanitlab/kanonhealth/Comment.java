package com.germanitlab.kanonhealth;

import android.content.Intent;
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
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
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

    UserInfo userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
       // userRepository = new UserRepository();
       // doctor = new UserInfo();
        userInfo= (UserInfo) getIntent().getSerializableExtra("user_info") ;
        if(userInfo==null)
        {
            finish();
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {

//            if (Helper.isNetworkAvailable(getApplicationContext())) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                        if(userInfo.getUserType()==UserInfo.DOCTOR) {
                            doc_id = String.valueOf(userInfo.getUserID());
                            req_id = String.valueOf(userInfo.getRequestID());
                          //  UserInfo temp = ApiHelper.getUserInfo(Comment.this, doc_id);
                           // if (temp != null) {
                               // userInfo = temp;

//                            }
                       }else {
                            doc_id = String.valueOf(userInfo.getId());
                            req_id = String.valueOf(userInfo.getRequestID());

                           // UserInfo temp = ApiHelper.postGetClinic(userInfo.getId(), Comment.this);
                           // if (temp != null) {
                               // userInfo = temp;
                            }
//            Comment.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
            if(userInfo.getUserType()==UserInfo.DOCTOR) {
                txt_doctor_name.setText(userInfo.getFullName());
            }else
            {
                txt_doctor_name.setText(userInfo.getName());
            }
                    if (userInfo.getAvatar() != null && !userInfo.getAvatar().isEmpty()) {
                        ImageHelper.setImage(img_chat_user_avatar, ApiHelper.SERVER_IMAGE_URL + "/" + userInfo.getAvatar(), R.drawable.placeholder);
                    }
//                }
//            });
                    //    }
//
//                    }
//                }).start();
                //doctor = userRepository.get(doctor);
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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RateDoctorResponse result = ApiHelper.rateDoctor(Comment.this, String.valueOf(PrefHelper.get(Comment.this,PrefHelper.KEY_USER_ID,-1)), doc_id, req_id, edt_comment.getText().toString(), String.valueOf(rb_doctor_rate.getRating()),userInfo.getUserType());
                        if (result != null) {
                            if (result.getData()) {
                           //     Toast.makeText(Comment.this, R.string.thanks_for_comment, Toast.LENGTH_SHORT).show();
                                //doctor.setHave_rate(1);
                                //userRepository.update(doctor);
                                    Intent intent = new Intent(getApplicationContext(), HttpChatActivity.class);
                                if(userInfo.getUserType()==UserInfo.DOCTOR) {
                                    intent.putExtra("doctorID", userInfo.getUserID());
                                }else
                                {
                                    intent.putExtra("doctorID", userInfo.getId());
                                }
                                    intent.putExtra("userInfo", userInfo);
                                     intent.putExtra("type",userInfo.getUserType());
                                    startActivity(intent);
                                    finish();

                            } else {
                               // Toast.makeText(Comment.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                          //  Toast.makeText(Comment.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
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
        if(userInfo.getUserType()==UserInfo.DOCTOR) {
            intent.putExtra("doctorID", userInfo.getUserID());
        }else
        {
            intent.putExtra("doctorID", userInfo.getId());
        }
        intent.putExtra("userInfo",userInfo);
        intent.putExtra("type",userInfo.getUserType());
        startActivity(intent);
        finish();

    }
}
