package com.germanitlab.kanonhealth.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.DoctorProfileActivity;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.ormLite.MessageRepositry;
import com.germanitlab.kanonhealth.payment.PaymentActivity;
import com.google.gson.Gson;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by eslam on 12/29/16.
 */

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ItemView>{
    private MessageRepositry mMessageRepositry;
    private List<User> doctorContactsList;
    Activity activity;
    int visibility ;
    private boolean settedAdapter=false;
    private boolean onBind;
    List<Message> list;
    int tabPosition;


    public DoctorListAdapter(List<User> doctorContactsList, Activity activity, int visibility, int i) {
        try {
            this.doctorContactsList = doctorContactsList;
            this.activity = activity;
            this.visibility = visibility ;
            tabPosition=i;

            mMessageRepositry = new MessageRepositry(activity.getApplicationContext());
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
//        setHasStableIds(true);

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
        onBind = true;

        holder.setIsRecyclable(false);
        
                /* data base
         */

                if(tabPosition==3) {
                    list = mMessageRepositry.getAll(doctorContactsList.get(position).get_Id());

                    if (list.size() > 0) {
//                        Toast.makeText(activity, "" + list.get(list.size() - 1).getMsg(), Toast.LENGTH_SHORT).show();
                        holder.tvSpecialist.setText(list.get(list.size() - 1).getMsg()+"");
                    }else {
                        holder.tvSpecialist.setText("");
                    }
                }


        final User doctor = doctorContactsList.get(position);
        String lasseen=(doctor.getLastOnline()!="null"&&doctor.getLastOnline()!=null)?doctor.getLastOnline():"";
        if(doctor.getIs_available()!=null) {
            if (doctor.getIs_available().equals("0")) {
                final int newColor = activity.getResources().getColor(R.color.medium_grey);
                holder.imgStatus.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                holder.tvSpecialist.append("\n"+lasseen+"  "+"Close");
            } else {
                final int newColor = activity.getResources().getColor(R.color.green);
                holder.imgStatus.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                holder.tvSpecialist.append("\n"+lasseen+"  "+"Open");

            }
        }
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
//        holder.imgPage.setImageResource(R.drawable.doctor_icon);

        Helper.setImage(activity ,Constants.CHAT_SERVER_URL
                + "/" + doctor.getAvatar() , holder.imgAvatar , R.drawable.placeholder );


        if(tabPosition!=3) {
            for (int x = 0; x < doctor.getSpecialities().size(); x++) {
                ImageView image = new ImageView(activity);
//                image.setBackgroundResource(R.drawable.doctor_icon);
                int width = 60;
                int height = 60;

                Helper.setImage(activity ,Constants.CHAT_SERVER_URL
                        + "/" + doctor.getSpecialities().get(x).getSpeciality_icon() , image , R.drawable.doctor_icon );

                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
                image.setLayoutParams(parms);
                holder.linearLayoutSpecialist.addView(image);
            }
        }else {

        }
        onBind = false;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(doctor.getIsDoc() == 1) {
                    Intent intent = new Intent(activity, DoctorProfileActivity.class);
                    intent.putExtra("doctor_data", doctor);
                    intent.putExtra("tab", "");
                    activity.startActivity(intent);
                }
                else if(doctor.getIsClinic() == 1){
                    Intent intent = new Intent(activity, PaymentActivity.class);
                    intent.putExtra("doctor_data", doctor);
                    intent.putExtra("tab", "");
                    activity.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorContactsList.size();
    }

    public class ItemView extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar, imgPage , imgStatus;
        TextView tvDoctorName, tvDate, tvAbout, tvSubtitle, tvUnreadMessage,tvSpecialist;
        LinearLayout background ,linearLayoutSpecialist;


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
            tvSpecialist = (TextView) itemView.findViewById(R.id.tv_specialities);
            imgStatus = (CircleImageView) itemView.findViewById(R.id.status);
            linearLayoutSpecialist= (LinearLayout) itemView.findViewById(R.id.ll_dynamic_specialist);
        }
    }
}
