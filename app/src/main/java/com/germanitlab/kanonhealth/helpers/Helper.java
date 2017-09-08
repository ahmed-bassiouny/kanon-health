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
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.Crop.PickerBuilder;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.google.gson.Gson;

import net.glxn.qrgen.android.QRCode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by eslam on 12/30/16.
 */

public class Helper {

    private Activity activity;
    private String birthdate = "";


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

    public void ImportQr() {

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
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(PrefHelper.get(activity,PrefHelper.KEY_USER_KEY,""), UserInfo.class);
        TextView name = (TextView) dialog.findViewById(R.id.name);
        TextView birthdate = (TextView) dialog.findViewById(R.id.birthdate);

        String idEncrypt = getMd5(String.valueOf(userInfo.getUserID()));
        if (!idEncrypt.isEmpty()) {
            Bitmap myBitmap = QRCode.from(userInfo.getUserID()+ ":" + idEncrypt).bitmap();
            imageView.setImageBitmap(myBitmap);
        }
        CircleImageView circleImageView = (CircleImageView) dialog.findViewById(R.id.image_profile);

        if (userInfo.getAvatar() != null && userInfo.getAvatar() != "") {
            ImageHelper.setImage(circleImageView, ApiHelper.SERVER_IMAGE_URL + userInfo.getAvatar(), -1);
        }
        name.setText(userInfo.getFirstName()+userInfo.getLastName());

        birthdate.setText(DateHelper.FromDisplayDateToBirthDateString(DateHelper.FromServerDateStringToServer(userInfo.getBirthday())));
        dialog.show();
    }

    public static String getFormattedDate(String timeInString) {
        Date parseDate = DateHelper.FromDisplayDateStringToDisplay(DateHelper.FromServerDatetimeStringToDisplayString(timeInString));
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(parseDate.getTime());

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
        }
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
        if (context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else
            return false;
    }

    public static void getCroppedImageFromCamera(final ParentActivity activity, int type) {
        new PickerBuilder(activity, type)
                .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                    @Override
                    public Uri onImageReceived(Uri imageUri) {
//                        imageView.setImageURI(imageUri);
                        activity.ImagePickerCallBack(imageUri);
                        return imageUri;
                    }
                })
                .start();
    }


    public static void getCroppedImageFromCamera(final ParentFragment parentFragment, final FragmentActivity activity, int type) {
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
            for (int i = 0; i < messageDigest.length; i++)
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

    public void showDatePicker(final TextView textView) {
        Calendar calender = Calendar.getInstance();
        if (birthdate.isEmpty())
            birthdate = calender.get(Calendar.YEAR) + "-" + calender.get(Calendar.MONTH) + "-" + calender.get(Calendar.DAY_OF_MONTH);
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(activity, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                birthdate = dateDesc;
                textView.setText(DateHelper.FromDisplayDateToBirthDateString(DateHelper.FromServerDateStringToServer(birthdate)));
            }
        }).textConfirm(activity.getString(R.string.ok)) //text of confirm button
                .textCancel(activity.getString(R.string.cancel)) //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .minYear(1900) //min year in loop
                .maxYear(calender.get(Calendar.YEAR) + 1) // max year in loop
                .showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                .dateChose(birthdate) // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(activity);
    }


    public static void handleError(String tag, final String errorMsg, Throwable e, final int errorCode, final Context context) {
        Crashlytics.setInt("Error_Code", errorCode);
        Crashlytics.logException(e);
        if ((!TextUtils.isEmpty(tag)) && (!TextUtils.isEmpty(errorMsg)) && context != null && context instanceof AppCompatActivity) {
            Log.e(tag, errorMsg + " Error Code : " + errorCode, e);
            ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, errorMsg + " Error Code : " + errorCode, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Log.e("handleError", " Error Code : " + errorCode, e);
        }
    }

}


