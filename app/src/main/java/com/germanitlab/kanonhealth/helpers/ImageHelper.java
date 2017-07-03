package com.germanitlab.kanonhealth.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.germanitlab.kanonhealth.R;
import com.mukesh.countrypicker.Country;

import java.lang.reflect.Field;

/**
 * Created by Milad Metias on 6/28/17.
 */

public class ImageHelper {

    // image string url
    public static void setImage(ImageView iv, String imageFullUrl, Context ctx) {
        setImage(iv, imageFullUrl, -1, ctx);
    }

    public static void setImage(final ImageView iv, String imageFullUrl, int placeHolder, Context ctx) {
        if (TextUtils.isEmpty(imageFullUrl)) {
            iv.setImageResource(placeHolder);
        } else if (placeHolder == -1) {
            Glide.with(ctx)
                    .load(imageFullUrl)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(iv);
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

    // image uri
    public static void setImage(ImageView iv, Uri imageUri, Context ctx) {
        setImage(iv, imageUri, R.drawable.placeholder, ctx);
    }

    public static void setImage(final ImageView iv, Uri imageUri, int placeHolder, Context ctx) {
        Glide.with(ctx)
                .load(imageUri)
                .asBitmap()
                .fitCenter()
                .placeholder(placeHolder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(iv);
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


    public static void setLanguageImage(final ImageView iv, String langCode) {
        if (!TextUtils.isEmpty(langCode)) {

//            int resourceId = ctx.getResources().getIdentifier("ic_lang_" + langCode, "drawable", "com.germanitlab.kanonhealth");
            int resourceId = getResourceIdByName("ic_lang_" + langCode);

            if (resourceId != -1) {
                iv.setImageResource(resourceId);
            } else {
//                iv.setImageDrawable(null);
                Country temp = Country.getCountryByISO(langCode);
                if (temp != null) {
                    iv.setImageResource(temp.getFlag());
                } else {
                    iv.setImageDrawable(null);
                }
            }
        }
    }


    public static void setCountryImage(final ImageView iv, String langCode) {
        if (!TextUtils.isEmpty(langCode)) {

//            int resourceId = ctx.getResources().getIdentifier("ic_lang_" + langCode, "drawable", "com.germanitlab.kanonhealth");
            int resourceId = getResourceIdByName("ic_lang_round_" + langCode);

            if (resourceId != -1) {
                iv.setImageResource(resourceId);
            } else {
                iv.setImageDrawable(null);
            }
        }
    }


    private static int getResourceIdByName(String name) {
        name = name.toLowerCase();
        int drawableId = -1;
        try {
            Class res = R.drawable.class;
            Field field = res.getField(name);
            drawableId = field.getInt(null);
        } catch (Exception e) {
            Log.e("MyTag", "Failure to get drawable id. with name : " + name, e);
        } finally {
            return drawableId;
        }
    }
}
