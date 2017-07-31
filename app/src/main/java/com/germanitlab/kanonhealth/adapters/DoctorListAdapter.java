package com.germanitlab.kanonhealth.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
    int userID;
    PrefManager prefManager;

    public DoctorListAdapter(List<User> doctorContactsList, Activity activity, int visibility, int i) {
        try {
            this.doctorContactsList = doctorContactsList;
            this.activity = activity;
            this.visibility = visibility;
            tabPosition = i;
            prefManager = new PrefManager(activity);
            mMessageRepositry = new MessageRepositry(activity.getApplicationContext());
            is_doc = new Gson().fromJson(prefManager.getData(PrefManager.USER_KEY), UserInfoResponse.class).getUser().getIsDoc() == 1;
            is_clinic = new Gson().fromJson(prefManager.getData(PrefManager.USER_KEY), UserInfoResponse.class).getUser().getIsClinic() == 1;
            userID = prefManager.getInt(PrefManager.USER_ID);


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
        View view;
        if (tabPosition == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_cell_for_contact, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_cell_for_chat, parent, false);
        }

        return new ItemView(view);
    }


    @Override
    public void onBindViewHolder(final ItemView holder, final int position) {
        try {
            holder.setIsRecyclable(false);

                /* data base
                // tab poition is for tabs from 1 to 4
         */
            final User doctor = doctorContactsList.get(position);

            if (holder.tvDoctorName != null) {
                if (!TextUtils.isEmpty(doctor.getFullName()))
                    holder.tvDoctorName.setText(doctor.getFullName());
            }
            if (tabPosition == 3) {
                list = mMessageRepositry.getAll(userID, doctorContactsList.get(position).get_Id());
                if (list != null) {
                    if (list.size() > 0) {
                        int index = list.size() - 1;
                        if (holder.tvLastMsg != null && list.get(index).getType() != null) {
                            switch (list.get(index).getType()) {
                                case Constants.IMAGE:
                                    holder.tvLastMsg.setText(R.string.image);
                                    break;
                                case Constants.AUDIO:
                                    holder.tvLastMsg.setText(R.string.audio);
                                    break;
                                case Constants.VIDEO:
                                    holder.tvLastMsg.setText(R.string.video);
                                    break;
                                case Constants.LOCATION:
                                    holder.tvLastMsg.setText(R.string.location);
                                    break;
                                case Constants.TEXT:
                                    holder.tvLastMsg.setText(list.get(index).getMsg());
                                    break;
                            }

                        }
                        if (holder.tvLastMsgDate != null) {
                            holder.tvLastMsgDate.setText(Helper.getFormattedDate(list.get(index).getSent_at()));
                        }
                    } else {
                        holder.tvLastMsg.setText("");
                    }
                }
            }
            if (holder.tvSpecialist != null) {
                holder.tvSpecialist.setText("");
                int size = 0;
                for (ChooseModel chooseModel : doctor.getSpecialities()) {
                    if (TextUtils.isEmpty(holder.tvSpecialist.getText())) {
                        holder.tvSpecialist.append(chooseModel.getSpeciality_title());
                    } else {
                        holder.tvSpecialist.append(", " + chooseModel.getSpeciality_title());
                    }
                    // ellipsize text
                    ViewTreeObserver vto = holder.tvSpecialist.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            ViewTreeObserver obs = holder.tvSpecialist.getViewTreeObserver();
                            obs.removeGlobalOnLayoutListener(this);
                            if (holder.tvSpecialist.getLineCount() > 1) {
                                int lineEndIndex = holder.tvSpecialist.getLayout().getLineEnd(0);
                                String text = holder.tvSpecialist.getText().subSequence(0, lineEndIndex ) + "...";
                                holder.tvSpecialist.setText(text);
                            }
                        }
                    });
                    size++;

                    ImageView image = new ImageView(activity);
//                image.setBackgroundResource(R.drawable.doctor_icon);
//                        int width = holder.linearLayoutSpecialist.getHeight();
//                        int height = holder.linearLayoutSpecialist.getHeight();
                    ImageHelper.setImage(image, Constants.CHAT_SERVER_URL_IMAGE + "/" + chooseModel.getSpeciality_icon());
                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    parms.setMargins(0, 0, 0, 0);
                    image.setLayoutParams(parms);
                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    holder.linearLayoutSpecialist.addView(image);
                }
            }
            if (doctor.getAvatar() != null && !doctor.getAvatar().isEmpty()) {
                ImageHelper.setImage(holder.imgAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + doctor.getAvatar(), R.drawable.placeholder);
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

            }
            if (tabPosition == 1) {
                if (doctor.getIs_available() != null) {
                    if (doctor.getIs_available().equals("0")) {
                        final int newColor = activity.getResources().getColor(R.color.medium_grey);
                        holder.imgStatus.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                    } else {
                        final int newColor = activity.getResources().getColor(R.color.new_green);
                        holder.imgStatus.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
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
        TextView tvDoctorName, tvSpecialist, tvLastMsg, tvLastMsgDate;
        FlowLayout linearLayoutSpecialist;
        PercentRelativeLayout prlMyRow;


        public ItemView(View itemView) {
            super(itemView);

            imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_avatar_cell);
//            imgPage = (CircleImageView) itemView.findViewById(R.id.img_lable_cell);
            tvLastMsg = (TextView) itemView.findViewById(R.id.tv_last_msg);
            tvLastMsgDate = (TextView) itemView.findViewById(R.id.tv_last_msg_date);
            tvDoctorName = (TextView) itemView.findViewById(R.id.tv_doctor_name_cell);
            tvSpecialist = (TextView) itemView.findViewById(R.id.tv_specialities);
            imgStatus = (CircleImageView) itemView.findViewById(R.id.status);
            linearLayoutSpecialist = (FlowLayout) itemView.findViewById(R.id.ll_dynamic_specialist);
            prlMyRow = (PercentRelativeLayout) itemView.findViewById(R.id.myrow);
        }
    }

    private void gotoChat(User doctor) {
        Gson gson = new Gson();
        Intent intent = new Intent(activity, HttpChatActivity.class);
        intent.putExtra("doctorID", doctor.get_Id());
        activity.startActivity(intent);
    }
}
