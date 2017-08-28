package com.germanitlab.kanonhealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.mukesh.countrypicker.Country;

import java.util.List;

/**
 * Created by Geram IT Lab on 18/05/2017.
 */

public class SpecilaitiesAdapter extends RecyclerView.Adapter<SpecilaitiesAdapter.MyViewHolder> {

    Context context;
    List<ChooseModel> list;
    int type;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public SpecilaitiesAdapter(List<ChooseModel> list, Context context, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.specialities_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            switch (type) {
                case Constants.SPECIALITIES:
                    ImageHelper.setImage(holder.image, ApiHelper.SERVER_IMAGE_UPLOADS + list.get(position).getSpeciality_icon());
                    holder.title.setVisibility(View.GONE);
                    break;
                case Constants.LANGUAUGE:
                    if (!TextUtils.isEmpty(list.get(position).getLong_short())) {
                        Country temp = Country.getCountryByISO(list.get(position).getLong_short());
                        if (temp != null) {
                            holder.image.setImageResource(temp.getFlag());
                        } else {
                            holder.image.setImageDrawable(null);
                        }
                    }
                    holder.title.setVisibility(View.GONE);
                    break;
                case Constants.MEMBERAT:
                case Constants.DoctorAll:
                    holder.title.setText(list.get(position).getFirst_nameMember());
                    ImageHelper.setImage(holder.image, ApiHelper.SERVER_IMAGE_UPLOADS + list.get(position).getAvatarMember());
                    holder.title.setVisibility(View.VISIBLE);
                    break;
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("SpecilaitiesAdapter", "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
