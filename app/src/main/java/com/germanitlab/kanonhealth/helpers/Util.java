package com.germanitlab.kanonhealth.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.germanitlab.kanonhealth.R;

/**
 * Created by mohammed on 8/29/15.
 */
public class Util {
    //    private static Util util;
    private Context context;
    private static ProgressDialog progressDialog;

    public static Util getInstance(Context context) {
//        if(util!=null)
//            return util;
//        else{
        return new Util(context);
//        }
    }

    private Util(Context context) {
        this.context = context;
    }

    public void showPhoto(Uri photoUri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoUri, "image/*");
        context.startActivity(intent);
    }

    public void showVideo(Uri videoUri) {
        Log.d("VIEDIO  : ", " " + videoUri);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(videoUri, "video/mp4");
        context.startActivity(intent);
    }

    public void showAudio(Uri audioUri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(audioUri, "audio/*");
        context.startActivity(intent);
    }

    public void showLocation(double lat, double lon) {
        String uriBegin = "geo:" + lat + "," + lon;
        String query = lat + "," + lon + "(" + "Location" + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }



    public void setupUI(View view, final Activity activity) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, activity);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
