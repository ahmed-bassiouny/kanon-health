package com.germanitlab.kanonhealth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.chat.ChatActivity;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.Info;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.ormLite.UserRepository;
import com.germanitlab.kanonhealth.payment.PreRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

// Edit By Ahmed 29-5-2017
public class DoctorProfile extends AppCompatActivity {
    @BindView(R.id.contact)
    Button contact;
    @BindView(R.id.first_name)
    TextView first_name;
    @BindView(R.id.last_name)
    TextView last_name;
    @BindView(R.id.street_name)
    TextView street_name;
    @BindView(R.id.house_number)
    TextView house_number;
    @BindView(R.id.zip)
    TextView zip;
    @BindView(R.id.provinz)
    TextView provinz;
    @BindView(R.id.country)
    TextView country;
    /*    @BindView(R.id.textts)
        TextView hot_line;*/
    @BindView(R.id.fax)
    TextView fax;
    @BindView(R.id.website)
    TextView website;
    @BindView(R.id.services)
    TextView services;
    @BindView(R.id.rate)
    LinearLayout rate;
    User doctor;
    String doctorJson;
    PrefManager mPrefManager;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.img_profile_avatar)
    CircleImageView imgAvatar;
    ProgressDialog progressDialog;
    private DoctorDocumentAdapter doctorDocumentAdapter;
    /*    @BindView(R.id.video_layout)
        LinearLayout video_layout;*/
    @BindView(R.id.profile_layout)
    ScrollView profile_layout;
    private UserRepository mDoctorRepository;

    @BindView(R.id.add_to_my_doctor)
    Button add_to_my_doctor;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile);
        mPrefManager = new PrefManager(this);
        ButterKnife.bind(this);
        util = Util.getInstance(this);
        util.showProgressDialog();
        Intent intent = getIntent();
        Gson gson = new Gson();
        mDoctorRepository = new UserRepository(getApplicationContext());
        doctorJson = intent.getStringExtra("doctor_data");
        doctor = gson.fromJson(intent.getStringExtra("doctor_data"), User.class);
        if (Helper.isNetworkAvailable(getApplicationContext())) {
            getDoctorData();
        } else {
            doctor = mDoctorRepository.getDoctor(doctor);
            doctor.setInfo(gson.fromJson(doctor.getJsonInfo(), Info.class));
            ArrayList<Message> dos = gson.fromJson(doctor.getJsonDocument(), new TypeToken<ArrayList<Message>>() {
            }.getType());
            if (dos != null)
                doctor.setDocuments(dos);
            bindData();
            util.dismissProgressDialog();
            checkDoctor();
        }

    }

    private void getDoctorData() {
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                Gson gson = new Gson();
                doctorJson = gson.toJson(response);
                UserInfoResponse userInfoResponse = (UserInfoResponse) response;
                doctor = userInfoResponse.getUser();
                util.dismissProgressDialog();
                mDoctorRepository.insertDocument(doctor);
                bindData();
                checkDoctor();
            }

            @Override
            public void onFailed(String error) {
                util.dismissProgressDialog();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            }
        }).getDoctorId("2", AppController.getInstance().getClientInfo().getPassword(), String.valueOf(doctor.get_Id()));
    }


    private void bindData() {
        first_name.setText(doctor.getFirst_name());
        last_name.setText(doctor.getLast_name());
        street_name.setText(doctor.getAddress());
        house_number.setText(doctor.getInfo().getHouseNumber());
        zip.setText(doctor.getInfo().getZipCode());
        provinz.setText(doctor.getInfo().getProvinz());
        country.setText(doctor.getInfo().getCountry());
        Log.e("returned image :", doctor.getAvatar());
        Helper.setImage(this, Constants.CHAT_SERVER_URL
                + "/" + doctor.getAvatar(), imgAvatar, R.drawable.profile_place_holder);
        if (doctor.getDocuments() != null)
            createAdapter();
    }

    @OnClick(R.id.contact)
    public void contactClick(View v) {
        if (mPrefManager.get(mPrefManager.IS_DOCTOR) || doctor.getIsOpen() == 1) {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("doctor_data", doctorJson);
            intent.putExtra("from", true);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PreRequest.class);
            intent.putExtra("doctor_data", doctorJson);
            startActivity(intent);
        }

    }

    @OnClick(R.id.rate)
    public void rateClick(View v) {
        Intent intent = new Intent(this, RateActivity.class);
        intent.putExtra("id", doctor.get_Id());
        intent.putExtra("name", doctor.getName());
        intent.putExtra("avater", doctor.getAvatar());
        startActivity(intent);
    }

    private void createAdapter() {
        Gson gson = new Gson();
        ArrayList<Message> messages = doctor.getDocuments();
        doctorDocumentAdapter = new DoctorDocumentAdapter(messages, getApplicationContext(), this, profile_layout);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(doctorDocumentAdapter);
    }

    @OnClick(R.id.add_to_my_doctor)
    public void addToMyDoctor() {
        if (doctor.getIs_my_doctor().equals("0")) {
            new HttpCall(this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    Log.i("Answers ", response.toString());
                    doctor.setIs_my_doctor("1");
                    checkDoctor();
                }

                @Override
                public void onFailed(String error) {
                    Log.i("Error ", " " + error);
                }
            }).addToMyDoctor(doctor.get_Id() + "");
        } else if (doctor.getIs_my_doctor().equals("1")) {
            new HttpCall(this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    Log.i("Answers ", response.toString());
                    doctor.setIs_my_doctor("0");
                    checkDoctor();
                }

                @Override
                public void onFailed(String error) {
                    Log.i("Error ", " " + error);
                }
            }).removeFromMyDoctor(doctor.get_Id() + "");
        } else {
            Toast.makeText(this, "Something Wrong Happpened", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkDoctor() {
        try {
            if (doctor.getIs_my_doctor().equals("0"))
                add_to_my_doctor.setText(getString(R.string.add_to));
            else if (doctor.getIs_my_doctor().equals("1"))
                add_to_my_doctor.setText(getString(R.string.remove_from));
        } catch (Exception e) {
        }

    }
}
