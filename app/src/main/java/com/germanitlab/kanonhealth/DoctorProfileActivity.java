package com.germanitlab.kanonhealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.germanitlab.kanonhealth.adapters.SpecilaitiesAdapter;
import com.germanitlab.kanonhealth.models.SpecilaitiesModels;
import com.germanitlab.kanonhealth.models.Table;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorProfileActivity extends AppCompatActivity {
    @BindView(R.id.speciality_recycleview)
    RecyclerView speciliatyRecycleView;
    SpecilaitiesAdapter speciliatyAdapter;
    List<SpecilaitiesModels> specilaitiesList;
    @BindView(R.id.tv_monday)
    TextView monday;
    @BindView(R.id.tv_tuesday)
    TextView tuesday;
    @BindView(R.id.tv_wednesday)
    TextView wednesday;
    @BindView(R.id.tv_thursday)
    TextView thursday;
    @BindView(R.id.tv_friday)
    TextView friday;
    @BindView(R.id.tv_saturday)
    TextView saturday;
    @BindView(R.id.tv_sunday)
    TextView sunday;
    @BindView(R.id.no_time)
    LinearLayout ll_no ;
    @BindView(R.id.monday_time)
    LinearLayout ll_monday;
    @BindView(R.id.tuesday_time)
    LinearLayout ll_tuesday;
    @BindView(R.id.wednesday_time)
    LinearLayout ll_wednesday;
    @BindView(R.id.thursday_time)
    LinearLayout ll_thursday;
    @BindView(R.id.friday_time)
    LinearLayout ll_friday;
    @BindView(R.id.saturday_time)
    LinearLayout ll_saturday;
    @BindView(R.id.sunday_time)
    LinearLayout ll_sunday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile_view);
        ButterKnife.bind(this);
        speciliatyAdapter = new SpecilaitiesAdapter(specilaitiesList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.speciality_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(speciliatyAdapter);
        String Json = "[{\"dayOfWeek\":\"1\",\"from\":\"10:00\",\"to\":\"2.24\"},{\"dayOfWeek\":\"1\",\"from\":\"11:05\",\"to\":\"15:44\"},{\"dayOfWeek\":\"2\",\"from\":\"10:44\",\"to\":\"17.50\"}]";
        getTimaTableData(Json);
    }

    private void getTimaTableData(String json) {
        List<Table> list = new Gson().fromJson(json, new TypeToken<List<Table>>() {
        }.getType());
        if(list.size() > 0)
            ll_no.setVisibility(View.GONE);
        passData(list);
    }

    private void passData(List<Table> list) {
        for (Table table : list) {
            if (table.getDayOfWeek().equals("1")) {
                setViewText(monday, table);
                ll_monday.setVisibility(View.VISIBLE);
            }
            else if (table.getDayOfWeek().equals("2")) {
                setViewText(tuesday, table);
                ll_tuesday.setVisibility(View.VISIBLE);
            }
            else if (table.getDayOfWeek().equals("3")) {
                setViewText(wednesday, table);
                ll_wednesday.setVisibility(View.VISIBLE);
            }
            else if (table.getDayOfWeek().equals("4")) {
                setViewText(thursday, table);
                ll_thursday.setVisibility(View.VISIBLE);
            }
            else if (table.getDayOfWeek().equals("5")) {
                setViewText(friday, table);
                ll_friday.setVisibility(View.VISIBLE);
            }
            else if (table.getDayOfWeek().equals("6")) {
                setViewText(saturday, table);
                ll_saturday.setVisibility(View.VISIBLE);
            }
            else if (table.getDayOfWeek().equals("7")) {
                setViewText(sunday, table);
                ll_sunday.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setViewText(TextView textView, Table table) {
        textView.setText(textView.getText() + table.getFrom() + " - " + table.getTo());
        textView.setText(textView.getText() + System.getProperty("line.separator"));
    }




}
