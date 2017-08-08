package com.germanitlab.kanonhealth.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.github.siyamed.shapeimageview.HeartImageView;
import com.nex3z.flowlayout.FlowLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.lang.reflect.Field;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Milad Metias on 6/28/17.
 */

public class ImageHelper {

    public static void setImage(ImageView iv, String imageFullUrl) {
        setImage(iv, imageFullUrl, -1, null);
    }

    public static void setImage(ImageView iv, String imageFullUrl, ProgressBar progressBar) {
        setImage(iv, imageFullUrl, -1, progressBar);
    }

    public static void setImage(ImageView iv, String imageFullUrl, int placeHolder) {
        setImage(iv, imageFullUrl, placeHolder, null);
    }

    public static void setImage(final ImageView iv, String imageFullUrl, int placeHolder, final ProgressBar progressBar) {
        try {

            ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance

            DisplayImageOptions.Builder options = new DisplayImageOptions.Builder()

                    .resetViewBeforeLoading(true)
                    .cacheInMemory(false)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2);
            if (placeHolder != -1) {
                options.showImageOnLoading(placeHolder); // resource or drawable
                options.showImageForEmptyUri(placeHolder); // resource or drawable
                options.showImageOnFail(placeHolder);// resource or drawable
            }

            imageLoader.displayImage(imageFullUrl, iv, options.build(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        } catch (Exception e) {
            Log.e("milad", "setImage: ", e);
        }
    }

    public static void setImage(ImageView iv, Uri imageFullUrl) {
        iv.setImageURI(imageFullUrl);
    }

    public static Bitmap TrimBitmap(int res, Context context) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), res);
        return TrimBitmap(bm);
    }

    public static Bitmap TrimBitmap(Bitmap bmp) {
        int imgHeight = bmp.getHeight();
        int imgWidth = bmp.getWidth();


        //TRIM WIDTH - LEFT
        int startWidth = 0;
        for (int x = 0; x < imgWidth; x++) {
            if (startWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if ((bmp.getPixel(x, y) != Color.TRANSPARENT)) {
                        startWidth = x;
                        break;
                    }
                }
            } else break;
        }


        //TRIM WIDTH - RIGHT
        int endWidth = 0;
        for (int x = imgWidth - 1; x >= 0; x--) {
            if (endWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        endWidth = x;
                        break;
                    }
                }
            } else break;
        }


        //TRIM HEIGHT - TOP
        int startHeight = 0;
        for (int y = 0; y < imgHeight; y++) {
            if (startHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        startHeight = y;
                        break;
                    }
                }
            } else break;
        }


        //TRIM HEIGHT - BOTTOM
        int endHeight = 0;
        for (int y = imgHeight - 1; y >= 0; y--) {
            if (endHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        endHeight = y;
                        break;
                    }
                }
            } else break;
        }


        return Bitmap.createBitmap(
                bmp,
                startWidth,
                startHeight,
                endWidth - startWidth,
                endHeight - startHeight
        );

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

    public static View setImageCircle(String speciality_icon, Activity activity) {
        Helper helper = new Helper(activity);
        CircleImageView circularImageView = new CircleImageView(activity);
        if (TextUtils.isEmpty(speciality_icon)) {
            circularImageView.setImageResource(R.drawable.placeholder);
        } else {
            setImage(circularImageView, ApiHelper.SERVER_IMAGE_URL + "/" + speciality_icon, -1);
        }
        circularImageView.setLayoutParams(new FlowLayout.LayoutParams(helper.dpToPx(30), helper.dpToPx(30)));
        circularImageView.setPadding(7, 4, 7, 4);
        return circularImageView;
    }

    public static View setImageHeart(int src, Context context) {
        View view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.heart_item_layout, null);
        HeartImageView item = (HeartImageView) view.findViewById(R.id.hiv_heart);
        item.setImageBitmap(ImageHelper.TrimBitmap(src, context));
        return view;
    }

}

