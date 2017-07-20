package com.germanitlab.kanonhealth.forward;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.user.User;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bassiouny on 20/07/17.
 */

public class ForwardAdapter extends RecyclerView.Adapter<ForwardAdapter.ItemView> {

    private List<User> doctorContactsList;
    Activity activity;

    public ForwardAdapter(List<User> doctorContactsList, Activity activity) {

            this.doctorContactsList = doctorContactsList;
            this.activity = activity;

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_cell, parent, false);
        return new ItemView(view);
    }


    @Override
    public void onBindViewHolder(final ItemView holder, final int position) {
        try {


            final User doctor = doctorContactsList.get(position);

            if (doctor.isClinic == 1) {
                holder.tvDoctorName.setText(doctor.getFullName());
                holder.tvPractice.setVisibility(View.GONE);
            } else {
                holder.tvDoctorName.setText(doctor.getFullName());
            }
            holder.tvDate.setText(doctor.getLastOnline());
            holder.tvSpecialist.setVisibility(View.GONE);

            holder.tvPractice.setText("");
            if (doctor.getIsDoc() == 1 && doctor.getMembers_at() != null && doctor.getMembers_at().size() > 0) {
                holder.tvPractice.setVisibility(View.VISIBLE);
                boolean isFirst = true;
                for (ChooseModel practice : doctor.getMembers_at()) {
                    if (isFirst) {
                        holder.tvPractice.setText(practice.getFirst_nameMember());
                        isFirst = false;
                    } else {
                        holder.tvPractice.append(", " + practice.getFirst_nameMember());
                    }
                }
            } else {
                holder.tvPractice.setVisibility(View.GONE);
            }
            if (doctor.getAvatar() != null && !doctor.getAvatar().isEmpty()) {
                ImageHelper.setImage(holder.imgAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + doctor.getAvatar(), activity);
            }
        }catch(Exception e){
                Crashlytics.logException(e);
                Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                Log.e("ForwardAdapter", "onBindViewHolder: ", e);
            }


    }


    @Override
    public int getItemCount() {
        return doctorContactsList.size();
    }

    public class ItemView extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar, imgStatus;
        TextView tvDoctorName, tvDate, tvSpecialist, tvPractice;
        FlowLayout linearLayoutSpecialist;


        public ItemView(View itemView) {
            super(itemView);

            imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_avatar_cell);
//            imgPage = (CircleImageView) itemView.findViewById(R.id.img_lable_cell);
            tvPractice = (TextView) itemView.findViewById(R.id.tv_practice);
            tvDoctorName = (TextView) itemView.findViewById(R.id.tv_doctor_name_cell);
            tvDate = (TextView) itemView.findViewById(R.id.tv_doctor_date_cell);
            tvSpecialist = (TextView) itemView.findViewById(R.id.tv_specialities);
            imgStatus = (CircleImageView) itemView.findViewById(R.id.status);
            linearLayoutSpecialist = (FlowLayout) itemView.findViewById(R.id.ll_dynamic_specialist);
        }
    }
}
