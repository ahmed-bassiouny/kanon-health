package com.germanitlab.kanonhealth.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;

import java.text.ParseException;
import java.util.Date;


/**
 * Created by eslam on 12/30/16.
 */

public class Helper {

    private Activity activity;

    public Helper(Activity activity) {

        this.activity = activity;
    }

    public void replaceFragments(Class fragmentClass, int continer , String tag) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(continer, fragment).addToBackStack(tag)
                .commit();
    }

    public void replaceFragments(Fragment fragment, int continer , String tag) {
        FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(continer, fragment).addToBackStack(tag)
                .commit();
    }

    public static void ImportQr(final PrefManager mPrefManager , final Activity activity , ImageView myQr)
    {

/*        if (mPrefManager.getData(PrefManager.Image_data) != "" &&mPrefManager.getData(PrefManager.Image_data) != null) {
            Picasso.with(activity).load(Constants.CHAT_SERVER_URL
                    + "/" + mPrefManager.getData(PrefManager.Image_data))
                    .resize(80, 80).into(myQr);
        }*/
        myQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog   ;
                dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialoge);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT ,
                        WindowManager.LayoutParams.MATCH_PARENT );
                ImageView imageView = (ImageView) dialog.findViewById(R.id.image);
                if (mPrefManager.getData(PrefManager.Image_data)!= null){
                    Picasso.with(activity).load(Constants.CHAT_SERVER_URL
                            + "/" + mPrefManager.getData(PrefManager.Image_data))
                            .into(imageView);
                }
                Gson gson = new Gson();
                Log.d("user data" , mPrefManager.getData(PrefManager.USER_KEY));
                UserInfoResponse userInfoResponse =gson.fromJson(mPrefManager.getData(PrefManager.USER_KEY), UserInfoResponse.class);
                TextView first_name = (TextView) dialog.findViewById(R.id.first_name);
                TextView last_name = (TextView) dialog.findViewById(R.id.last_name);
                TextView birthdate = (TextView) dialog.findViewById(R.id.birthdate);
                CircleImageView circleImageView = (CircleImageView) dialog.findViewById(R.id.image_profile);
                if(userInfoResponse.getUser().getAvatar() != null && userInfoResponse.getUser().getAvatar() != "" ) {
                    Picasso.with(activity).load(Constants.CHAT_SERVER_URL
                            + "/" + userInfoResponse.getUser().getAvatar()).into(circleImageView);
                }
                else
                    Picasso.with(activity).load(Constants.CHAT_SERVER_URL
                            + "/" + userInfoResponse.getUser().getAvatar()).placeholder(R.drawable.profile_place_holder)
                            .resize(500, 500).into(circleImageView);
                first_name.setText(userInfoResponse.getUser().getFirst_name().toString());
                last_name.setText(userInfoResponse.getUser().getLast_name().toString());
                try {
                    Date parseDate = DateUtil.getAnotherFormat().parse(userInfoResponse.getUser().getBirth_date().toString());
                    String s = (DateUtil.formatBirthday(parseDate.getTime()));
                    Log.d("my converted date" ,s );
                    birthdate.setText(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.show();
            }
        });
    }

    public static void dismissProgressDialog(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }
    public static void showProgressDialog(ProgressDialog progressDialog , Activity activity) {
        progressDialog = ProgressDialog.show(activity, "", activity.getResources().getString(R.string.waiting_text), true);
    }


    public static void showAlertDialog(final Context context, String title, String message
            , DialogInterface.OnClickListener yesClickListener, DialogInterface.OnClickListener noClickListener) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes), yesClickListener)
                .setNegativeButton(context.getString(R.string.no), noClickListener);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


