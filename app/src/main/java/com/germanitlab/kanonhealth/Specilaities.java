package com.germanitlab.kanonhealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.germanitlab.kanonhealth.adapters.FilterAdapter;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.Speciality;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geram IT Lab on 27/04/2017.
 */

public class Specilaities extends AppCompatActivity {
    private List<Speciality> specialityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FilterAdapter mAdapter;
    TypeToken<List<Speciality>> token;
    private LinearLayout toolbar;
    private Boolean from;
    private int type ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_specialities);
        token = new TypeToken<List<Speciality>>() {
        };
        toolbar = (LinearLayout) findViewById(R.id.toolbar);
        Intent intent = getIntent();
/*            String dd = savedInstanceState.getString("data");
            Log.d("my string ", dd.toString());*/
        from = intent.getBooleanExtra("from" ,true);
        type = intent.getIntExtra("type" , 0);
        try {
            boolean hide = intent.getBooleanExtra("hide" , true);
            if (hide)
                toolbar.setVisibility(View.GONE);
        } catch (Exception e) {

        }
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.e("returned msg ", json);
                specialityList = (List<Speciality>) response;
                createAdapter(specialityList, from);

            }

            @Override
            public void onFailed(String error) {
                Log.e("error in returned json", error);
            }
        }).getSpecialities((String.valueOf(AppController.getInstance().getClientInfo().getUser_id()))
                , AppController.getInstance().getClientInfo().getPassword());
    }

    public void createAdapter(List<Speciality> specialityList, Boolean from) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new FilterAdapter(specialityList, this, from , type);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
