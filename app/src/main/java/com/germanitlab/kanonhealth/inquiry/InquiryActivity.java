package com.germanitlab.kanonhealth.inquiry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.germanitlab.kanonhealth.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Mo on 3/6/17.
 */

public class InquiryActivity extends AppCompatActivity implements OnChoiceSelectedListener {


    public static ArrayList<HashMap<String, String>> inquiryResult;
    public static ArrayList<String> finishedFirstLevelOptions;
    public static ArrayList<String> finishedSecondLevelOptions;

    @BindView(R.id.inquiry_container)
    FrameLayout inquiryContainer;
    String jsonData ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inquiry);
        Intent intent = getIntent() ;
        jsonData = intent.getStringExtra("doctor_data");
        if (savedInstanceState == null){
            inquiryResult = new ArrayList<>();
            finishedFirstLevelOptions = new ArrayList<>();
            finishedSecondLevelOptions = new ArrayList<>();
            getSupportFragmentManager().beginTransaction().add(R.id.inquiry_container, getInquiryMainFragment("firstTime"))
                    .addToBackStack(null).commit();
        } else {
            finishedFirstLevelOptions = savedInstanceState.getStringArrayList("finished_first_level");
            finishedSecondLevelOptions = savedInstanceState.getStringArrayList("finished_second_level");
            inquiryResult = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("inquiry_result");

            getSupportFragmentManager().beginTransaction().replace(R.id.inquiry_container, getInquiryMainFragment("comingFromSaveState"))
                    .addToBackStack(null).commit();
        }
        ButterKnife.bind(this);
    }

    @Override
    public void OnChoiceSelected(String levelName, @Nullable Fragment fragment) {
        if (fragment != null) {
            replaceFragments( fragment);
        } else {
            replaceFragments(getInquiryMainFragment(levelName));
        }
    }

    private void replaceFragments( Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction().
                replace(R.id.inquiry_container, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("finished_first_level", finishedFirstLevelOptions);
        outState.putStringArrayList("finished_second_level", finishedSecondLevelOptions);
        outState.putSerializable("inquiry_result",  inquiryResult);

        super.onSaveInstanceState(outState);
    }

    public Fragment getInquiryMainFragment (String levelName) {
        InquiryMainFragment inquiryMainFragment = new InquiryMainFragment(jsonData);
        Bundle bundle = new Bundle();

        bundle.putString(getResourceString(R.string.first_level), levelName);
        inquiryMainFragment.setArguments(bundle);
        return  inquiryMainFragment;
    }

    public String getResourceString(int resource) {
        return getResources().getString(resource);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
