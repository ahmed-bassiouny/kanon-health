package com.germanitlab.kanonhealth;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.models.doctors.*;
import com.germanitlab.kanonhealth.models.user.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.germanitlab.kanonhealth.models.doctors.Comment;

/**
 * Created by Geram IT Lab on 22/04/2017.
 */

public class RateAdapter  extends RecyclerView.Adapter<RateAdapter.MyViewHolder>  {

    private List<Comment> commentList;
    private Activity activity;

    public RateAdapter(List<Comment> rateList , Activity activity)
    {
        this.commentList = rateList ;
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
        Comment comment = commentList.get(position);
        holder.txt_person_name.setText(comment.getFirst_name()+" "+comment.getLast_name());
        holder.txt_comment.setText(comment.getComment());
        holder.rb_person_rate.setRating(Float.valueOf(comment.getRate()));
        if(comment.getAvatar() != null && comment.getAvatar() != "" ) {
            Helper.setImage(activity , Constants.CHAT_SERVER_URL
                    + "/" + comment.getAvatar() , holder.img_person_image ,R.drawable.profile_place_holder );
        }
        if(comment.getCountry_flag() != null && comment.getCountry_flag() != "" ) {
            Helper.setImage(activity , Constants.CHAT_SERVER_URL
                    + "/" + comment.getCountry_flag() , holder.img_country_image ,R.drawable.profile_place_holder );
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView img_person_image ,img_country_image;
        public TextView txt_person_name,txt_comment;
        RatingBar rb_person_rate;

        public MyViewHolder(View view) {
            super(view);
            img_person_image = (CircleImageView) view.findViewById(R.id.img_person_image);
            img_country_image = (CircleImageView) view.findViewById(R.id.img_country_image);
            txt_person_name = (TextView) view.findViewById(R.id.txt_person_name);
            txt_comment = (TextView) view.findViewById(R.id.txt_comment);
            rb_person_rate = (RatingBar) view.findViewById(R.id.rb_person_rate);
        }
    }
}
