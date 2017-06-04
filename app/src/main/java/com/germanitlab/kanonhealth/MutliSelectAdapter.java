package com.germanitlab.kanonhealth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.models.user.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Geram IT Lab on 04/06/2017.
 */

public class MutliSelectAdapter extends RecyclerView.Adapter<MutliSelectAdapter.MyViewHolder> {
    private List<User> userList = new ArrayList<>();;
    private List<User> chosedList = new ArrayList<>(); ;
    Context context ;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public RelativeLayout linearLayout ;
        public CircleImageView avatar , checked ;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            avatar = (CircleImageView) view.findViewById(R.id.avatar);
            checked = (CircleImageView) view.findViewById(R.id.check);
            linearLayout = (RelativeLayout) view.findViewById(R.id.multi_choise_layout);
        }
    }
    public MutliSelectAdapter(List<User> userList , Context context) {
        this.userList = userList;
        this.context = context ;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.multi_select_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        User user = userList.get(position);
        holder.name.setText(user.getName());
        Helper.setImage(context , Constants.CHAT_SERVER_URL + "/" + user.getAvatar() , holder.avatar , R.drawable.profile_place_holder);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosedList.contains(userList.get(position)))
                    removeFromList(position ,holder.linearLayout , holder.checked);
                else
                    addFromList(position ,holder.linearLayout , holder.checked);
            }
        });
    }

    private void removeFromList(int position, RelativeLayout relativeLayout, CircleImageView checked) {
        chosedList.remove(chosedList.indexOf(userList.get(position)));
        relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        checked.setVisibility(View.GONE);
    }
    private void addFromList(int position, RelativeLayout relativeLayout, CircleImageView checked) {
        chosedList.add(userList.get(position));
        relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.gray));
        checked.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
