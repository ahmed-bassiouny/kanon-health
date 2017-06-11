package com.germanitlab.kanonhealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.chat.ChatActivity;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class OpeningHoursActivity extends AppCompatActivity {
    @BindView(R.id.first)
    RadioButton first;
    @BindView(R.id.second)
    RadioButton second;
    @BindView(R.id.third)
    RadioButton third;
    @BindView(R.id.fourth)
    RadioButton fourth;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public static TimeTable instance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_hours);
        ButterKnife.bind(this);
        initTB();

    }

    void initTB() {
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.payment));
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @OnClick(R.id.save)
    public void save(View view) {
        if (first.isChecked()) {
            Intent intent = new Intent();
            intent.putExtra("type", 0);
            setResult(Constants.HOURS_CODE, intent);
            finish();
        } else {
            Intent intent = new Intent();
            if (second.isChecked())
                intent.putExtra("type", 1);
            else if(third.isChecked())
                intent.putExtra("type", 2);
            else if(fourth.isChecked())
                intent.putExtra("type", 3);
            setResult(RESULT_OK, intent);
            instance.finish();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
