package com.germanitlab.kanonhealth;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.models.doctors.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Geram IT Lab on 22/04/2017.
 */

public class RateAdapter  extends RecyclerView.Adapter<RateAdapter.MyViewHolder>  {

    private List<User> rateList;
    private Activity activity;

    public RateAdapter(List<User> rateList , Activity activity)
    {
        this.rateList = rateList ;
        this.activity = activity;
    }

    @Override
    public RateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rate_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RateAdapter.MyViewHolder holder, int position) {
        User user = rateList.get(position);
        holder.name.setText(user.getName());
        holder.comment.setText(user.getComment());
        holder.rate.setRating(Float.valueOf(user.getRate()));
        if(user.getAvatar() != null && user.getAvatar() != "" ) {
            Picasso.with(activity).load(Constants.CHAT_SERVER_URL
                    + "/" + user.getAvatar()).into(holder.avatar);
        }
        else
            Picasso.with(activity).load(Constants.CHAT_SERVER_URL
                    + "/" + user.getAvatar()).placeholder(R.drawable.profile_place_holder)
                    .resize(80, 80).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView avatar ;
        public TextView name , comment;
        RatingBar rate;

        public MyViewHolder(View view) {
            super(view);
            avatar = (CircleImageView) view.findViewById(R.id.avatar);
            name = (TextView) view.findViewById(R.id.name);
            comment = (TextView) view.findViewById(R.id.comment);
            rate = (RatingBar) view.findViewById(R.id.rate);
        }
    }
}
