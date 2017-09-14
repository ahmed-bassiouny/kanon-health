
package com.germanitlab.kanonhealth.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.responses.IsOpenResponse;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.httpchat.HttpChatActivity;
import com.google.gson.Gson;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ItemView> {

    private List<ChatModel> chatList;
    Activity activity;
    UserInfo user;
    int type;

    public ChatListAdapter(List<ChatModel> chatList, Activity activity, int type) {
        try {
            this.chatList = chatList;
            this.activity = activity;
            this.type = type;
            try {
                user = new Gson().fromJson(PrefHelper.get(activity, PrefHelper.KEY_USER_KEY, ""), UserInfo.class);
            } catch (Exception e) {
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("CHATListAdapter", "CHATListAdapter: ", e);
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
        if (type == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_cell_for_three_rows, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_cell_for_chat, parent, false);
        }


        return new ItemView(view);
    }


    @Override
    public void onBindViewHolder(final ItemView holder, final int position) {
        try {
            holder.setIsRecyclable(false);
            final ChatModel chatModel = chatList.get(position);

            if (holder.tvDoctorName != null) {
                if (!TextUtils.isEmpty(chatModel.getFullName()))
                    holder.tvDoctorName.setText(chatModel.getFullName());
            }

            if (holder.tvLastMsg != null) {
                switch (chatModel.getType()) {
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
                        holder.tvLastMsg.setText(chatModel.getMessage());
                        break;
                }

                if (holder.tvLastMsgDate != null) {
                    holder.tvLastMsgDate.setText(chatModel.getTime());
                }
            } else {
                holder.tvLastMsg.setText("");
            }
            if (chatModel.getAvatar() != null && !chatModel.getAvatar().isEmpty()) {
                ImageHelper.setImage(holder.imgAvatar, ApiHelper.SERVER_IMAGE_URL + chatModel.getAvatar(), R.drawable.placeholder);
            }

            if (chatModel.getIsSessionOpen() != 1) {
                holder.imgAvatar.setBorderColor(Color.parseColor("#cfcdcd"));
            } else if (chatModel.getUserType() == user.DOCTOR) {
                holder.imgAvatar.setBorderColor(Color.BLUE);
            } else if (chatModel.getUserType() == user.CLINIC) {
                holder.imgAvatar.setBorderColor(Color.parseColor("#FFC0CB"));
            } else {
                holder.imgAvatar.setBorderColor(Color.GREEN);
            }
            if (type != 1) {
                if (chatModel.getSpecialities() != null && chatModel.getSpecialities().size()>0) {
                    holder.tvSpecialist.setText("");
                    holder.linearLayoutSpecialist.removeAllViews();
                    int size = 0;
                    for (Speciality speciality : chatModel.getSpecialities()) {
                        holder.linearLayoutSpecialist.addView(ImageHelper.setImageCircleSpecial(speciality.getImage(), activity));
                        holder.tvSpecialist.append(speciality.getTitle());
                        size++;
                        if (size < chatModel.getSpecialities().size()) {
                            holder.tvSpecialist.append(", ");
                        }
                    }
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoChat(chatModel);
                }

            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("cHATListAdapter", "onBindViewHolder: ", e);
        }


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ItemView extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar, imgStatus;
        TextView tvDoctorName, tvSpecialist, tvLastMsg, tvLastMsgDate;
        FlowLayout linearLayoutSpecialist;
        PercentRelativeLayout prlMyRow;


        public ItemView(View itemView) {
            super(itemView);

            imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_avatar_cell);
            tvLastMsg = (TextView) itemView.findViewById(R.id.tv_last_msg);
            tvLastMsgDate = (TextView) itemView.findViewById(R.id.tv_last_msg_date);
            tvDoctorName = (TextView) itemView.findViewById(R.id.tv_doctor_name_cell);
            tvSpecialist = (TextView) itemView.findViewById(R.id.tv_specialities);
            imgStatus = (CircleImageView) itemView.findViewById(R.id.status);
            linearLayoutSpecialist = (FlowLayout) itemView.findViewById(R.id.ll_dynamic_specialist);
            prlMyRow = (PercentRelativeLayout) itemView.findViewById(R.id.myrow);

        }
    }

    private void gotoChat(final ChatModel doctor) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //*********** it's comment becuase i am waiting karim finish task
                final IsOpenResponse result = ApiHelper.getIsOpen(PrefHelper.get(activity, PrefHelper.KEY_USER_ID, -1), doctor.getUserID(), doctor.getUserType());

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.getStatus() == 1) {
                            doctor.setIsSessionOpen(1);
                            doctor.setRequestID(result.getRequestId());
                        } else {
                            doctor.setIsSessionOpen(0);
                        }
                        Intent intent = new Intent(activity, HttpChatActivity.class);
                        intent.putExtra("userInfo", doctor);
                        intent.putExtra("doctorID", doctor.getUserID());
                        activity.startActivity(intent);
                    }
                });
            }
        }).start();

    }


}
