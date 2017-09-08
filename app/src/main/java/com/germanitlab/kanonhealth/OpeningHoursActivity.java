package com.germanitlab.kanonhealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.models.Table;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    List<Table> list;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_hours);
        try {
            ButterKnife.bind(this);
            list = (List<Table>) getIntent().getSerializableExtra(Constants.DATA);
            type = getIntent().getIntExtra("type", 4);
            instance = new TimeTable();
            initTB();
            addSelected(type);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            Log.e("Opening Hours", "Activity ", e);
        }

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radios);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                Intent intent = new Intent();
                if (first.isChecked()) {
//                if (TimeTable.active) {
                    intent.putExtra("type", 3);
                    //  setResult(RESULT_OK, intent);
                    // finish();
                    //  }
                    // else {
//                    Intent intent1 = new Intent(this, TimeTable.class);
//                    intent1.putExtra(Constants.DATA, (Serializable) list);
//                    intent1.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
//                    startActivity(intent1);
//                    finish();
                    //  }
                } else {
                    if (second.isChecked())
                        intent.putExtra("type", 1);
                    else if (third.isChecked())
                        intent.putExtra("type", 4);
                    else if (fourth.isChecked())
                        intent.putExtra("type", 2);
                    // intent.putExtra(Constants.DATA, (Serializable) list);

                    // if (TimeTable.active)
//                instance.finish();
                    //   TimetableInstance.finish();
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }

    private void addSelected(int type) {
        if (type == 3)
            first.setChecked(true);
        else if (type == 1)
            second.setChecked(true);
        else if (type == 2)
            fourth.setChecked(true);
        else
            third.setChecked(true);

    }


    void initTB() {
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.payment));
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

//    @OnClick(R.id.save)
//    public void save(View view) {
//        try {
//            Intent intent = new Intent();
//            if (first.isChecked()) {
////                if (TimeTable.active) {
//                    intent.putExtra("type", 0);
//                 //  setResult(RESULT_OK, intent);
//                   // finish();
//              //  }
//               // else {
////                    Intent intent1 = new Intent(this, TimeTable.class);
////                    intent1.putExtra(Constants.DATA, (Serializable) list);
////                    intent1.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
////                    startActivity(intent1);
////                    finish();
//              //  }
//            } else {
//                if (second.isChecked())
//                    intent.putExtra("type", 1);
//                else if (third.isChecked())
//                    intent.putExtra("type", 2);
//                else if (fourth.isChecked())
//                    intent.putExtra("type", 3);
//               // intent.putExtra(Constants.DATA, (Serializable) list);
//
//               // if (TimeTable.active)
////                instance.finish();
//                 //   TimetableInstance.finish();
//            }
//            setResult(RESULT_OK, intent);
//            finish();
//        } catch (Exception e) {
//            Crashlytics.logException(e);
//            Toast.makeText(this, getResources().getText(R.string.error_saving_data), Toast.LENGTH_SHORT).show();
//            Log.e("Opening Hours", "Activity ", e);
//        }
//
//    }

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
