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
import com.germanitlab.kanonhealth.MultiSelectAdapter;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.models.Language;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.models.ChooseModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by norhan on 8/7/17.
 */

public class SelectLanguageAdapter extends RecyclerView.Adapter<SelectLanguageAdapter.MyViewHolder> {


    public ArrayList<Language> allLanguages;
    public ArrayList<Language> myLanguages;
    int type =0;
    Context context;


    public SelectLanguageAdapter(Context context, ArrayList<Language> allLanguages,ArrayList<Language> myLanguages) {
        this.context = context;
        this.allLanguages = allLanguages;
         this.myLanguages= myLanguages;
    }

    @Override
    public SelectLanguageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_select_row, parent, false);

        return new SelectLanguageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectLanguageAdapter.MyViewHolder holder, int position) {
        try {
           Language model = allLanguages.get(position);
            if(model != null) {
                int index=containsId(allLanguages.get(position).getLanguageID());
                if(index!=-1)
                {
                    myLanguages.remove(index);
                    myLanguages.add(allLanguages.get(position));
                    holder.rbtn.setChecked(true);
                }
                holder.tv_title.setText(model.getLanguageTitle());
                holder.img_icon.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            Log.i("MultiSelect", "Adapter" + e);
        }

    }
    public  int containsId( long id) {

        for ( int i=0 ;i<myLanguages.size();i++) {
            if (myLanguages.get(i).getLanguageID() == id) {
                return i;
            }
        }
        return -1;
    }

    private void setDataChecked(CheckBox dbtn, boolean show) {
        if (show)
            dbtn.setChecked(true);
        else
            dbtn.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return allLanguages.size();
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


