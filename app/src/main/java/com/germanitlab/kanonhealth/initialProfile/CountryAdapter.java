package com.germanitlab.kanonhealth.initialProfile;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.intro.SignupActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Geram IT Lab on 05/04/2017.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {
    HashMap<String , String> countryCode ;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CountryAdapter.MyViewHolder holder, int position) {
        String country = (new ArrayList<String>(countryCode.keySet())).get(position);
        String code = (new ArrayList<String>(countryCode.values())).get(position);
        holder.code.setText(code);
        holder.country.setText(country);
    }
    public CountryAdapter(HashMap<String , String> countryCode){
        this.countryCode = countryCode ;
    }

    @Override
    public int getItemCount() {
        return countryCode.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView country , code ;
        public MyViewHolder(final View view){
            super(view);
            country = (TextView) view.findViewById(R.id.sdd);
            code = (TextView)view.findViewById(R.id.code);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext() , SignupActivity.class);
                    intent.putExtra("country" , country.getText().toString());
                    intent.putExtra("codeC" , code.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
