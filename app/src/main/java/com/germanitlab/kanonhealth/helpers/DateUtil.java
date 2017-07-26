package com.germanitlab.kanonhealth.helpers;


import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Atitia Gamea on 27/01/17.
 */

public class DateUtil {

    public static SimpleDateFormat getFormat() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        return format;
    }

    public static SimpleDateFormat getAnotherFormat() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        return format;
    }

    public static String formatDate(long date) {

        int days = getDifferenceDays(date);
        if (days == 0) {
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTimeInMillis(date);
            String dateStr = DateFormat.format("HH:mm", cal).toString();
            return dateStr;
        } else if (days == 1) {
            return "Yesterday";
        } else if (days <= 28) {

            return days + " days ago";
        } else if (days < 365) {

            SimpleDateFormat form = new SimpleDateFormat("EEE, MM dd");
            return form.format(date);
        } else {
            SimpleDateFormat form = new SimpleDateFormat("EEE, yyyy-MM-dd");
            return form.format(date);
        }
    }

    public static String formatBirthday(long date) {
        SimpleDateFormat form = new SimpleDateFormat("dd.MMM.yyyy");
        String result = form.format(date).replace("..", ".");
        return result.replace("..", ".");
    }

    static int getDifferenceDays(long time) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date(time));
        Calendar cal2 = Calendar.getInstance();

        long difference = cal2.getTime().getTime() - cal1.getTime().getTime();
        int days = (int) (difference / (24 * 60 * 60 * 1000));
        return days;

    }

//    static String reformatDateFromString(String dateString) {
//        String result = dateString;
//        SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MMM.yyyy", Locale.getDefault());
//        SimpleDateFormat from1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        try {
//            Date date1 = from1.parse(dateString);
//            result = displayFormat.format(date1);
//            return result;
//        } catch (Exception e) {
//            SimpleDateFormat from2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//            try {
//                Date date2 = from2.parse(dateString);
//                result = displayFormat.format(date2);
//                return result;
//            } catch (Exception e2) {
//                return result;
//            }
//        }
//    }
//
//    static String reformatDateTimeFromString(String dateString) {
//        String result = dateString;
//        SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MMM.yyyy HH:mm:ss", Locale.getDefault());
//        SimpleDateFormat from1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        try {
//            Date date1 = from1.parse(dateString);
//            result = displayFormat.format(date1);
//            return result;
//        } catch (Exception e) {
//            SimpleDateFormat from2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//            try {
//                Date date2 = from2.parse(dateString);
//                result = displayFormat.format(date2);
//                return result;
//            } catch (Exception e2) {
//                return result;
//            }
//        }
//    }
}
