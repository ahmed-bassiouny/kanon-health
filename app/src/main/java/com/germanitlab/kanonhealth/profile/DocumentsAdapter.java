package com.germanitlab.kanonhealth.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.models.messages.Message;

import java.util.ArrayList;

/**
 * Created by Geram IT Lab on 19/03/2017.
 */

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.MyViewHolder> {
    private Context mContext ;
    ArrayList<Message> documnents;

    public DocumentsAdapter(ArrayList<Message> documnents, Context mcontext)
    {
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
        if(documnents.get(position) !=null)
        {
            Helper.setImage(mContext ,Constants.CHAT_SERVER_URL
                    + "/" + documnents.get(position) , holder.image , R.drawable.profile_place_holder );
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
