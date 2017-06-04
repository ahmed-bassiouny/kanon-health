package com.germanitlab.kanonhealth.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.models.user.User;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by eslam on 12/29/16.
 */

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ItemView>{
    private List<User> doctorContactsList;
    Activity activity;
    int visibility ;

    public DoctorListAdapter(List<User> doctorContactsList, Activity activity , int visibility) {
        this.doctorContactsList = doctorContactsList;
        this.activity = activity;
        this.visibility = visibility ;
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_cell, parent, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(final ItemView holder, final int position) {

        final User doctor = doctorContactsList.get(position);
        holder.imgStatus.setVisibility(View.GONE);
        holder.tvDoctorName.setText(doctor.getName());
        holder.tvAbout.setText(doctor.getAbout());
        if(doctor.isChosen())
            holder.background.setBackgroundResource(R.color.dark_gray);
        holder.tvDate.setText(doctor.getLastOnline());
        if (doctor.getUnreadedMesCount() > 0) {
            holder.tvUnreadMessage.setVisibility(View.VISIBLE);
            holder.tvUnreadMessage.setText(doctor.getUnreadedMesCount());
        } else {
            holder.tvUnreadMessage.setVisibility(View.INVISIBLE);
        }
        holder.tvSubtitle.setText(doctor.getSubTitle());
        holder.imgPage.setImageResource(R.drawable.doctor_icon);

        Helper.setImage(activity ,Constants.CHAT_SERVER_URL
                + "/" + doctor.getAvatar() , holder.imgAvatar , R.drawable.profile_place_holder );
    }

    @Override
    public int getItemCount() {
        return doctorContactsList.size();
    }

    public class ItemView extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar, imgPage , imgStatus;
        TextView tvDoctorName, tvDate, tvAbout, tvSubtitle, tvUnreadMessage;
        LinearLayout background ;

        public ItemView(View itemView) {
            super(itemView);

            imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_avatar_cell);
            imgPage = (CircleImageView) itemView.findViewById(R.id.img_lable_cell);
            background =  (LinearLayout) itemView.findViewById(R.id.background);
            tvDoctorName = (TextView) itemView.findViewById(R.id.tv_doctor_name_cell);
            tvDate = (TextView) itemView.findViewById(R.id.tv_doctor_date_cell);
            tvSubtitle = (TextView) itemView.findViewById(R.id.tv_sub_title_cell);
            tvAbout = (TextView) itemView.findViewById(R.id.tv_about_doctor_cell);
            tvUnreadMessage = (TextView) itemView.findViewById(R.id.tv_unread_message_cell);
            imgStatus = (CircleImageView) itemView.findViewById(R.id.status);
        }
    }
}
