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
import com.germanitlab.kanonhealth.models.Table;
import com.germanitlab.kanonhealth.models.Times;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by bassiouny on 15/06/17.
 */

public class TimeTable {
    private Context context;

    private String getDayOfWeek(String day) {
        switch (day) {
            case "0":
                return context.getString(R.string.monday);
            case "1":
                return context.getString(R.string.tuesday);
            case "2":
                return context.getString(R.string.wednesday);
            case "3":
                return context.getString(R.string.thursday);
            case "4":
                return context.getString(R.string.friday);
            case "5":
                return context.getString(R.string.saturday);
            case "6":
                return context.getString(R.string.sunday);
            default:
                return "";
        }
    }

    public void creatTimeTable(Table list, Context context, TableLayout tablelayout) {
        try {
            this.context = context;
            if (list.getMon() != null && list.getMon().size() > 0) {
                createRowsForDay(list.getMon(), context.getString(R.string.monday), tablelayout);
            }
            if (list.getTues() != null && list.getTues().size() > 0) {
                createRowsForDay(list.getTues(), context.getString(R.string.tuesday), tablelayout);
            }
            if (list.getWedn() != null && list.getWedn().size() > 0) {
                createRowsForDay(list.getWedn(), context.getString(R.string.wednesday), tablelayout);
            }
            if (list.getThurs() != null && list.getThurs().size() > 0) {
                createRowsForDay(list.getThurs(), context.getString(R.string.thursday), tablelayout);
            }
            if (list.getFri() != null && list.getFri().size() > 0) {
                createRowsForDay(list.getFri(), context.getString(R.string.friday), tablelayout);
            }
            if (list.getSat() != null && list.getSat().size() > 0) {
                createRowsForDay(list.getSat(), context.getString(R.string.saturday), tablelayout);
            }
            if (list.getSun() != null && list.getSun().size() > 0) {
                createRowsForDay(list.getSun(), context.getString(R.string.sunday), tablelayout);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void createRowsForDay(List<Times> mon, String dayInString, TableLayout tablelayout) {
        String temp = "";
        for (Times times : mon) {
            TableRow row = new TableRow(context);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView day = new TextView(context);
            day.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            day.setTextColor(Color.BLACK);
            TextView time = new TextView(context);
            time.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4f));
            time.setText(times.getFrom() + " - " + times.getTo());
            time.setTextColor(Color.BLACK);
            if (temp.equals(dayInString)) {
                day.setText(getDayOfWeek(""));
            } else {
                day.setText(dayInString);
                temp = dayInString;
            }
            row.addView(day);
            row.addView(time);
            tablelayout.addView(row);
        }
    }
}
