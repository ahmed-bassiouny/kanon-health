package com.germanitlab.kanonhealth.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import com.germanitlab.kanonhealth.DoctorProfileActivity;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Clinic;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.ormLite.MessageRepositry;
import com.germanitlab.kanonhealth.widget.SquareImageView;
import com.nex3z.flowlayout.FlowLayout;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


public class ClinicListAdapter extends RecyclerView.Adapter<ClinicListAdapter.ItemView> {
    private MessageRepositry mMessageRepositry;
    private List<Clinic> clinicContactList;
    Activity activity;
    int userID;
    PrefManager prefManager;

    public ClinicListAdapter(List<Clinic> clinicContactList, Activity activity) {
        try {
            this.clinicContactList = clinicContactList;
            this.activity = activity;
            prefManager = new PrefManager(activity);
            //mMessageRepositry = new MessageRepositry(activity.getApplicationContext());
            userID = prefManager.getInt(PrefManager.USER_ID);

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("clinicListAdapter", "clinicListAdapter: ", e);
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_cell_for_contact, parent, false);
        return new ItemView(view);
    }


    @Override
    public void onBindViewHolder(final ItemView holder, final int position) {
        try {
            holder.setIsRecyclable(false);

                /* data base
                // tab poition is for tabs from 1 to 4
         */
            final Clinic clinic = clinicContactList.get(position);

            if (holder.tvDoctorName != null) {
                if (!TextUtils.isEmpty(clinic .getName()))
                    holder.tvDoctorName.setText(clinic.getName());
            }
            //----------------------------------------------------------------------------------set Specialist -------------------------//
            if (holder.tvSpecialist != null) {
                holder.tvSpecialist.setText("");
                holder.linearLayoutSpecialist.removeAllViews();
                holder.tvSpecialist.setLines(1);
                holder.tvSpecialist.setMaxLines(1);
                holder.tvSpecialist.setSingleLine(true);
                holder.tvSpecialist.setEllipsize(TextUtils.TruncateAt.END);;
            int size = 0;
            for (Speciality speciality : clinic.getSpeciality()) {
                holder.linearLayoutSpecialist.addView(ImageHelper.setImageCircle(speciality.getImage(), activity));
                holder.tvSpecialist.append(speciality.getTitle());

                if (size <  clinic.getSpeciality().size()) {
                    holder.tvSpecialist.append(", ");
                    size++;
                }
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
            }

            //--------------------------------------------------------------------------------------------------//

            if (clinic.getAvatar() != null && !clinic.getAvatar().isEmpty()) {
                ImageHelper.setImage(holder.imgAvatar, ApiHelper.SERVER_IMAGE_URL + "/" + clinic.getAvatar(), R.drawable.placeholder);
            }

            holder.imgAvatar.setBorderWidth(0);
            holder.imgAvatar.setBorderOverlay(false);


            if (clinic.getAvailable() != null) {
                if (clinic.getAvailable()==0) {
                    final int newColor = activity.getResources().getColor(R.color.medium_grey);
                    holder.imgStatus.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                } else {
                    final int newColor = activity.getResources().getColor(R.color.new_green);
                    holder.imgStatus.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                }
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /****** jira task number 208 **************************/
                    Intent intent = new Intent(activity, DoctorProfileActivity.class);
                    intent.putExtra("doctor_data", clinic);
                    intent.putExtra("tab", "");
                    activity.startActivity(intent);

                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("DoctorListAdapter", "onBindViewHolder: ", e);
        }


    }

    @Override
    public int getItemCount() {
        return clinicContactList.size();
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


}
