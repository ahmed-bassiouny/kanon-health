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
import com.mukesh.countrypicker.Country;

import java.util.ArrayList;
import java.util.List;
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

            mAdapter = new CountryAdapter(Country.getAllCountries(), this);
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
                    List<Country> search = new ArrayList<Country>();
                    try {
                        String temp = search_bar.getText().toString().replaceAll(Pattern.quote("+"), "");
                        for (Country e : Country.getAllCountries()) {
                            if (e.getDialCode().toLowerCase().startsWith("+" + temp)) {
                                search.add(e);
                            }
                            if (e.getName().trim().toLowerCase().startsWith(temp)) {

                                search.add(e);
                            }

                        }

                    } catch (Exception w) {
                        for (Country e : Country.getAllCountries()) {
                            Log.d(e.getName(), e.getDialCode());
                            if (e.getName().startsWith(search_bar.getText().toString().toLowerCase())) {
                                search.add(e);
                            }
                        }
                    }
                    if(s.toString().equals(""))
                    {
                        mAdapter = new CountryAdapter(Country.getAllCountries(), CountryActivty.this);
                    }else {
                        mAdapter = new CountryAdapter(search, CountryActivty.this);
                    }
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
