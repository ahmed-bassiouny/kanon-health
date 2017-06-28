package com.germanitlab.kanonhealth.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.germanitlab.kanonhealth.R;

/**
 * Created by Milad Metias on 6/28/17.
 */

public class ImageHelper {


    public static void setImage(ImageView iv, String imageFullUrl, Context ctx) {
        setImage(iv, imageFullUrl, R.drawable.placeholder, ctx);
    }

    public static void setImage(ImageView iv, String imageFullUrl, int placeHolder, Context ctx) {
        if (TextUtils.isEmpty(imageFullUrl)) {
            iv.setImageResource(placeHolder);
        } else {
            Glide.with(ctx)
                    .load(imageFullUrl)
                    .fitCenter()
                    .placeholder(placeHolder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(iv);
        }
    }

    public static void setBackground(final View v, String imageFullUrl, Context ctx) {

        setBackground(v, imageFullUrl, R.drawable.appbackground, ctx);
    }

    public static void setBackground(final View v, String imageFullUrl, int placeHolder, Context ctx) {

        v.setBackgroundResource(placeHolder);

        if (!TextUtils.isEmpty(imageFullUrl)) {
            int width = v.getWidth();
            int height = v.getHeight();

            if (width <= 0) {
                width = v.getMeasuredWidth();
            }
            if (height <= 0) {
                height = v.getMeasuredHeight();
            }

            if (width <= 0 && v.getLayoutParams() != null) {
                width = v.getLayoutParams().width;
            }
            if (height <= 0 && v.getLayoutParams() != null) {
                height = v.getLayoutParams().height;
            }


            Glide.with(ctx)
                    .load(imageFullUrl)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(width, height) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                v.setBackground(drawable);
                            } else {
                                v.setBackgroundDrawable(drawable);
                            }
                        }
                    });
        }
    }
}
