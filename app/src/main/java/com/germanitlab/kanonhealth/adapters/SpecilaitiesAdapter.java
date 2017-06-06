package com.germanitlab.kanonhealth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.models.SpecilaitiesModels;

import java.util.List;

/**
 * Created by Geram IT Lab on 18/05/2017.
 */

public class SpecilaitiesAdapter extends RecyclerView.Adapter<SpecilaitiesAdapter.MyViewHolder> {
    private List<SpecilaitiesModels> specilaitiesList;
    private int visiblity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public SpecilaitiesAdapter(List<SpecilaitiesModels> specilaitiesList, int visiblity) {
        this.specilaitiesList = specilaitiesList;
        this.visiblity = visiblity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.specialities_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
/*        SpecilaitiesModels specilaities = specilaitiesList.get(position);
        holder.title.setText(specilaities.getName());*/
        holder.title.setVisibility(visiblity);
    }

    @Override
    public int getItemCount() {
        return 10;
    }


}
