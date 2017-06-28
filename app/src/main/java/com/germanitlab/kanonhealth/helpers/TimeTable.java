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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by bassiouny on 15/06/17.
 */

public class TimeTable {

    private String getDayOfWeek(String day) {
        switch (day) {
            case "0":
                return "monday";
            case "1":
                return "tuesday";
            case "2":
                return "wednesday";
            case "3":
                return "thursday";
            case "4":
                return "friday";
            case "5":
                return "saturday";
            case "6":
                return "sunday";
            default:
                return "";
        }
    }

    public void creatTimeTable(List<Table> list, Context context, TableLayout tablelayout) {
        try {
            Collections.sort(list, new Comparator<Table>() {
                @Override
                public int compare(Table t2, Table t1) {
                    return t2.getDayweek().compareTo(t1.getDayweek());
                }
            });
            String temp = "";
            for (Table item : list) {
                TableRow row = new TableRow(context);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                TextView day = new TextView(context);
                day.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                day.setTextColor(Color.BLACK);
                TextView time = new TextView(context);
                time.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4f));
                time.setText(item.getFrom() + " - " + item.getTo());
                time.setTextColor(Color.BLACK);
                if (temp.equals(item.getDayweek())) {
                    day.setText(getDayOfWeek(""));
                } else {
                    day.setText(getDayOfWeek(item.getDayweek()));
                    temp = item.getDayweek();
                }
                row.addView(day);
                row.addView(time);
                tablelayout.addView(row);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }
}
