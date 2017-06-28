package com.germanitlab.kanonhealth.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.models.messages.Message;

import java.util.ArrayList;

/**
 * Created by Geram IT Lab on 19/03/2017.
 */

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.MyViewHolder> {
    private Context mContext;
    ArrayList<Message> documnents;

    public DocumentsAdapter(ArrayList<Message> documnents, Context mcontext) {
        this.documnents = documnents;
        this.mContext = mcontext;
    }

    @Override
    public DocumentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.image_layout, parent, false);

        return new DocumentsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            if (documnents.get(position) != null) {
                ImageHelper.setImage(holder.image, Constants.CHAT_SERVER_URL + "/" + documnents.get(position), R.drawable.profile_place_holder, mContext);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(mContext, mContext.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return documnents.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }
}
