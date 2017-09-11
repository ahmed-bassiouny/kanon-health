package com.germanitlab.kanonhealth.adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by norhan on 8/7/17.
 */

public class SelectMemberDoctorAdapter extends RecyclerView.Adapter<SelectMemberDoctorAdapter.MyViewHolder> {

    public ArrayList<UserInfo> allDoctors;
    public ArrayList<UserInfo> myDoctors;
    Context context;;


    public SelectMemberDoctorAdapter(Context context,ArrayList<UserInfo> allDoctors,ArrayList<UserInfo> myDoctors) {
        this.context = context;
        this.allDoctors = allDoctors;
        this.myDoctors = myDoctors;
    }

    @Override
    public SelectMemberDoctorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_doctor, parent, false);

        return new SelectMemberDoctorAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectMemberDoctorAdapter.MyViewHolder holder, int position) {
        try {
            UserInfo model = allDoctors.get(position);
            if(model != null) {
                int index=containsId(allDoctors.get(position).getUserID());
                if(index!=-1)
                {
                    myDoctors.remove(index);
                    myDoctors.add(allDoctors.get(position));
                    holder.rbtn.setChecked(true);
                }else
                {
                    holder.rbtn.setChecked(false);
                }


                holder.tv_title.setText(model.getFullName());
                        if (holder.img_icon != null) {
                            if (model.getAvatar()!= null && !TextUtils.isEmpty(model.getAvatar())) {
                                ImageHelper.setImage(holder.img_icon, ApiHelper.SERVER_IMAGE_URL + "/" + model.getAvatar(),R.drawable.placeholder);
                            } else {
                                holder.img_icon.setImageResource(R.drawable.placeholder);
                            }
                        }
                        if (model.getAvailable()==1)
                            holder.status.setImageResource(R.color.green);
                        else
                            holder.status.setImageResource(R.color.gray);

                holder.tv_specialities.setText("");

            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            Log.i("MultiSelect", "Adapter" + e);
        }
    }
    public  int containsId( long id) {

        for ( int i=0 ;i<myDoctors.size();i++) {
            //-----------------------------------------------------doctor id or user id ?!----------------------------------------------//
            if (myDoctors.get(i).getUserID() == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return allDoctors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public RelativeLayout relativeLayout;
        public CircleImageView img_icon, status;
        public CheckBox rbtn;
        public  TextView tv_specialities;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_specialities=(TextView) view.findViewById(R.id.tv_specilities);
            img_icon = (CircleImageView) view.findViewById(R.id.img_icon);
            status = (CircleImageView) view.findViewById(R.id.status);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.multi_choise_layout);
            rbtn = (CheckBox) view.findViewById(R.id.rbtn);
            rbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Test","Name clicked : "+getAdapterPosition());

                    if(rbtn.isChecked())
                    {
                        myDoctors.add(allDoctors.get(getAdapterPosition()));
                    }else
                    {
                        myDoctors.remove( allDoctors.get(getAdapterPosition()));
                    }

                }
            });
        }

    }
}
