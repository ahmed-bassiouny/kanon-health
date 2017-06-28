package com.germanitlab.kanonhealth.helpers;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.germanitlab.kanonhealth.R;

/**
 * Created by Milad Metias on 6/28/17.
 */

public class ImageHelper {


    public static void setImage(ImageView iv, String imageFullUrl, Context ctx) {
        Glide.with(ctx)
                .load(imageFullUrl)
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(iv);
    }
}
