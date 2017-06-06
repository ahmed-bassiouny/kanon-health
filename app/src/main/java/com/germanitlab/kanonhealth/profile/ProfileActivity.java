package com.germanitlab.kanonhealth.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.germanitlab.kanonhealth.DoctorDocumentAdapter;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.DateUtil;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.germanitlab.kanonhealth.chat.ChatActivity.indexFromIntent;
public class ProfileActivity extends AppCompatActivity implements ApiResponse {

    @BindView(R.id.tv_profile_edit)
    TextView tvEdit;
    @BindView(R.id.tv_profile_name)
    TextView tvName;
    @BindView(R.id.tv_profile_mobile_number)
    TextView tvPhone;
    @BindView(R.id.tv_profile_birthday)
    TextView tvBirthDate;
    @BindView(R.id.tv_profile_street)
    TextView tvStreet;
    @BindView(R.id.tv_profile_houser_numebr)
    TextView tvHouseNumber;
    @BindView(R.id.tv_profile_zip_code)
    TextView tvZipCode;
    @BindView(R.id.tv_profile_provinz)
    TextView tvProvinz;
    @BindView(R.id.tv_profile_country)
    TextView tvCounty;
    @BindView(R.id.tv_profile_error)
    TextView tvLoadingError;
    @BindView(R.id.img_profile_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.linear_profile_content)
    LinearLayout linearProfileContent;
    @BindView(R.id.profile_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_view2)
    RecyclerView recyclerView2;
//    @BindView(R.id.img_profile_qr)
//    ImageView qr;
    @BindView(R.id.scrollView)
    ScrollView scrollView ;
    @BindView(R.id.video_layout)
    LinearLayout video_layout ;
    private UserInfoResponse userInfoResponse;
    private PrefManager mPrefManager ;

    private QuestionAdapter mAdapter;
    private DoctorDocumentAdapter mAdapter2;
    LinkedHashMap<String, String> questionAnswer;

//    public static int indexFromIntent=0;

    ArrayList<Message> images;
    Dialog dialog;
    boolean is_doctor ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        ButterKnife.bind(this);

        if (Helper.isNetworkAvailable(getApplicationContext())) {
            tvEdit.setVisibility(View.VISIBLE);
        }else {
            tvEdit.setVisibility(View.GONE);
        }

        mPrefManager = new PrefManager(this);
        Intent intent = getIntent();
        Boolean from = intent.getBooleanExtra("from", false);
        Log.e("Where  coming ", from.toString());
        if (!from) {

            userInfoResponse = (UserInfoResponse) intent.getSerializableExtra("userInfoResponse");
            mPrefManager.put(PrefManager.USER_KEY , new Gson().toJson(userInfoResponse));
            bindData();

            progressBar.setVisibility(View.GONE);

            linearProfileContent.setVisibility(View.VISIBLE);
        } else {
            new HttpCall(this, this).getProfile(AppController.getInstance().getClientInfo());
        }

    }

    private void bindData() {

        PrefManager prefManager = new PrefManager(this);
        if ( prefManager.getData(PrefManager.PROFILE_IMAGE) != null && prefManager.getData(PrefManager.PROFILE_IMAGE) != "" ){

            String path = prefManager.getData(PrefManager.PROFILE_IMAGE);

            Helper.setImage(this, path , imgAvatar, R.drawable.profile_place_holder);
        }

        if(userInfoResponse.getUser().getIsDoc() == 1)
            is_doctor = true;
        else
            is_doctor = false;

//        if (userInfoResponse.getUser().getQr_url() != null) {
//            Helper.ImportQr(mPrefManager, this, qr);
//        } else {
//            Helper.setImage(this,Constants.CHAT_SERVER_URL
//                    + "/" + userInfoResponse.getUser().getQr_url() , imgAvatar, R.drawable.qr);
//        }

        tvName.setText(userInfoResponse.getUser().getLast_name() + " " + userInfoResponse.getUser().getFirst_name());
        tvPhone.setText(userInfoResponse.getUser().getCountryCOde() + userInfoResponse.getUser().getPhone());
        try {
            userInfoResponse.getUser().setBirthDate(userInfoResponse.getUser().getBirth_date().toString().split("T")[0]);
        } catch (Exception e) {

        }


        try {
            Date parseDate = DateUtil.getAnotherFormat().parse(userInfoResponse.getUser().getBirth_date().toString());
            String s = (DateUtil.formatBirthday(parseDate.getTime()));
            Log.d("my converted date" ,s );
            tvBirthDate.setText(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvStreet.setText(userInfoResponse.getUser().getInfo().getStreetname());
        tvHouseNumber.setText(userInfoResponse.getUser().getInfo().getHouseNumber());
        tvZipCode.setText(userInfoResponse.getUser().getInfo().getZipCode());
        tvProvinz.setText(userInfoResponse.getUser().getInfo().getProvinz());
        tvCounty.setText(userInfoResponse.getUser().getInfo().getCountry());
        if(!is_doctor) {
            questionAnswer = userInfoResponse.getUser().getQuestions();
            images = userInfoResponse.getUser().getDocuments();
            Log.d("My question answer", (new ArrayList<String>(questionAnswer.keySet())).get(0).toString());
            createAdapter();
        }

    }


    public void createAdapter() {
        mAdapter = new QuestionAdapter(questionAnswer, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());

/*        mAdapter2 = new DoctorDocumentAdapter(images, getApplicationContext(), this , scrollView , video_layout);
        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView2.setAdapter(mAdapter2);*/
    }

    @OnClick(R.id.tv_profile_edit)
    public void editProfileOnClicked() {

        Intent i = new Intent(getApplicationContext(), EditDoctorProfileActivity.class);
        i.putExtra("userInfoResponse", userInfoResponse);
        startActivity(i);
    }

    @Override
    public void onSuccess(Object response) {

        progressBar.setVisibility(View.GONE);

        if (response != null) {

            userInfoResponse = (UserInfoResponse) response;
            Gson gson = new Gson();
            mPrefManager.put(PrefManager.USER_KEY , gson.toJson(response));
            bindData();

            linearProfileContent.setVisibility(View.VISIBLE);

        } else {
            tvLoadingError.setVisibility(View.VISIBLE);
            tvLoadingError.setText("Some thing went wrong");
        }
    }

    @Override
    public void onFailed(String error) {
        progressBar.setVisibility(View.GONE);
        tvLoadingError.setVisibility(View.VISIBLE);
        if (error != null && error.length() > 0)
            tvLoadingError.setText(error);
        else
            tvLoadingError.setText("Some thing went wrong");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "called");
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra("index" , 3);
//        startActivity(intent);

        if(getIntent().getExtras().containsKey("tab")){
            indexFromIntent = 1;

        }else {
            indexFromIntent = 3;

        }

        finish();
    }
}
