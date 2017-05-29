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
import android.widget.TextView;

import java.util.List;

import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RateActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RateAdapter mAdapter;
    private List<User> users;
    private TextView errorText;
    private CircleImageView avatar ;
    private TextView doctor_name ;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id" , 0);
        String name = intent.getStringExtra("name" );
        String avatar_path = intent.getStringExtra("avatar" );
        avatar =(CircleImageView) findViewById(R.id.avatar);
        doctor_name = (TextView) findViewById(R.id.doctor_name);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if(avatar_path != null && avatar_path!= "" ) {
            Picasso.with(this).load(Constants.CHAT_SERVER_URL
                    + "/" + avatar_path).into(avatar);
        }
        else
            Picasso.with(this).load(Constants.CHAT_SERVER_URL
                    + "/" + avatar_path).placeholder(R.drawable.profile_place_holder)
                    .resize(80, 80).into(avatar);
        doctor_name.setText(name);
        errorText = (TextView) findViewById(R.id.error);
        showProgressDialog();
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                    users = (List<User>) response;
                if(users.size() <1)
                {

                }
                else {
                    mAdapter = new RateAdapter(users, RateActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    dismissProgressDialog();
                }
            }
            @Override
            public void onFailed(String error)
            {
                dismissProgressDialog();
                Log.e("My error ", error.toString());
                recyclerView.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
            }
        }).getrating(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                , AppController.getInstance().getClientInfo().getPassword() , String.valueOf(id));



    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(getApplicationContext(), "", getResources().getString(R.string.waiting_text), true);
    }

}
