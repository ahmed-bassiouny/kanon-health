package com.germanitlab.kanonhealth.settingsClinics;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.user.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Geram IT Lab on 19/03/2017.
 */

public class PrcticiesSAdapter extends RecyclerView.Adapter<PrcticiesSAdapter.MyViewHolder>

{
    private Context mContext ;
    private List<ChooseModel> clinicsList;

//    LinkedHashMap<String , String> questionAnswer ;

    public PrcticiesSAdapter(Context mContext, List<ChooseModel> clinicsList)
    {
//        this.questionAnswer = questionAnswer ;
        this.mContext = mContext  ;
        this.clinicsList=clinicsList;
    }


    @Override
    public PrcticiesSAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.practice_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PrcticiesSAdapter.MyViewHolder holder, int position)
    {
        holder.tvPracticeName.setText(clinicsList.get(position).getFirst_nameMember()+"");
    }

    @Override
    public int getItemCount() {
        return clinicsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPracticeName;

        public MyViewHolder(View view) {
            super(view);
            tvPracticeName = (TextView) view.findViewById(R.id.practice_name);
        }
    }


}
