package com.germanitlab.kanonhealth.helpers;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by milad on 7/30/17.
 */

public class DateHelper {

    //    public static final String SERVER_DATETIME_FORMATE = "2017-07-24 10:24:58";
    public static final String SERVER_DATETIME_FORMATE = "yyyy-MM-dd HH:mm:ss";
    public static final String DISPLAY_DATETIME_FORMATE = "yyyy-MM-dd HH:mm:ss";
    public static final String SERVER_DATE_FORMATE = "yyyy-MM-dd";
    public static final String DISPLAY_DATE_FORMATE = "yyyy-MM-dd";
    public static final String BIRTHDATE_FORMATE = "dd.MMM.yyyy";


    private static final String TAG = "DateHelper";

    //region DateTime

    public static String FromServerDatetimeToDisplayString(Date serverDate) {
        String result = serverDate.toString();
        try {
            SimpleDateFormat displayDateFormat = new SimpleDateFormat(DISPLAY_DATETIME_FORMATE,Locale.US);
            result = displayDateFormat.format(serverDate);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "FromServerDatetimeToDisplayString: ", e);
        } finally {
            return result;
        }
    }

    public static String FromDisplayDatetimeToServerString(Date displayDate) {
        String result = displayDate.toString();
        try {
            SimpleDateFormat serverDateFormat = new SimpleDateFormat(SERVER_DATETIME_FORMATE,Locale.US);
            result = serverDateFormat.format(displayDate);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "FromDisplayDatetimeToServerString: ", e);
        } finally {
            return result;
        }
    }

    public static Date FromServerDatetimeStringToServer(String serverDate) {
        Date result = new Date();
        try {
            SimpleDateFormat serverDateFormat = new SimpleDateFormat(SERVER_DATETIME_FORMATE,Locale.US);
            result = serverDateFormat.parse(serverDate);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "FromServerDatetimeStringToServer: ", e);
        } finally {
            return result;
        }
    }

    public static Date FromDisplayDatetimeStringToDisplay(String displayDate) {
        Date result = new Date();
        try {
            SimpleDateFormat displayDateFormat = new SimpleDateFormat(DISPLAY_DATETIME_FORMATE,Locale.US);
            result = displayDateFormat.parse(displayDate);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "FromDisplayDatetimeStringToDisplay: ", e);
        } finally {
            return result;
        }
    }

    public static String FromServerDatetimeStringToDisplayString(String serverDate) {
        return FromServerDatetimeToDisplayString(FromServerDatetimeStringToServer(serverDate));
    }

    public static String FromDisplayDatetimeStringToServerString(String displayDate) {
        return FromDisplayDatetimeToServerString(FromDisplayDatetimeStringToDisplay(displayDate));
    }

    //endregion

    //region Date

    public static String FromServerDateToDisplayString(Date serverDate) {
        String result = serverDate.toString();
        try {
            SimpleDateFormat displayDateFormat = new SimpleDateFormat(DISPLAY_DATE_FORMATE,Locale.US);
            result = displayDateFormat.format(serverDate);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "FromServerDateToDisplayString: ", e);
        } finally {
            return result;
        }
    }

    public static String FromDisplayDateToServerString(Date displayDate) {
        String result = displayDate.toString();
        try {
            SimpleDateFormat serverDateFormat = new SimpleDateFormat(SERVER_DATE_FORMATE,Locale.US);
            result = serverDateFormat.format(displayDate);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "FromDisplayDateToServerString: ", e);
        } finally {
            return result;
        }
    }

    public static Date FromServerDateStringToServer(String serverDate) {
        Date result = new Date();
        try {
            SimpleDateFormat serverDateFormat = new SimpleDateFormat(SERVER_DATE_FORMATE,Locale.US);
            result = serverDateFormat.parse(serverDate);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "FromServerDateStringToServer: ", e);
        } finally {
            return result;
        }
    }

    public static Date FromDisplayDateStringToDisplay(String displayDate) {
        Date result = new Date();
        try {
            SimpleDateFormat displayDateFormat = new SimpleDateFormat(DISPLAY_DATE_FORMATE,Locale.US);
            result = displayDateFormat.parse(displayDate);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "FromDisplayDateStringToDisplay: ", e);
        } finally {
            return result;
        }
    }

    public static String FromServerDateStringToDisplayString(String serverDate) {
        return FromServerDateToDisplayString(FromServerDateStringToServer(serverDate));
    }

    public static String FromDisplayDateStringToServerString(String displayDate) {
        return FromDisplayDateToServerString(FromDisplayDateStringToDisplay(displayDate));
    }

    //endregion

    //region BirthDate

    public static String FromDisplayDateToBirthDateString(Date displayDate) {
        String result = displayDate.toString();
        try {
            SimpleDateFormat displayDateFormat = new SimpleDateFormat(BIRTHDATE_FORMATE,Locale.US);
            result = displayDateFormat.format(displayDate);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "FromDisplayDateToBirthDateString: ", e);
        } finally {
            return result;
        }
    }

    public static Date FromBirthDateStringToDisplay(String displayDate) {
        Date result = new Date();
        try {
            SimpleDateFormat displayDateFormat = new SimpleDateFormat(BIRTHDATE_FORMATE,Locale.US);
            result = displayDateFormat.parse(displayDate);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(TAG, "FromBirthDateStringToDisplay: ", e);
        } finally {
            return result;
        }
    }


    //endregion

}
