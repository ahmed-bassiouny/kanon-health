package com.germanitlab.kanonhealth.signUp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;


/**
 * Created by eslam on 10/26/16.
 */
public class SpinnerCustomAdapter extends BaseAdapter {

    Context context;
    String[] countryNames;
    LayoutInflater inflter;

    public SpinnerCustomAdapter(Context applicationContext, String[] countryNames) {
        this.context = applicationContext;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            view = inflter.inflate(R.layout.spinner_layout, null);

            TextView names = (TextView) view.findViewById(R.id.tv_spinner_layout_county_title);
            names.setText(countryNames[i]);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


        return view;
    }
}
