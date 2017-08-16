package com.germanitlab.kanonhealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.*;
import com.germanitlab.kanonhealth.api.models.Comment;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.ParentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RateActivity extends ParentActivity{

    // doctor name , rate , reviews number
    @BindView(R.id.txt_doctor_name)
    TextView txt_doctor_name;
    @BindView(R.id.rb_doctor_rate)
    RatingBar rb_doctor_rate;
    @BindView(R.id.txt_reviews_1)
    TextView txt_reviews_1;
    @BindView(R.id.txt_reviews_3)
    TextView txt_reviews_3;

    @BindView(R.id.img_back)
    ImageView ivBack;

    // rate stars Button
    @BindView(R.id.btn_five_stars)
    Button btn_five_stars;
    @BindView(R.id.btn_four_stars)
    Button btn_four_stars;
    @BindView(R.id.btn_three_stars)
    Button btn_three_stars;
    @BindView(R.id.btn_two_stars)
    Button btn_two_stars;
    @BindView(R.id.btn_one_star)
    Button btn_one_star;
    @BindView(R.id.temp)
    Button temp;

    // rate stars textview
    @BindView(R.id.txt_five_stars)
    TextView txt_five_stars;
    @BindView(R.id.txt_four_stars)
    TextView txt_four_stars;
    @BindView(R.id.txt_three_stars)
    TextView txt_three_stars;
    @BindView(R.id.txt_two_stars)
    TextView txt_two_stars;
    @BindView(R.id.txt_one_star)
    TextView txt_one_star;

    // recycle view
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.img_chat_user_avatar)
    ImageView img_chat_user_avatar;


    String doc_id = "14";
    RateAdapter rateAdapter;
    private HashMap<String, Integer> rate_percentages;
    private ArrayList<Comment> comments;
    private  float rate;
    private  int rateCount;
    int height, width;

    UserInfo doctor;
    UserInfo clinic;
    boolean isClinic;
    Review review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        ButterKnife.bind(this);
        isClinic=getIntent().getStringExtra("type").equals("clinic");
        if(!isClinic) {
            doctor = (UserInfo) getIntent().getSerializableExtra("doctor_info");
        }else
        {
            clinic=(UserInfo)getIntent().getSerializableExtra("clinic_info");
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        try {
            recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            if ( (isClinic&&clinic==null)||(!isClinic&&doctor==null)) {
                finish();
            }

           showProgressBar();
            loadData();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            Log.e("Rate Activity", "", e);

        }

    }

    private void loadData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                       if(isClinic)
                       {
                          review = ApiHelper.getReview(String.valueOf(clinic.getId()), RateActivity.this);
                           Log.i("ReviewId", clinic.getId()+"");
                       }else {
                            review = ApiHelper.getReview(String.valueOf(doctor.getUserID()), RateActivity.this);
                       }
                if(review!=null){
                    RateActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(review.getRatePercentage().size()>0)
                            {
                                rate_percentages= review.getRatePercentage().get(0) ;
                                rate=review.getRate();
                                rateCount=review.getRateCount();
                                txt_one_star.post(new TimerTask() {
                                    @Override
                                    public void run() {
                                        height = temp.getHeight();
                                        width = temp.getWidth();
                                        if(rateCount>0) {
                                            for (String key : rate_percentages.keySet())
                                                setRate(key, rate_percentages.get(key));
                                        }
                                        txt_reviews_1.setText(String.format("%.02f", rate));
                                        txt_reviews_3.setText(String.valueOf(rateCount)); // android.content.res.Resources$NotFoundException: String resource ID #0x77
                                        rb_doctor_rate.setRating(rate);


                                    }
                                });

                            }

                            comments=review.getComments();
                            if(comments!=null&&comments.size()>0)
                            {
                                RecyclerView recyclerVie;
                                RateAdapter rateAdapter=   new RateAdapter(comments,RateActivity.this);
                                recyclerVie = (RecyclerView) findViewById(R.id.recycler_view);
                                recyclerVie.setHasFixedSize(true);
                                recyclerVie.setLayoutManager(new LinearLayoutManager(RateActivity.this,LinearLayoutManager.VERTICAL, false));
                                recyclerVie.setNestedScrollingEnabled(false);
                                recyclerVie.setAdapter(rateAdapter);
                            }
                            if(isClinic)
                            {

                                txt_doctor_name.setText(clinic.getName());
                                if (clinic.getAvatar() != null && !clinic.getAvatar().isEmpty()) {
                                    ImageHelper.setImage(img_chat_user_avatar, ApiHelper.SERVER_IMAGE_URL + "/" + clinic.getAvatar(), R.drawable.placeholder);
                                }
                            }else {

                                txt_doctor_name.setText(doctor.getFullName());
                                if (doctor.getAvatar() != null && !doctor.getAvatar().isEmpty()) {
                                    ImageHelper.setImage(img_chat_user_avatar, ApiHelper.SERVER_IMAGE_URL + "/" + doctor.getAvatar(), R.drawable.placeholder);
                                }
                            }


                        }
                    });

                }
                hideProgressBar();
            }
        }).start();
    }

    private void setRate(String position, int rate) {
        int result;
        result = (rate * width) / rateCount;
        boolean check=(rate * width) / rateCount==width;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(result, height);
        switch (position) {
            case "r5":
                if(!check)
                btn_five_stars.setLayoutParams(lp);
                txt_five_stars.setText(rate + "");
                break;
            case "r4":
                if(!check)
                btn_four_stars.setLayoutParams(lp);
                txt_four_stars.setText(rate + "");
                break;
            case "r3":
                if(!check)
                btn_three_stars.setLayoutParams(lp);
                txt_three_stars.setText(rate + "");
                break;
            case "r2":
                if(!check)
                btn_two_stars.setLayoutParams(lp);
                txt_two_stars.setText(rate + "");
                break;
            case "r1":
                if(!check)
                btn_one_star.setLayoutParams(lp);
                txt_one_star.setText(rate + "");
                break;
        }
    }
}
