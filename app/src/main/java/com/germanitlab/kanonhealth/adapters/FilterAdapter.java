package com.germanitlab.kanonhealth.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.chat.MapsActivity;
import com.germanitlab.kanonhealth.doctors.DoctorListFragment;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.main.MainActivity;
import com.germanitlab.kanonhealth.models.Speciality;

/**
 * Created by Geram IT Lab on 20/03/2017.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {

    private List<Speciality> specialityList;
    private HashMap<Integer, Integer> positionId;
    private Activity activity;
    Boolean from;
    int type ;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }

    }


    public FilterAdapter(List<Speciality> specialityList, Activity activity, Boolean from , int type) {
        this.specialityList = specialityList;
        this.activity = activity;
        this.from = from;
        this.type = type ;
        positionId = new HashMap<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_row_activity, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Speciality speciality = specialityList.get(position);
        holder.title.setText(speciality.getSpeciality_title());
        positionId.put(position, speciality.getSpeciality_id());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from)
                {
                    // from map
                    int id = positionId.get(position);
                    Intent intent = new Intent(activity , MapsActivity.class);
                    intent.putExtra("speciality_id",id);
                    intent.putExtra("type", type);
                    intent.putExtra("from_map", true);
                    activity.startActivity(intent);
                }
                else {
                    //from main activity
                    int id = positionId.get(position);
                    Intent intent = new Intent(activity , MainActivity.class);
                    intent.putExtra("speciality_id", id);
                    intent.putExtra("type", type);
                    intent.putExtra("index", -1);
                    activity.startActivity(intent);

                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return specialityList.size();
    }
}

