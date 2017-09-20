package com.germanitlab.kanonhealth.initialProfile;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.AddPractics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.intro.SignupActivity;
import com.github.siyamed.shapeimageview.HeartImageView;
import com.mukesh.countrypicker.Country;

import java.util.List;

/**
 * Created by Geram IT Lab on 05/04/2017.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {
    List<Country> countryList;
    Activity activity;
    int from;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CountryAdapter.MyViewHolder holder, int position) {
        try {
            Country currentCountry = countryList.get(position);
            String country = currentCountry.getName();
            String code = currentCountry.getDialCode();
            holder.code.setText(code);
            holder.country.setText(country.trim());
            holder.flag.setImageResource(currentCountry.getFlag());
//            ImageHelper.setLanguageImage(holder.flag, currentCountry.getCode());
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public CountryAdapter(List<Country> countryList, Activity activity, int from) {
        this.countryList = countryList;
        this.activity = activity;
        this.from=from;
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView country, code;
        public HeartImageView flag;

        public MyViewHolder(final View view) {
            super(view);
            flag = (HeartImageView) view.findViewById(R.id.iv_flag);
            country = (TextView) view.findViewById(R.id.sdd);
            code = (TextView) view.findViewById(R.id.code);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(from==1) {
                        Intent intent = new Intent(view.getContext(), SignupActivity.class);
                        intent.putExtra("country", country.getText().toString());
                        intent.putExtra("codeC", code.getText().toString());
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                    }else
                    {
                        Intent intent = new Intent(view.getContext(), AddPractics.class);
                        intent.putExtra("country", country.getText().toString());
                        intent.putExtra("codeC", code.getText().toString());
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                    }
                }
            });
        }
    }
}
