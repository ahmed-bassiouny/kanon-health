package com.germanitlab.kanonhealth.profile;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.DoctorDocumentAdapter;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.DateUtil;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.models.user.UserRegisterResponse;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    ScrollView scrollView;

    private UserInfoResponse userInfoResponse = new UserInfoResponse();
    private PrefManager mPrefManager;

    private QuestionAdapter mAdapter;
    private DoctorDocumentAdapter mAdapter2;
    LinkedHashMap<String, String> questionAnswer;
    PrefManager prefManager;

//    public static int indexFromIntent=0;

    ArrayList<Message> images;
    Dialog dialog;
    boolean is_doctor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        ButterKnife.bind(this);
        initTB();
        prefManager = new PrefManager(this);
        try {
            if (Helper.isNetworkAvailable(getApplicationContext())) {
                tvEdit.setVisibility(View.VISIBLE);
            } else {
                tvEdit.setVisibility(View.GONE);
            }

            mPrefManager = new PrefManager(this);
            Intent intent = getIntent();
            Boolean from = intent.getBooleanExtra("from", false);
            if (!from) {

                userInfoResponse = (UserInfoResponse) intent.getSerializableExtra("userInfoResponse");
                mPrefManager.put(PrefManager.USER_KEY, new Gson().toJson(userInfoResponse));
                bindData();
                progressBar.setVisibility(View.GONE);
                linearProfileContent.setVisibility(View.VISIBLE);
            } else {
                UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
                userRegisterResponse.setUser_id(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));
                userRegisterResponse.setPassword(prefManager.getData(PrefManager.USER_PASSWORD));
                new HttpCall(this, this).getProfile(userRegisterResponse);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }


    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:

                finish();
                break;

        }

        return super.onOptionsItemSelected(item);

    }


    private void initTB() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }


    private void bindData() {

        if (userInfoResponse.getUser().getAvatar() != null && !userInfoResponse.getUser().getAvatar().isEmpty()) {
            ImageHelper.setImage(imgAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + userInfoResponse.getUser().getAvatar(), this);
        }

        if (userInfoResponse.getUser().getIsDoc() == 1)
            is_doctor = true;
        else
            is_doctor = false;


        tvName.setText(userInfoResponse.getUser().getLast_name() + " " + userInfoResponse.getUser().getFirst_name());
        tvPhone.setText(userInfoResponse.getUser().getCountryCOde() + userInfoResponse.getUser().getPhone());
        try {
            userInfoResponse.getUser().setBirthDate(userInfoResponse.getUser().getBirth_date().toString());
        } catch (Exception e) {

        }


        try {
            Date parseDate = DateUtil.getAnotherFormat().parse(userInfoResponse.getUser().getBirth_date().toString());
            String s = (DateUtil.formatBirthday(parseDate.getTime()));
            Log.d("my converted date", s);
            tvBirthDate.setText(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvStreet.setText(userInfoResponse.getUser().getInfo().getStreetname());
        tvHouseNumber.setText(userInfoResponse.getUser().getInfo().getHouseNumber());
        tvZipCode.setText(userInfoResponse.getUser().getInfo().getZipCode());
        tvProvinz.setText(userInfoResponse.getUser().getInfo().getProvinz());
        tvCounty.setText(userInfoResponse.getUser().getInfo().getCountry());
        if (!is_doctor) {
            questionAnswer = userInfoResponse.getUser().getQuestions();
            images = userInfoResponse.getUser().getDocuments();
            createAdapter();
        }

    }


    public void createAdapter() {
        mAdapter = new QuestionAdapter(questionAnswer, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @OnClick(R.id.tv_profile_edit)
    public void editProfileOnClicked() {

        Intent i = new Intent(getApplicationContext(), EditDoctorProfileActivity.class);
        i.putExtra("userInfoResponse", userInfoResponse);
        startActivity(i);
        finish();
    }

    @Override
    public void onSuccess(Object response) {

        try {
            progressBar.setVisibility(View.GONE);

            if (response != null) {

                userInfoResponse = (UserInfoResponse) response;
                Gson gson = new Gson();
                mPrefManager.put(PrefManager.USER_KEY, gson.toJson(response));
                bindData();

                linearProfileContent.setVisibility(View.VISIBLE);

            } else {
                tvLoadingError.setVisibility(View.VISIBLE);
                tvLoadingError.setText("Some thing went wrong");
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
