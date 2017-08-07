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
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Document;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.parameters.UserInfoParameter;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.DateHelper;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.google.gson.Gson;
import com.mukesh.countrypicker.Country;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

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
    //    @BindView(R.id.img_profile_qr)
//    ImageView qr;
    @BindView(R.id.scrollView)
    ScrollView scrollView;


    private UserInfo userInfoResponse = new UserInfo();
    private PrefManager mPrefManager;

    private QuestionAdapter mAdapter;
    private DoctorDocumentAdapter mAdapter2;
    LinkedHashMap<String, String> questionAnswer;
    PrefManager prefManager;

//    public static int indexFromIntent=0;

    //    ArrayList<Document> images;
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


            mPrefManager = new PrefManager(this);
            Intent intent = getIntent();
            Boolean from = intent.getBooleanExtra("from", false);
            if (!from) {

                userInfoResponse = (UserInfo) intent.getSerializableExtra("userInfoResponse");
                mPrefManager.put(PrefManager.USER_KEY, new Gson().toJson(userInfoResponse));
                bindData();
                progressBar.setVisibility(View.GONE);
                linearProfileContent.setVisibility(View.VISIBLE);

            } else {
                tvEdit.setVisibility(View.GONE);
                if (Helper.isNetworkAvailable(getApplicationContext())) {
                    (new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserInfo userInfo = ApiHelper.getUserInfo(ProfileActivity.this, Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));
                            if (userInfo != null) {
                                try {
                                    Gson gson = new Gson();
                                    mPrefManager.put(PrefManager.USER_KEY, gson.toJson(userInfo));
                                    ProfileActivity.this.userInfoResponse = userInfo;
                                    ProfileActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            bindData();
                                            linearProfileContent.setVisibility(View.VISIBLE);
                                            tvLoadingError.setVisibility(View.GONE);
                                            tvEdit.setVisibility(View.VISIBLE);
                                        }
                                    });
                                } catch (Exception e) {
                                    Crashlytics.logException(e);
                                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvEdit.setVisibility(View.GONE);
                                        linearProfileContent.setVisibility(View.GONE);
                                        tvLoadingError.setVisibility(View.GONE);
                                    }
                                });
                            }
                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    })).run();
                } else {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();

                }

            }
        } catch (
                Exception e)

        {
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

        if (userInfoResponse.getAvatar() != null && !userInfoResponse.getAvatar().isEmpty()) {
            ImageHelper.setImage(imgAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + userInfoResponse.getAvatar());
        }

        if (userInfoResponse.getUserType() == UserInfo.DOCTOR)
            is_doctor = true;
        else
            is_doctor = false;


        tvName.setText(userInfoResponse.getFullName());
        tvPhone.setText(userInfoResponse.getCountry_code() + userInfoResponse.getPhone());

        tvBirthDate.setText(DateHelper.FromDisplayDateToBirthDateString(DateHelper.FromServerDateStringToServer(userInfoResponse.getBirthday())));


        tvStreet.setText(userInfoResponse.getStreetName());
        tvHouseNumber.setText(userInfoResponse.getHouseNumber());
        tvZipCode.setText(userInfoResponse.getZipCode());
        tvProvinz.setText(userInfoResponse.getProvidence());

        String temp = userInfoResponse.getCountry_code().replaceAll(Pattern.quote("+"), "");
        Country found = null;
        for (Country e : Country.getAllCountries()) {
            if (e.getDialCode().toLowerCase().equals("+" + temp)) {
                found = e;
            }
        }

        if (found != null) {
            Locale locale = new Locale("", found.getCode());
            if (locale != null) {
                tvCounty.setText(locale.getDisplayCountry(Locale.getDefault()));
            }
        }


        if (!is_doctor) {
            questionAnswer = userInfoResponse.getQuestionsAnswers();
//            images = userInfoResponse.getDocuments();
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
        tvEdit.setVisibility(View.VISIBLE);
        Intent i = new Intent(getApplicationContext(), EditUserProfileActivity.class);
        i.putExtra("userInfoResponse", userInfoResponse);
        startActivity(i);
        finish();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.img_profile_qr)
    public void qrcode() {
        new Helper(this).ImportQr(mPrefManager);
    }
}
