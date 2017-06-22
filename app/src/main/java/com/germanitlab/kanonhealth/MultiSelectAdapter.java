package com.germanitlab.kanonhealth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.models.ChooseModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Geram IT Lab on 04/06/2017.
 */

public class MultiSelectAdapter extends RecyclerView.Adapter<MultiSelectAdapter.MyViewHolder> {

    // Edit by ahmed 9-6-2017
    private ArrayList<ChooseModel> allspecialist;
    int type = 0;
    Context context;


    public MultiSelectAdapter(Context context, ArrayList<ChooseModel> allspecialist,int type) {
        this.context = context;
        this.allspecialist = allspecialist;
        this.type=type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (type){
            case Constants.SPECIALITIES:
            case Constants.LANGUAUGE:
                 itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_select_row, parent, false);
                break;
            case Constants.DoctorAll:
                  itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_doctor, parent, false);
                break;
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            ChooseModel model = allspecialist.get(position);
            setDataChecked(holder.rbtn, model.getIsMyChoise());
            switch (type){

                case Constants.SPECIALITIES:
                    holder.tv_title.setText(model.getSpeciality_title());
                    Helper.setImage(context, Constants.CHAT_SERVER_URL + "/" + model.getSpeciality_icon(), holder.img_icon, R.drawable.profile_place_holder);
                    break;
                case Constants.LANGUAUGE:
                    holder.tv_title.setText(model.getLang_title());
                    holder.img_icon.setVisibility(View.GONE);
                    break;
                case Constants.MEMBERAT:
                    break;
                case Constants.DoctorAll:
                    holder.tv_title.setText(model.getLast_nameMember()+" "+model.getFirst_nameMember());
                    Helper.setImage(context, Constants.CHAT_SERVER_URL + "/" + model.getAvatarMember(), holder.img_icon, R.drawable.profile_place_holder);
                    if(model.getIs_available().equals("1"))
                        holder.status.setImageResource(R.color.green);
                    else
                        holder.status.setImageResource(R.color.gray);
                    break;
            }
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            Log.i("MultiSelect", "Adapter" + e);
        }
    }


    private void setDataChecked(CheckBox dbtn, boolean show) {
        if (show)
            dbtn.setChecked(true);
        else
            dbtn.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return allspecialist.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public RelativeLayout relativeLayout;
        public CircleImageView img_icon,status;
        public CheckBox rbtn;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            img_icon = (CircleImageView) view.findViewById(R.id.img_icon);
            status = (CircleImageView) view.findViewById(R.id.status);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.multi_choise_layout);
            rbtn= (CheckBox) view.findViewById(R.id.rbtn);
        }
    }
}
