package com.germanitlab.kanonhealth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.germanitlab.kanonhealth.models.user.User;

import java.util.ArrayList;
import java.util.List;

public class CheckMutli extends AppCompatActivity {
    ProgressBar progressBar ;
    RecyclerView recyclerView ;
    List<User> userList ;
    MutliSelectAdapter mAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mutli);
        progressBar = (ProgressBar) findViewById(R.id.progress_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        userList = new ArrayList<>();
        setData();
        mAdapter = new MutliSelectAdapter(userList , getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void setData() {
        User user = new User() ;
        user.setName("Andy");
        userList.add(user);
        User user1 = new User() ;
        user1.setName("Emad");
        userList.add(user1);
        User user2 = new User() ;
        user2.setName("dsfdsf");
        userList.add(user2);
        User user3 = new User() ;
        user3.setName("sdfsdf");
        userList.add(user3);
        User user4 = new User() ;
        user4.setName("Emazxcvdxcvd");
        userList.add(user4);
        User user5 = new User() ;
        user5.setName("xczvxzcv");
        userList.add(user5);


    }

}
