package com.germanitlab.kanonhealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;

/**
 * Created by Geram IT Lab on 02/03/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    private Context mContext;
    String[] images;

    public ImageAdapter(String[] images, Context mcontext) {
        this.images = images;
        this.mContext = mcontext;
    }

    @Override
    public ImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.image_layout, parent, false);

        return new ImageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            if (images[position] != null) {
                ImageHelper.setImage(holder.image, ApiHelper.SERVER_IMAGE_UPLOADS + images[position], R.drawable.profile_place_holder);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(mContext, mContext.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("ImageAdapter", "onBindViewHolder: ", e);
        }

    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }


}
