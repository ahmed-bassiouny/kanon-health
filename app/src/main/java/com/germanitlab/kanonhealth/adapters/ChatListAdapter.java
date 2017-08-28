
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
        import com.germanitlab.kanonhealth.api.models.UserInfo;
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

    public ChatListAdapter(List<ChatModel> chatList, Activity activity) {
        try {
            this.chatList = chatList;
            this.activity = activity;
            try {
                user = new Gson().fromJson(PrefHelper.get(activity,PrefHelper.KEY_USER_KEY,""), UserInfo.class);
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_cell_for_chat, parent, false);

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


//            if (holder.tvSpecialist != null) {
//                holder.tvSpecialist.setText("");
//                int size = 0;
//                for (ChooseModel chooseModel : doctor.getSpecialities()) {
//                    if (TextUtils.isEmpty(holder.tvSpecialist.getText())) {
//                        holder.tvSpecialist.append(chooseModel.getSpeciality_title());
//                    } else {
//                        holder.tvSpecialist.append(", " + chooseModel.getSpeciality_title());
//                    }
////                    // ellipsize text
////                    ViewTreeObserver vto = holder.tvSpecialist.getViewTreeObserver();
////                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
////
////                        @Override
////                        public void onGlobalLayout() {
////                            ViewTreeObserver obs = holder.tvSpecialist.getViewTreeObserver();
////                            obs.removeGlobalOnLayoutListener(this);
////                            if (holder.tvSpecialist.getLineCount() > 1) {
////                                int lineEndIndex = holder.tvSpecialist.getLayout().getLineEnd(0);
////                                String text = holder.tvSpecialist.getText().subSequence(0, lineEndIndex - 3 ) + "...";
////                                holder.tvSpecialist.setText(text);
////                            }
////                        }
////                    });
//
//                    size++;
//
//                    SquareImageView image = new SquareImageView(activity);
////                image.setBackgroundResource(R.drawable.doctor_icon);
////                        int width = holder.linearLayoutSpecialist.getHeight();
////                        int height = holder.linearLayoutSpecialist.getHeight();
//                    ImageHelper.setImage(image, Constants.CHAT_SERVER_URL_IMAGE + "/" + chooseModel.getSpeciality_icon());
//                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    parms.setMargins(5, 0, 5, 0);
//                    image.setLayoutParams(parms);
//                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    holder.linearLayoutSpecialist.addView(image);
//                }
//                holder.tvSpecialist.setLines(1);
//                holder.tvSpecialist.setMaxLines(1);
//                holder.tvSpecialist.setSingleLine(true);
//                holder.tvSpecialist.setEllipsize(TextUtils.TruncateAt.END);
//            }
            if (chatModel.getAvatar() != null && !chatModel.getAvatar().isEmpty()) {
                ImageHelper.setImage(holder.imgAvatar, ApiHelper.SERVER_IMAGE_UPLOADS + "/" + chatModel.getAvatar(), R.drawable.placeholder);
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

    private void gotoChat(ChatModel doctor) {
        Intent intent = new Intent(activity, HttpChatActivity.class);
        intent.putExtra("userInfo",doctor);
        intent.putExtra("doctorID", doctor.getUserID());
        activity.startActivity(intent);
    }




}
