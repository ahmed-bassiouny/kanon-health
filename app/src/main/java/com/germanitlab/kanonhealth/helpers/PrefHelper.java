package com.germanitlab.kanonhealth.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.android.gms.common.api.Result;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by milad on 8/14/17.
 */

public class PrefHelper {
    private static final String PREF_NAME = "health";
    private static final int PRIVATE_MODE = 0;

    //region key

    public static final String KEY_IS_DOCTOR = "is_doctor";
    public static final String KEY_PASSCODE = "pass_code";
    public static final String KEY_DOCTOR_LIST = "doctor_list";
    public static final String KEY_DOCUMENT_HISTORY = "document_history";
    public static final String KEY_HISTORY = "history";
    public static final String KEY_TIME_TYPE = "time_type";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_PASSWORD = "user_password";
    public static final String KEY_PROFILE_IMAGE = "profileImage";
    public static final String KEY_PROFILE_QR = "profileQr";
    public static final String KEY_IS_OLD = "is_new";
    public static final String KEY_DOCTOR_KEY = "doctor";
    public static final String KEY_USER_KEY = "user";
    public static final String KEY_USER_INTENT = "user_intent";
    public static final String KEY_USER_STATUS = "userStatus";
    public static final String KEY_IS_DOC = "is_doc";
    public static final String KEY_IS_CLINIC = "is_clinic";

    //endregion

    //region put

    public static void put(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void put(Context context, String key, float value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void put(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void put(Context context, String key, long value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void put(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void put(Context context, String key, Set<String> value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    public static void put(Context context, String key, Serializable value) {
        Gson gson = new Gson();
        put(context, key, gson.toJson(value));
    }

    //endregion

    //region check

    public static boolean check(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.contains(key);
    }

    //endregion

    //region get

    public static boolean get(Context context, String key, boolean defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getBoolean(key, defaultValue);
    }

    public static float get(Context context, String key, float defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getFloat(key, defaultValue);
    }

    public static int get(Context context, String key, int defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getInt(key, defaultValue);
    }

    public static long get(Context context, String key, long defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getLong(key, defaultValue);
    }

    public static String get(Context context, String key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getString(key, defaultValue);
    }

    public static Set<String> get(Context context, String key, Set<String> defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getStringSet(key, defaultValue);
    }

    public static <T extends Serializable> T get(Context context, String key, Class<T> type) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        Gson gson = new Gson();
        String result = pref.getString(key, "");
        if (TextUtils.isEmpty(result) || type == null) {
            return null;
        } else {
            return gson.fromJson(result, type);
        }
    }


    //endregion

    //region remove

    public static void put(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    //endregion

}
