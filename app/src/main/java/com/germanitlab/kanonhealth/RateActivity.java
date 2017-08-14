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
import com.germanitlab.kanonhealth.api.models.Review;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.responses.UserInfoResponse;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.models.doctors.Comment;
import com.germanitlab.kanonhealth.ormLite.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RateActivity extends AppCompatActivity {

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
    int height, width, sum_rate_number;
    float sum_rate_result, rate_result;

    UserInfo doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        ButterKnife.bind(this);
         doctor = new UserInfo();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        try {
            recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            rate_percentages = new HashMap<>();
            rate_percentages.put("r1", 22);
            rate_percentages.put("r2", 33);
            rate_percentages.put("r3", 44);
            rate_percentages.put("r4", 2);
            rate_percentages.put("r5", 18);

            doc_id = getIntent().getStringExtra("doc_id");
            if (doc_id.isEmpty()) {
                finish();
            }
            loadData();

            doctor.setUserID(Integer.valueOf(doc_id));
            txt_doctor_name.setText(doctor.getFullName());
            if (doctor.getAvatar() != null && !doctor.getAvatar().isEmpty()) {
                ImageHelper.setImage(img_chat_user_avatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + doctor.getAvatar());
            }

            txt_one_star.post(new TimerTask() {
                @Override
                public void run() {

                    height = temp.getHeight();
                    width = temp.getWidth();
                    for (String key : rate_percentages.keySet())
                        setRate(key, rate_percentages.get(key));
                    rate_result = (sum_rate_result / sum_rate_number);
                    txt_reviews_1.setText(String.format("%.02f", rate_result));
                    txt_reviews_3.setText(String.valueOf(sum_rate_number)); // android.content.res.Resources$NotFoundException: String resource ID #0x77
                    rb_doctor_rate.setRating(rate_result);

                }
            });
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
                Review review= ApiHelper.getReview(doc_id,RateActivity.this);
                if(review!=null){

                }
            }
        }).start();
    }

    private void setRate(String position, int rate) {
        int result;
        result = (rate * width) / 100;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(result, height);
        switch (position) {
            case "r5":
                btn_five_stars.setLayoutParams(lp);
                txt_five_stars.setText(rate + "");
                sum_rate_result += rate * 5;
                break;
            case "r4":
                btn_four_stars.setLayoutParams(lp);
                txt_four_stars.setText(rate + "");
                sum_rate_result += rate * 4;
                break;
            case "r3":
                btn_three_stars.setLayoutParams(lp);
                txt_three_stars.setText(rate + "");
                sum_rate_result += rate * 3;
                break;
            case "r2":
                btn_two_stars.setLayoutParams(lp);
                txt_two_stars.setText(rate + "");
                sum_rate_result += rate * 2;
                break;
            case "r1":
                btn_one_star.setLayoutParams(lp);
                txt_one_star.setText(rate + "");
                sum_rate_result += rate * 1;
                break;
        }
        sum_rate_number += rate;
    }
}
