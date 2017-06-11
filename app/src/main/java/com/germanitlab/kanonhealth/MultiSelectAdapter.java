package com.germanitlab.kanonhealth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.models.SpecilaitiesModels;
import com.germanitlab.kanonhealth.models.Specialities;
import com.germanitlab.kanonhealth.models.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Geram IT Lab on 04/06/2017.
 */

public class MultiSelectAdapter extends RecyclerView.Adapter<MultiSelectAdapter.MyViewHolder> {
    Context context;

    // Edit by ahmed 9-6-2017
    private ArrayList<?> allspecialist;
    Specialities model;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public RelativeLayout relativeLayout;
        public CircleImageView avatar, checked;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            avatar = (CircleImageView) view.findViewById(R.id.avatar);
            checked = (CircleImageView) view.findViewById(R.id.check);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.multi_choise_layout);
        }
    }

    public MultiSelectAdapter(List<User> userList, List<User> chosedList, Context context) {
        //this.userList = userList;
        //this.context = context;
        //this.chosedList = chosedList;
    }
    public MultiSelectAdapter(Context context, ArrayList<?> allspecialist) {
        this.context = context;
        this.allspecialist=allspecialist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.multi_select_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //User user = userList.get(position);
        if(1==1){
             model= (Specialities)allspecialist.get(position);
            holder.name.setText(model.getTitle());
            Helper.setImage(context, Constants.CHAT_SERVER_URL + "/" + model.getAvatar(), holder.avatar, R.drawable.profile_place_holder);
            setDataChecked( holder.checked,model.is_my_specialities());


        }
        //holder.name.setText(user.getName());
        //Helper.setImage(context, Constants.CHAT_SERVER_URL + "/" + user.getAvatar(), holder.avatar, R.drawable.profile_place_holder);

        /*if (checkUser(userList.get(position))) {
            setDataChecked(holder.relativeLayout, holder.checked);
        }*/
        /*
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosedList.contains(userList.get(position)))
                    removeFromList(position ,holder.relativeLayout, holder.checked);
                else
                    addFromList(position ,holder.relativeLayout, holder.checked);
            }
        });*/
    }


/*
    private boolean checkUser(User user) {
        for (User chosedUser :chosedList
                ) {
            if(chosedUser.get_Id() == user.get_Id()) {
                chosedList.remove(chosedUser);
                chosedList.add(user);
                return true;
            }
        }
        return  false ;
    }
*/
    private void setDataChecked(CircleImageView checked,boolean show) {
            //relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.gray));
        if(show)
            checked.setVisibility(View.VISIBLE);
        else
            checked.setVisibility(View.GONE);
    }

/*
    private void removeFromList(int position, RelativeLayout relativeLayout, CircleImageView checked) {
        chosedList.remove(chosedList.indexOf(userList.get(position)));
        relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        checked.setVisibility(View.GONE);
    }
    private void addFromList(int position, RelativeLayout relativeLayout, CircleImageView checked) {
        chosedList.add(userList.get(position));
        setDataChecked(relativeLayout , checked);
    }*/

    @Override
    public int getItemCount() {
        return allspecialist.size();
    }
}
