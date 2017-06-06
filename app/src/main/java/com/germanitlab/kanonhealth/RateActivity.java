package com.germanitlab.kanonhealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.User;

import java.util.List;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RateActivity extends AppCompatActivity {

    // doctor name , rate , reviews number
    @BindView(R.id.txt_doctor_name) TextView txt_doctor_name;
    @BindView(R.id.rb_doctor_rate) RatingBar rb_doctor_rate;
    @BindView(R.id.txt_reviews) TextView txt_reviews;

    // rate stars Button
    @BindView(R.id.btn_five_stars) Button btn_five_stars;
    @BindView(R.id.btn_four_stars) Button btn_four_stars;
    @BindView(R.id.btn_three_stars) Button btn_three_stars;
    @BindView(R.id.btn_two_stars) Button btn_two_stars;
    @BindView(R.id.btn_one_star) Button btn_one_star;
    @BindView(R.id.temp) Button temp;

    // rate stars textview
    @BindView(R.id.txt_five_stars) TextView txt_five_stars;
    @BindView(R.id.txt_four_stars) TextView txt_four_stars;
    @BindView(R.id.txt_three_stars) TextView txt_three_stars;
    @BindView(R.id.txt_two_stars) TextView txt_two_stars;
    @BindView(R.id.txt_one_star) TextView txt_one_star;

    // recycle view
    @BindView(R.id.recycler_view) RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        ButterKnife.bind(this);
        txt_one_star.post(new TimerTask() {
            @Override
            public void run() {
                /*
                int height,width,result;
                height=temp.getHeight();
                width=temp.getWidth();
                result = (85*width)/100;
                FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(result, height);
                btn_four_stars.setLayoutParams(lp1);
                result = (50*width)/100;
                lp1 = new FrameLayout.LayoutParams(result, height);
                btn_three_stars.setLayoutParams(lp1);
                */
            }
        });

    }
}
