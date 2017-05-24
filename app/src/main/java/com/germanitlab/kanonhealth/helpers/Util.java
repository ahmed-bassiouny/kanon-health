package com.germanitlab.kanonhealth.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by mohammed on 8/29/15.
 */
public class Util {
    private static Util util;
    private Context context;
    public static Util getInstance(Context context) {
        if(util!=null)
            return util;
        else{
            return new Util(context);
        }
    }
    private Util(Context context){
        this.context = context;
    }
    public void showPhoto(Uri photoUri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoUri, "image/*");
        context.startActivity(intent);
    }
    public void showVideo(Uri videoUri){
        Log.d("VIEDIO  : " , " " + videoUri);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(videoUri, "video/mp4");
        context.startActivity(intent);
    }
    public void showAudio(Uri audioUri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(audioUri, "audio/*");
        context.startActivity(intent);
    }
    public void showLocation(double lat,double lon){
        String uriBegin = "geo:" + lat + "," + lon;
        String query =  lat  + "," + lon + "(" + "Location" + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
