package com.germanitlab.kanonhealth.initialProfile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.mukesh.countrypicker.Country;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class CountryActivty extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CountryAdapter mAdapter;
    private TextView noCountryData;
    EditText search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_activty);
        try {
            search_bar = (EditText) findViewById(R.id.search_bar);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            noCountryData=(TextView) findViewById(R.id.no_country_data) ;
            sortList();
            getIntent().getIntExtra("from",0);
            mAdapter = new CountryAdapter(Country.getAllCountries(), this,getIntent().getIntExtra("from",0));
            noCountryData.setVisibility(View.GONE);
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

                    if(search.size()==0&&!s.toString().equals(""))
                    {
                        noCountryData.setVisibility(View.VISIBLE);
                    }else
                    {
                        noCountryData.setVisibility(View.GONE);
                    }

                    if(s.toString().equals(""))
                    {
                        mAdapter = new CountryAdapter(Country.getAllCountries(), CountryActivty.this,getIntent().getIntExtra("from",0));
                        noCountryData.setVisibility(View.GONE);
                    }else {
                        mAdapter = new CountryAdapter(search, CountryActivty.this,getIntent().getIntExtra("from",0));
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

    private void sortList() {
        Collections.sort(Country.getAllCountries(), new Comparator<Country>()
        {
            @Override
            public int compare(Country country , Country country2)
            {
                return country.getName().trim().compareToIgnoreCase(country2.getName().trim());
            }
        });
    }
}
