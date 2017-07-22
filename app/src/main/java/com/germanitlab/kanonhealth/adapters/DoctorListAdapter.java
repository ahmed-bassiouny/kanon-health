package com.germanitlab.kanonhealth.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.httpchat.HttpChatActivity;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.ormLite.MessageRepositry;
import com.google.gson.Gson;
import com.nex3z.flowlayout.FlowLayout;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by eslam on 12/29/16.
 */

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ItemView> {
    private MessageRepositry mMessageRepositry;
    private List<User> doctorContactsList;
    Activity activity;
    int visibility;
    List<Message> list;
    int tabPosition;
    boolean is_doc = false;
    boolean is_clinic = false;

    public DoctorListAdapter(List<User> doctorContactsList, Activity activity, int visibility, int i) {
        try {
            this.doctorContactsList = doctorContactsList;
            this.activity = activity;
            this.visibility = visibility;
            tabPosition = i;
            mMessageRepositry = new MessageRepositry(activity.getApplicationContext());
            is_doc = new Gson().fromJson(new PrefManager(activity).getData(PrefManager.USER_KEY), UserInfoResponse.class).getUser().getIsDoc() == 1;
            is_clinic = new Gson().fromJson(new PrefManager(activity).getData(PrefManager.USER_KEY), UserInfoResponse.class).getUser().getIsClinic() == 1;

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("DoctorListAdapter", "DoctorListAdapter: ", e);
        }
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
            holder.setIsRecyclable(false);

                /* data base
         */
            if (tabPosition == 3) {
                list = mMessageRepositry.getAll(doctorContactsList.get(position).get_Id());
                if (list != null) {
                    if (list.size() > 0) {
                        int index = list.size() - 1;
                        if (holder.tvSpecialist != null && list.get(index).getType() != null) {
                            switch (list.get(index).getType()) {
                                case Constants.IMAGE:
                                    holder.tvSpecialist.setText(R.string.image);
                                    break;
                                case Constants.AUDIO:
                                    holder.tvSpecialist.setText(R.string.audio);
                                    break;
                                case Constants.VIDEO:
                                    holder.tvSpecialist.setText(R.string.video);
                                    break;
                                case Constants.LOCATION:
                                    holder.tvSpecialist.setText(R.string.location);
                                    break;
                                case Constants.TEXT:
                                    holder.tvSpecialist.setText(list.get(index).getMsg());
                                    break;
                            }
                        }
                    } else {
                        holder.tvSpecialist.setText("");
                    }
                }
                holder.imgStatus.setVisibility(View.GONE);
            }


            final User doctor = doctorContactsList.get(position);
            String lasseen = (doctor.getLastOnline() != "null" && doctor.getLastOnline() != null) ? doctor.getLastOnline() : "";
            if (doctor.getIs_available() != null && tabPosition != 3) {
                if (doctor.getIs_available().equals("0")) {
                    final int newColor = activity.getResources().getColor(R.color.medium_grey);
                    holder.imgStatus.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                    holder.tvSpecialist.append("\n" + lasseen + "  " + activity.getString(R.string.close));
                } else {
                    final int newColor = activity.getResources().getColor(R.color.green);
                    holder.imgStatus.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                    holder.tvSpecialist.append("\n" + lasseen + "  " + activity.getString(R.string.open));

                }
            }
            if (doctor.isClinic == 1) {
                holder.tvDoctorName.setText(doctor.getFullName());
                holder.tvPractice.setVisibility(View.GONE);
            } else {
                holder.tvDoctorName.setText(doctor.getFullName());
            }

//            holder.tvAbout.setText(doctor.getAbout());
//            if (doctor.isChosen())
//                holder.background.setBackgroundResource(R.color.dark_gray);
            holder.tvDate.setText(doctor.getLastOnline());
            if (doctor.getUnreadedMesCount() > 0) {
//                holder.tvUnreadMessage.setVisibility(View.VISIBLE);
//                holder.tvUnreadMessage.setText(doctor.getUnreadedMesCount());
            } else {
//                holder.tvUnreadMessage.setVisibility(View.INVISIBLE);
            }
//            holder.tvSubtitle.setText(doctor.getSubTitle());
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
//        holder.imgPage.setImageResource(R.drawable.doctor_icon);
            if (doctor.getAvatar() != null && !doctor.getAvatar().isEmpty()) {
                ImageHelper.setImage(holder.imgAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + doctor.getAvatar(), activity);
            }
            if (tabPosition == 3) {
                // Glide.with(activity).load(Constants.CHAT_SERVER_URL_IMAGE + "/" + doctor.getAvatar()).into(holder.imgAvatar);
                if (doctor.getIsOpen() != 1) {
                    holder.imgAvatar.setBorderColor(Color.parseColor("#cfcdcd"));
                } else if (doctor.getIsDoc() == 1) {
                    holder.imgAvatar.setBorderColor(Color.BLUE);
                } else if (doctor.getIsClinic() == 1) {
                    holder.imgAvatar.setBorderColor(Color.parseColor("#FFC0CB"));
                } else {
                    holder.imgAvatar.setBorderColor(Color.GREEN);
                }
            } else {
                holder.imgAvatar.setBorderWidth(0);
                holder.imgAvatar.setBorderOverlay(false);
                if (doctor.getSpecialities() != null && doctor.getSpecialities().size() > 0) {
                    holder.tvSpecialist.setText("");
                    for (int x = 0; x < doctor.getSpecialities().size(); x++) {
                        ImageView image = new ImageView(activity);
//                image.setBackgroundResource(R.drawable.doctor_icon);
                        int width = 60;
                        int height = 60;
                        ImageHelper.setImage(image, Constants.CHAT_SERVER_URL_IMAGE + "/" + doctor.getSpecialities().get(x).getSpeciality_icon(), activity);
                        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
                        image.setLayoutParams(parms);
                        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        holder.linearLayoutSpecialist.addView(image);
//                        if (x == 0) {
//                            holder.tvSpecialist.append(doctor.getSpecialities().get(x).getSpeciality_title());
//                        } else {
//                            holder.tvSpecialist.append(", " + doctor.getSpecialities().get(x).getSpeciality_title());
//                        }
                    }
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /****** jira task number 208 **************************/
                    if (tabPosition == 3) {
                        if (is_doc) {
                            gotoChat(doctor);

                        } else if (is_clinic) {

                        } else {
                            if (doctor.isClinic == 1 || doctor.getIsDoc() == 1) {
                                gotoChat(doctor);
                            } else {
                                //direct chat no optional to open or close it
                            }
                        }

                    } else {
                        Intent intent = new Intent(activity, DoctorProfileActivity.class);
                        intent.putExtra("doctor_data", doctor);
                        intent.putExtra("tab", "");
                        activity.startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("DoctorListAdapter", "onBindViewHolder: ", e);
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

    private void gotoChat(User doctor) {
        Gson gson = new Gson();
        Intent intent = new Intent(activity, HttpChatActivity.class);
        intent.putExtra("doctorID", doctor.get_Id());
        activity.startActivity(intent);
    }
}
