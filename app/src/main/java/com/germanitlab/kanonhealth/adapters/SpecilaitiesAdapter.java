package com.germanitlab.kanonhealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.models.MembersAt;
import com.germanitlab.kanonhealth.models.SpecilaitiesModels;
import com.germanitlab.kanonhealth.models.SupportedLanguage;

import java.util.List;

/**
 * Created by Geram IT Lab on 18/05/2017.
 */

public class SpecilaitiesAdapter extends RecyclerView.Adapter<SpecilaitiesAdapter.MyViewHolder> {
    private List<?> list;
    private int visiblity;
    Context context ;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public SpecilaitiesAdapter(List<?> list, int visiblity , Context context) {
        this.list = list;
        this.context = context ;
        this.visiblity = visiblity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.specialities_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(list.getClass().isInstance(SpecilaitiesModels.class)) {
            SpecilaitiesModels specilaities = (SpecilaitiesModels) list.get(position);
            setMydata(specilaities.getName() , specilaities.getSpeciality_icon() , holder);
            holder.title.setVisibility(visiblity);
        }
        else if(list.getClass().isInstance(SupportedLanguage.class)){
            SupportedLanguage supportedLanguage = (SupportedLanguage) list.get(position);
            setMydata(supportedLanguage.getName() , supportedLanguage.getAvatar() , holder);
            holder.title.setVisibility(visiblity);
        }
        else if(list.getClass().isInstance(MembersAt.class)){
            MembersAt membersAt = (MembersAt) list.get(position);
            setMydata(membersAt.getName() , membersAt.getAvatar() , holder);
            holder.title.setVisibility(visiblity);
        }
    }
    public void setMydata(String name , String avatar ,MyViewHolder holder ){
        holder.title.setText(name);
        Helper.setImage(context , Constants.CHAT_SERVER_URL + "/"+avatar , holder.image , R.drawable.profile_place_holder);
    }


    @Override
    public int getItemCount() {
        return 10;
    }


}
