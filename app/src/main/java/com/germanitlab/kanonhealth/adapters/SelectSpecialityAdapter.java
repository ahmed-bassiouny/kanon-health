package com.germanitlab.kanonhealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

/**
/**
 * Created by norhan on 8/7/17.
 */

public class SelectSpecialityAdapter extends RecyclerView.Adapter<com.germanitlab.kanonhealth.adapters.SelectSpecialityAdapter.MyViewHolder>{


       public ArrayList<Speciality> allSpecialList;
       public ArrayList<Speciality> mySpecialList;
        Context context;


        public SelectSpecialityAdapter(Context context, ArrayList<Speciality> allSpecialList,ArrayList<Speciality> mySpecialList) {
            this.context = context;
            this.allSpecialList = allSpecialList;
            this.mySpecialList=mySpecialList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_select_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final com.germanitlab.kanonhealth.adapters.SelectSpecialityAdapter.MyViewHolder holder, int position) {
            try {
                Speciality model = allSpecialList.get(position);
                if(model != null) {
                    int index=containsId(allSpecialList.get(position).getSpecialityID());
                  if(index!=-1)
                  {
                      mySpecialList.remove(index);
                      mySpecialList.add(allSpecialList.get(position));
                      holder.rbtn.setChecked(true);
                  }
                            holder.tv_title.setText(model.getTitle());
                            ImageHelper.setImage(holder.img_icon, ApiHelper.SERVER_IMAGE_URL + "/" + model.getImage(), -1);
                }
            } catch (Exception e) {
                Crashlytics.logException(e);
                Toast.makeText(context, context.getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
                Log.i("MultiSelect", "Adapter" + e);
            }
        }

        @Override
        public int getItemCount() {
            return allSpecialList.size();
        }

    public  int containsId( long id) {

        for ( int i=0 ;i<mySpecialList.size();i++) {
            if (mySpecialList.get(i).getSpecialityID() == id) {
                return i;
            }
        }
        return -1;
    }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_title;
            public RelativeLayout relativeLayout;
            public CircleImageView img_icon, status;
            public CheckBox rbtn;

            public MyViewHolder(View view) {
                super(view);
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                img_icon = (CircleImageView) view.findViewById(R.id.img_icon);
                status = (CircleImageView) view.findViewById(R.id.status);
                relativeLayout = (RelativeLayout) view.findViewById(R.id.multi_choise_layout);
                rbtn = (CheckBox) view.findViewById(R.id.rbtn);
            }
        }
    }


