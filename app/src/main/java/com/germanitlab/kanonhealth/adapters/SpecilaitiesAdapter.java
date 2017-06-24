package com.germanitlab.kanonhealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.SpecilaitiesModels;
import com.germanitlab.kanonhealth.models.SupportedLanguage;
import com.germanitlab.kanonhealth.models.user.User;

import java.util.List;

/**
 * Created by Geram IT Lab on 18/05/2017.
 */

public class SpecilaitiesAdapter extends RecyclerView.Adapter<SpecilaitiesAdapter.MyViewHolder> {

    Context context ;
    List<ChooseModel> list;
    int type;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title ;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public SpecilaitiesAdapter(List<ChooseModel> list , Context context,int type) {
        this.context = context ;
        this.list=list;
        this.type=type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.specialities_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChooseModel chooseModel=list.get(position);
        try {
            switch (type){
                case Constants.SPECIALITIES:
                    Helper.setImage(context , Constants.CHAT_SERVER_URL_IMAGE + "/"+list.get(position).getSpeciality_icon() , holder.image , R.drawable.profile_place_holder);
                    holder.title.setVisibility(View.GONE);
                    break;
                case Constants.LANGUAUGE:
                    Helper.setImage(context , Constants.CHAT_SERVER_URL_IMAGE + "/"+list.get(position).getLang_icon() , holder.image , R.drawable.profile_place_holder);
                    holder.title.setVisibility(View.GONE);
                    break;
                case Constants.MEMBERAT:
                case Constants.DoctorAll:
                    holder.title.setText(chooseModel.getLast_nameMember()+" " +list.get(position).getFirst_nameMember());
                    Helper.setImage(context , Constants.CHAT_SERVER_URL_IMAGE + "/"+list.get(position).getAvatarMember() , holder.image , R.drawable.profile_place_holder);
                    holder.title.setVisibility(View.VISIBLE);
                    break;
            }
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("SpecilaitiesAdapter", "onBindViewHolder: ",e );
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
