package com.germanitlab.kanonhealth.helpers;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.models.Times;
import com.germanitlab.kanonhealth.api.models.WorkingHours;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by bassiouny on 15/06/17.
 */

public class TimeTable {
    private Context context;


    public void creatTimeTable(WorkingHours list, Context context, TableLayout tablelayout) {
        try {
            this.context = context;
            String[] namesOfDays =   DateFormatSymbols.getInstance(Locale.getDefault()).getWeekdays();

            if (list.getMonday()!= null && list.getMonday().size() > 0) {
                createRowsForDay(list.getMonday(),namesOfDays [Calendar.MONDAY], tablelayout);
            }
            if (list.getTuesday() != null && list.getTuesday().size() > 0) {
                createRowsForDay(list.getTuesday(),namesOfDays [Calendar.TUESDAY], tablelayout);
            }
            if (list.getWednesday() != null && list.getWednesday().size() > 0) {
                createRowsForDay(list.getWednesday(), namesOfDays [Calendar.WEDNESDAY], tablelayout);
            }
            if (list.getThursday() != null && list.getThursday().size() > 0) {
                createRowsForDay(list.getThursday(), namesOfDays [Calendar.THURSDAY], tablelayout);
            }
            if (list.getFriday() != null && list.getFriday().size() > 0) {
                createRowsForDay(list.getFriday(), namesOfDays [Calendar.FRIDAY], tablelayout);
            }
            if (list.getSaturday()!= null && list.getSaturday().size() > 0) {
                createRowsForDay(list.getSaturday(), namesOfDays [Calendar.SATURDAY], tablelayout);
            }
            if (list.getSunday() != null && list.getSunday().size() > 0) {
                createRowsForDay(list.getSunday(),namesOfDays [Calendar.SUNDAY], tablelayout);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void createRowsForDay(List<Times> day, String dayInString, TableLayout tablelayout) {
        String temp = "";
        for (Times times : day) {
            TableRow row = new TableRow(context);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView tvDay = new TextView(context);
            tvDay.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            tvDay.setTextColor(Color.BLACK);
            TextView time = new TextView(context);
            time.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4f));
            time.setText(times.getFrom() + " - " + times.getTo());
            time.setTextColor(Color.BLACK);
            if (temp.equals(dayInString)) {
                tvDay.setText("");
            } else {
                tvDay.setText(dayInString);
                temp = dayInString;
            }
            row.addView(tvDay);
            row.addView(time);
            tablelayout.addView(row);
        }
    }
}
