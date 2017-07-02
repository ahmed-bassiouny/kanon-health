package com.germanitlab.kanonhealth.initialProfile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.intro.SignupActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Geram IT Lab on 05/04/2017.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {
    HashMap<String, CountriesCodes> countryCode;
    Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CountryAdapter.MyViewHolder holder, int position) {
        try {
            CountriesCodes currentCountry = countryCode.get((new ArrayList<String>(countryCode.keySet())).get(position));
            String country = currentCountry.getName();
            String code = currentCountry.getDial_code();
            holder.code.setText(code);
            holder.country.setText(country);
            ImageHelper.setLanguageImage(holder.flag, currentCountry.getCode(), context);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public CountryAdapter(HashMap<String, CountriesCodes> countryCode, Context context) {
        this.countryCode = countryCode;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return countryCode.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView country, code;
        public ImageView flag;

        public MyViewHolder(final View view) {
            super(view);
            flag = (ImageView) view.findViewById(R.id.iv_flag);
            country = (TextView) view.findViewById(R.id.sdd);
            code = (TextView) view.findViewById(R.id.code);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(), SignupActivity.class);
                    intent.putExtra("country", country.getText().toString());
                    intent.putExtra("codeC", code.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
