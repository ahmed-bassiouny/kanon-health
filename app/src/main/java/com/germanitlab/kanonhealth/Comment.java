package com.germanitlab.kanonhealth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Comment extends AppCompatActivity {

    // doctor info
    @BindView(R.id.img_doctor_image) ImageView img_doctor_image;
    @BindView(R.id.txt_doctor_name) TextView txt_doctor_name;

    //comment info
    @BindView(R.id.rb_doctor_rate) RatingBar rb_doctor_rate;
    @BindView(R.id.edt_comment) EditText edt_comment;

    String doc_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        try {
            doc_id = getIntent().getStringExtra("doc_id");
        }catch (Exception e){
            doc_id="";
        }
        edt_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                submit();
                return true;
            }
        });
    }

    private boolean validationInput(){
        if(edt_comment.getText().toString().trim().isEmpty() || rb_doctor_rate.getRating()<=0.00 || doc_id.isEmpty()){
            Toast.makeText(this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.submit)
    public void submit(){
        Toast.makeText(this, doc_id, Toast.LENGTH_SHORT).show();
        if(validationInput()){
            new HttpCall(this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    Toast.makeText(Comment.this, R.string.thanksforcomment, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailed(String error) {
                    Toast.makeText(Comment.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                }
            }).rateDoctor(doc_id,edt_comment.getText().toString(),String.valueOf(rb_doctor_rate.getRating()));
        }
    }
}
