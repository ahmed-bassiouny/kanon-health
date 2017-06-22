package com.germanitlab.kanonhealth.db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.germanitlab.kanonhealth.helpers.Constants;


/**
 * Created by eslam on 12/30/16.
 */

public class PrefManager {


    public static final String IS_DOCTOR ="is_doctor" ;
    public static final String PASSCODE ="pass_code" ;
    public static final String CHAT_LIST = "chat_list";
    public static final String DOCTOR_LIST = "doctor_list" ;
    public static final String DOCUMENT_HISTORY ="document_history" ;
    public static final String HISTORY = "history";
    public static final String TIME_TYPE = "time_type";
    public static final String USER_ID = "user_id";
    public static final String USER_PASSWORD = "user_password";
    public static String PROFILE_IMAGE = "profileImage";
    public static String PROFILE_QR = "profileQr";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    //Keys
    public static String DOCTOR_KEY = "doctor";
    public static String USER_KEY = "user";
    public static String USER_STATUS = "userStatus";
/*
    public static String Image_data = "imagedata";
*/


    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = Constants.PREF_NAME;
    private static final String IS_FIRST_TIME_LAUNCH = Constants.IS_FIRST_LAUNCH;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setLogin(boolean isLogin) {
        editor.putBoolean(Constants.IS_LOGIN, isLogin);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean isLogin() {
        return pref.getBoolean(Constants.IS_LOGIN, false);
    }

    public void put(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }
    public void put (String key, boolean value){
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean get(String key){
        return pref.getBoolean(key, false);
    }

    public String getData(String key) {
        return pref.getString(key, "");
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }
}
