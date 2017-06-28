package com.germanitlab.kanonhealth;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.models.doctors.Comment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Geram IT Lab on 22/04/2017.
 */

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.MyViewHolder> {

    private List<Comment> commentList;
    private Activity activity;

    public RateAdapter(List<Comment> rateList, Activity activity) {
        this.commentList = rateList;
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
        try {
            Comment comment = commentList.get(position);
            holder.txt_person_name.setText(comment.getFirst_name() + " " + comment.getLast_name());
            holder.txt_comment.setText(comment.getComment());
            holder.rb_person_rate.setRating(Float.valueOf(comment.getRate()));
            if (comment.getAvatar() != null && comment.getAvatar() != "") {
                ImageHelper.setImage(holder.img_person_image, Constants.CHAT_SERVER_URL + "/" + comment.getAvatar(), R.drawable.profile_place_holder, activity);
            }
            if (comment.getCountry_flag() != null && comment.getCountry_flag() != "") {
                ImageHelper.setImage(holder.img_country_image, Constants.CHAT_SERVER_URL + "/" + comment.getCountry_flag(), R.drawable.profile_place_holder, activity);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            Log.e("Rate Activity", "", e);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView img_person_image, img_country_image;
        public TextView txt_person_name, txt_comment;
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
