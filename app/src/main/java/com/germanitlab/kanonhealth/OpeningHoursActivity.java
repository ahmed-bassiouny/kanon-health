package com.germanitlab.kanonhealth;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
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
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.Table;
import com.germanitlab.kanonhealth.models.user.User;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.germanitlab.kanonhealth.TimeTable.TimetableInstance;

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
    public static Boolean active = false;
    PrefManager prefManager ;
    List<Table> list;
    int type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_hours);
        prefManager = new PrefManager(this);
        ButterKnife.bind(this);
        list = (List<Table>) getIntent().getSerializableExtra(Constants.DATA);
        type = getIntent().getIntExtra("type", 0);
        instance = new TimeTable();
        initTB();
        addSelected(type);

    }

    private void addSelected(int type) {
        if(type == 0)
            first.setChecked(true);
        else if(type == 1)
            second.setChecked(true);
        else if(type == 2)
            third.setChecked(true);
        else if(type == 3)
            fourth.setChecked(true);
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
        Intent intent = new Intent();
        if (first.isChecked()) {
            if (TimeTable.active) {
                intent.putExtra("type", 0);
                intent.putExtra(Constants.DATA, (Serializable) list);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Intent intent1 = new Intent(this, TimeTable.class);
                intent1.putExtra(Constants.DATA , (Serializable) list);
                intent1.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent1);
                finish();
            }
        } else {
            if (second.isChecked())
                intent.putExtra("type", 1);
            else if (third.isChecked())
                intent.putExtra("type", 2);
            else if (fourth.isChecked())
                intent.putExtra("type", 3);
            intent.putExtra(Constants.DATA , (Serializable) list);
            setResult(RESULT_OK , intent);
            if (TimeTable.active)
//                instance.finish();
                TimetableInstance.finish();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
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
