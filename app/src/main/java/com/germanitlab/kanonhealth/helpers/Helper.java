package com.germanitlab.kanonhealth.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.AddPractics;
import com.germanitlab.kanonhealth.Crop.PickerBuilder;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.google.gson.Gson;

import net.glxn.qrgen.android.QRCode;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by eslam on 12/30/16.
 */

public class Helper {

    private Activity activity;
    private String birthdate="";


    public Helper(Activity activity) {

        this.activity = activity;
    }

    public void replaceFragments(Class fragmentClass, int continer, String tag) {
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


    public void replaceFragments(Fragment fragment, int continer, String tag) {
        FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(continer, fragment).addToBackStack(tag)
                .commit();
    }

    public void ImportQr(final PrefManager mPrefManager) {

/*        if (mPrefManager.getData(PrefManager.Image_data) != "" &&mPrefManager.getData(PrefManager.Image_data) != null) {
            Picasso.with(activity).load(Constants.CHAT_SERVER_URL
                    + "/" + mPrefManager.getData(PrefManager.Image_data))
                    .resize(80, 80).into(myQr);
        }*/
        Dialog dialog;
        dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialoge);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.image);
        /*if (mPrefManager.getData(PrefManager.PROFILE_QR) != null) {
            ImageHelper.setImage(imageView, Constants.CHAT_SERVER_URL + "/" + mPrefManager.getData(PrefManager.PROFILE_QR), R.drawable.qr, activity);
        }*/
        Gson gson = new Gson();
        Log.d("user data", mPrefManager.getData(PrefManager.USER_KEY));
        UserInfoResponse userInfoResponse = gson.fromJson(mPrefManager.getData(PrefManager.USER_KEY), UserInfoResponse.class);
        TextView name = (TextView) dialog.findViewById(R.id.name);
//                TextView last_name = (TextView) dialog.findViewById(R.id.last_name);
        TextView birthdate = (TextView) dialog.findViewById(R.id.birthdate);

        String idEncrypt=getMd5(String.valueOf(userInfoResponse.getUser().getId()));
        if(!idEncrypt.isEmpty()) {
            Bitmap myBitmap = QRCode.from(userInfoResponse.getUser().getId()+":"+idEncrypt).bitmap();
            imageView.setImageBitmap(myBitmap);
        }
        CircleImageView circleImageView = (CircleImageView) dialog.findViewById(R.id.image_profile);

        if (userInfoResponse.getUser().getAvatar() != null && userInfoResponse.getUser().getAvatar() != "") {
            ImageHelper.setImage(circleImageView, Constants.CHAT_SERVER_URL + "/" + userInfoResponse.getUser().getAvatar(), -1, activity);
        }
        if(userInfoResponse != null && !TextUtils.isEmpty(userInfoResponse.getUser().getFullName()))
            name.setText(userInfoResponse.getUser().getFullName());
//                last_name.setText(userInfoResponse.getUser().getLast_name().toString());
        try {
            Date parseDate = DateUtil.getAnotherFormat().parse(userInfoResponse.getUser().getBirthDate().toString());
            String s="";
            if(!userInfoResponse.getUser().getBirthDate().toString().equals("0000-00-00"))
                s= (DateUtil.formatBirthday(parseDate.getTime()));
            birthdate.setText(s);
        } catch (ParseException e) {
            e.printStackTrace();
            birthdate.setText("");
        }
        dialog.show();
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
        if(context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        else
            return false ;
    }

    public static void getCroppedImageFromCamera(final ParentActivity activity, int type) {
        getCroppedImageFromCamera(null, activity, type);
    }


    public static void getCroppedImageFromCamera(final ParentFragment parentFragment, final AppCompatActivity activity, int type) {
        new PickerBuilder(activity, type)
                .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                    @Override
                    public Uri onImageReceived(Uri imageUri) {
//                        imageView.setImageURI(imageUri);
                        parentFragment.ImagePickerCallBack(imageUri);
                        return imageUri;
                    }
                })
                .start();
    }



    public String getMd5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
            Toast.makeText(activity, R.string.generate_qrcode, Toast.LENGTH_SHORT).show();
        }
        return "";
    }
    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public void  showDatePicker( final TextView textView){
        Calendar calender = Calendar.getInstance();
        if(birthdate.isEmpty())
            birthdate=calender.get(Calendar.YEAR) + "-" + calender.get(Calendar.MONTH) + "-" +calender.get(Calendar.DAY_OF_MONTH);
        DatePickerPopWin pickerPopWin=new DatePickerPopWin.Builder(activity, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                birthdate = dateDesc;
                Date parseDate = null;
                try {
                    parseDate = DateUtil.getAnotherFormat().parse(birthdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String newformatDate = (DateUtil.formatBirthday(parseDate.getTime()));
                textView.setText(newformatDate);
            }
        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .minYear(1900) //min year in loop
                .maxYear(calender.get(Calendar.YEAR)+1) // max year in loop
                .showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                .dateChose(birthdate) // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(activity);
    }


}


