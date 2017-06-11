package com.germanitlab.kanonhealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    private int visiblity;
    Context context ;
    List<ChooseModel> list;
    int type;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title , name;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            name = (TextView) view.findViewById(R.id.name);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public SpecilaitiesAdapter(List<ChooseModel> list, int visiblity , Context context,int type) {
        this.context = context ;
        this.visiblity = visiblity;
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
        switch (type){
            case Constants.SPECIALITIES:
                holder.title.setText(chooseModel.getSpeciality_title());
                Helper.setImage(context , Constants.CHAT_SERVER_URL + "/"+list.get(position).getSpeciality_icon() , holder.image , R.drawable.profile_place_holder);
                holder.name.setVisibility(View.GONE);
                holder.title.setVisibility(View.VISIBLE);
                break;
            case Constants.LANGUAUGE:
                holder.title.setText(chooseModel.getLang_title());
                Helper.setImage(context , Constants.CHAT_SERVER_URL + "/"+list.get(position).getLang_icon() , holder.image , R.drawable.profile_place_holder);
                holder.name.setVisibility(View.GONE);
                holder.title.setVisibility(View.VISIBLE);
                break;
            case Constants.MEMBERAT:
                holder.name.setText(chooseModel.getLast_nameMember()+" " +list.get(position).getFirst_nameMember());
                Helper.setImage(context , Constants.CHAT_SERVER_URL + "/"+list.get(position).getAvatarMember() , holder.image , R.drawable.profile_place_holder);
                holder.name.setVisibility(View.VISIBLE);
                holder.title.setVisibility(View.GONE);
                break;
        }
        /*holder.title.setVisibility(visiblity);
        if(visiblity == View.VISIBLE)
        holder.name.setVisibility(visiblity==View.VISIBLE ? View.GONE : View.VISIBLE);*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
