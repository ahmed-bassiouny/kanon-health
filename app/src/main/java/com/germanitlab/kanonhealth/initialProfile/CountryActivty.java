package com.germanitlab.kanonhealth.initialProfile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.helpers.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CountryActivty extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CountryAdapter mAdapter;
    EditText search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_activty);
        try {
            search_bar = (EditText) findViewById(R.id.search_bar);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

            mAdapter = new CountryAdapter(Constants.COUNTRY_CODES, getApplicationContext());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            search_bar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    HashMap<String, CountriesCodes> search = new HashMap<String, CountriesCodes>();
                    try {
                        String temp = search_bar.getText().toString().replaceAll(Pattern.quote("+"), "");
                        if (temp == "") {
                        } else {
                            int number = Integer.parseInt(temp);
                        }
                        for (Map.Entry<String, CountriesCodes> e : Constants.COUNTRY_CODES.entrySet()) {
                            if (e.getValue().getDial_code().toLowerCase().startsWith("+" + temp)) {
                                search.put(e.getKey().toString(), e.getValue());

                            }
                        }
                    } catch (Exception w) {
                        for (Map.Entry<String, CountriesCodes> e : Constants.COUNTRY_CODES.entrySet()) {
                            Log.d(e.getKey().toLowerCase().toString(), e.getValue().toString());
                            if (e.getKey().toLowerCase().startsWith(search_bar.getText().toString().toLowerCase())) {
                                search.put(e.getKey().toString(), e.getValue());

                            }
                        }
                    }
                    mAdapter = new CountryAdapter(search, getApplicationContext());
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }
}
