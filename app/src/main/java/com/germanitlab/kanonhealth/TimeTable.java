package com.germanitlab.kanonhealth;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.api.models.Times;
import com.germanitlab.kanonhealth.api.models.WorkingHours;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.PrefHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class TimeTable extends AppCompatActivity {
    @BindView(R.id.monday_layout)
    LinearLayout monday_layout;
    @BindView(R.id.tuesday_layout)
    LinearLayout tuesday_layout;
    @BindView(R.id.wednesday_layout)
    LinearLayout wendesday_layout;
    @BindView(R.id.thurday_layout)
    LinearLayout thurday_layout;
    @BindView(R.id.friday_layout)
    LinearLayout friday_layout;
    @BindView(R.id.saturday_layout)
    LinearLayout saturday_layout;
    @BindView(R.id.sunday_layout)
    LinearLayout sunday_layout;
    Map<Integer, List<Integer>> map;
    @BindView(R.id.monday_switch)
    Switch monadaySwitch;
    @BindView(R.id.tuesday_switch)
    Switch tuesdaySwitch;
    @BindView(R.id.wednesday_switch)
    Switch wednesdaySwitch;
    @BindView(R.id.thursday_switch)
    Switch thursdaySwitch;
    @BindView(R.id.friday_switch)
    Switch fridaySwitch;
    @BindView(R.id.saturday_switch)
    Switch saturdaySwitch;
    @BindView(R.id.sunday_switch)
    Switch sundaySwitch;
    @BindView(R.id.sunday_from_to)
    LinearLayout sunday_from_to;
    @BindView(R.id.tuesday_from_to)
    LinearLayout tuesday_from_to;
    @BindView(R.id.wednesday_from_to)
    LinearLayout wednesday_from_to;
    @BindView(R.id.thurday_from_to)
    LinearLayout thurday_from_to;
    @BindView(R.id.friday_from_to)
    LinearLayout friday_from_to;
    @BindView(R.id.saturday_from_to)
    LinearLayout saturday_from_to;
    @BindView(R.id.monday_from_to)
    LinearLayout monday_from_to;
    @BindView(R.id.ll_schedule)
    LinearLayout linearLayoutSchedule;
    @BindView(R.id.tv_schedule)
    TextView tvSchedule;

    @BindView(R.id.table_layout)
    LinearLayout tableLayout;

   WorkingHours list;
    public int type;
    public static Boolean active;
    WorkingHours table;
    public final int MON_KEY = 1;
    public final int TUES_KEY = 2;
    public final int WEDN_KEY = 3;
    public final int THURS_KEY = 4;
    public final int FRI_KEY = 5;
    public final int SAT_KEY = 6;
    public final int SUN_KEY = 7;

    public static OpeningHoursActivity instance;
    public static Activity TimetableInstance;
    Helper helper ;
   int  buttonCount=0;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table_activity);

        initTB();
        table = new WorkingHours();

        try {
            helper = new Helper(this);
            TimetableInstance = this;

            ButterKnife.bind(this);
            instance = new OpeningHoursActivity();
            map = new HashMap<>();
            list = (WorkingHours) getIntent().getSerializableExtra(Constants.DATA);
            type = getIntent().getIntExtra("type", 4);
            from=getIntent().getStringExtra("from");
            handleData(list);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            Log.e("Specilaities", "onCreate", e);
        }


    }

    private void initTB() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_table, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
//                onBackPressed();
                break;
            case R.id.mi_save:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }

    @OnCheckedChanged({R.id.monday_switch, R.id.tuesday_switch, R.id.wednesday_switch, R.id.thursday_switch, R.id.friday_switch, R.id.saturday_switch, R.id.sunday_switch})
    public void checkboxToggled(CompoundButton buttonView, boolean isChecked) {
        try {
            switch (buttonView.getId()) {
                case R.id.monday_switch:
                    if (isChecked) {
                        buttonCount++;
                        if (checkKeys(MON_KEY))
                            addNewItem(monday_layout, MON_KEY, monadaySwitch, null, null, monday_from_to);
                    } else {
                        buttonCount--;
                        removeAll(MON_KEY, monday_layout, monday_from_to);

                    }
                    break;
                case R.id.tuesday_switch:
                    if (isChecked) {
                        buttonCount++;
                        if (checkKeys(TUES_KEY))
                            addNewItem(tuesday_layout, TUES_KEY, tuesdaySwitch, null, null, tuesday_from_to);
                    } else {
                        buttonCount--;
                        removeAll(TUES_KEY, tuesday_layout, tuesday_from_to);
                    }
                    break;
                case R.id.wednesday_switch:
                    if (isChecked) {
                        buttonCount++;
                        if (checkKeys(WEDN_KEY))
                            addNewItem(wendesday_layout, WEDN_KEY, wednesdaySwitch, null, null, wednesday_from_to);
                    } else {
                        buttonCount--;
                        removeAll(WEDN_KEY, wendesday_layout, wednesday_from_to);
                    }
                    break;
                case R.id.thursday_switch:
                    if (isChecked) {
                        buttonCount++;
                        if (checkKeys(THURS_KEY))
                            addNewItem(thurday_layout, THURS_KEY, thursdaySwitch, null, null, thurday_from_to);
                    } else {
                        buttonCount--;
                        removeAll(THURS_KEY, thurday_layout, thurday_from_to);
                    }
                    break;
                case R.id.friday_switch:
                    if (isChecked) {
                        buttonCount++;
                        if (checkKeys(FRI_KEY))
                            addNewItem(friday_layout, FRI_KEY, fridaySwitch, null, null, friday_from_to);
                    } else {
                        buttonCount--;
                        removeAll(FRI_KEY, friday_layout, friday_from_to);
                    }
                    break;
                case R.id.saturday_switch:
                    if (isChecked) {
                        buttonCount++;
                        if (checkKeys(SAT_KEY))
                            addNewItem(saturday_layout, SAT_KEY, saturdaySwitch, null, null, saturday_from_to);
                    } else {
                        buttonCount--;
                        removeAll(SAT_KEY, saturday_layout, saturday_from_to);
                    }
                    break;
                case R.id.sunday_switch:
                    if (isChecked) {
                        buttonCount++;
                        if (checkKeys(SUN_KEY))
                            addNewItem(sunday_layout, SUN_KEY, sundaySwitch, null, null, sunday_from_to);
                    } else {
                        buttonCount--;
                        removeAll(SUN_KEY, sunday_layout, sunday_from_to);
                    }
                    break;
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("Specilaities", "checkboxToggled", e);
        }


    }

    private void removeAll(int key, LinearLayout layout, LinearLayout layout1) {
        if (!checkKeys(key)) {
            List<Integer> Ids = getArray(key);
            for (int i = 0; i < Ids.size(); i++) {
                layout.removeView(findViewById(Ids.get(i)));
                layout.removeView(findViewById(Ids.get(++i)));

            }
            layout1.setVisibility(View.GONE);
            map.remove(key);
        }
    }


    private void addNewItem(final LinearLayout layout, final int day, final Switch tempSwitch, String tfrom, String tto, final LinearLayout layout_from_to) {
        try {
            final LinearLayout line = new LinearLayout(this);
            final int lineId = line.generateViewId();
            line.setId(lineId);
            line.setMinimumWidth(2);
            line.setMinimumHeight(1);
            layout_from_to.setVisibility(View.VISIBLE);
            line.setBackgroundColor(getResources().getColor(R.color.black));
            layout.addView(line);
            final LinearLayout linearLayout = new LinearLayout(this);
            final int layoutId = linearLayout.generateViewId();
            linearLayout.setId(layoutId);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, getPixal(30)));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
            final TextView from = new TextView(this);
            if (tfrom != null)
                from.setText(tfrom);
            else
                from.setText("08:00");
            from.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   showTimePicker(from);
                }
            });
            from.setTextColor(getResources().getColor(R.color.black));
            final int fromId = from.generateViewId();
            from.setId(fromId);
            from.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            from.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            linearLayout.addView(from);
            LinearLayout verticalLine = new LinearLayout(this);
            verticalLine.setLayoutParams(new LinearLayout.LayoutParams(
                    getPixal(2), LinearLayout.LayoutParams.MATCH_PARENT));
            verticalLine.setBackgroundResource(R.color.black);
            linearLayout.addView(verticalLine);
            final TextView to = new TextView(this);
            if (tto != null)
                to.setText(tto);
            else
                to.setText("16:00");
            to.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePicker(to);

                }
            });
            to.setTextColor(getResources().getColor(R.color.black));
            to.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            to.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            final int toId = to.generateViewId();
            to.setId(toId);
            linearLayout.addView(to);
            if (getArray(day) == null)
                createNewList(day, lineId, layoutId, fromId, toId);
            else
                addToArray(day, lineId, layoutId, fromId, toId);
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, .33f));
            linearLayout1.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            ImageView minus = new ImageView(this);
            minus.setBackgroundResource(R.drawable.ic_remove_black_24dp);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getPixal(40), LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.END;
            layoutParams.setMargins(5, 0, 5, 0);
            minus.setLayoutParams(layoutParams);
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.removeView(linearLayout);
                    layout.removeView(line);
                    removeFromList(day, lineId, layoutId, fromId, toId);
                    if (checkKeys(day)) {
                        tempSwitch.setChecked(false);
                        buttonCount--;
                        layout_from_to.setVisibility(View.GONE);
                    }
                }
            });
            linearLayout1.addView(minus);
            final ImageView add = new ImageView(this);
            add.setBackgroundResource(R.drawable.ic_add_black_24dp);
            add.setLayoutParams(layoutParams);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewItem(layout, day, tempSwitch, null, null, layout_from_to);
                }
            });
            linearLayout1.addView(add);
            linearLayout.addView(linearLayout1);
            layout.addView(linearLayout);
            tempSwitch.setChecked(true);
            buttonCount++;
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("Specilaities", "addNewItem", e);
        }

    }

    private void removeFromList(int day, int lineId, int layoutId, int fromId, int toId) {
        List<Integer> Ids = map.get(day);
        Ids.remove(Ids.indexOf(lineId));
        Ids.remove(Ids.indexOf(layoutId));
        Ids.remove(Ids.indexOf(fromId));
        Ids.remove(Ids.indexOf(toId));
        map.remove(day);
        map.put(day, Ids);
    }

    private void createNewList(int day, int lineId, int layoutId, int fromId, int toId) {
        List<Integer> Ids;
        if (map.get(day) != null) {
            Ids = map.get(day);
        } else {
            Ids = new ArrayList<>();
        }
        Ids.add(lineId);
        Ids.add(layoutId);
        Ids.add(fromId);
        Ids.add(toId);
        map.put(day, Ids);
    }

    private void addToArray(int day, int lineId, int layoutId, int fromId, int toId) {
        List<Integer> Ids = map.get(day);
        Ids.add(lineId);
        Ids.add(layoutId);
        Ids.add(fromId);
        Ids.add(toId);
        map.remove(day);
        map.put(day, Ids);
    }

    private boolean checkKeys(int day) {
        if (map.containsKey(day)) {
            List<Integer> Ids = map.get(day);
            if (Ids.size() == 0)
                return true;
            else
                return false;
        } else return true;
    }

    private List<Integer> getArray(int key) {
        if (map.get(key) != null)
            return map.get(key);
        else
            return null;
    }

    private int getPixal(int dps) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    @OnClick(R.id.ll_schedule)
    public void schedule(View view) {
        try {
            Intent openingHoursIntent = new Intent(getApplicationContext(), OpeningHoursActivity.class);
           // openingHoursIntent.putExtra(Constants.DATA, list);
            openingHoursIntent.putExtra("type", type);
         //   openingHoursIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivityForResult(openingHoursIntent,Constants.HOURS_OPENING);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("TimeTable", "schedule: ", e);
        }

    }


    //    @OnClick(R.id.save)
    public void save() {
        try {
            Intent  intent = new Intent(this, AddPractics.class);

            intent.putExtra("type", type);
            if(type==3) {
                int key = MON_KEY;
                while (key < 8) {
                    if (map.containsKey(key)) {
                        List<Integer> Ids = map.get(key);
                        setlists(key, Ids);
                    }
                    key++;
                }
                if (buttonCount != 0) {
                    intent.putExtra("list", table);
                }else
                {
                    intent.putExtra("list", new WorkingHours());
                }

                try {
                    if (OpeningHoursActivity.active)
                        instance.finish();
                } catch (Exception e) {
                }
            }
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void setlists(int key, List<Integer> ids) {
        int count = 0;
        List<Times> timesList = new ArrayList<>();
        while (count < ids.size()) {
//                        Table table = new Table();
//                        table.setDayweek(String.valueOf(key));
            Times times = new Times();
            TextView from = (TextView) findViewById(ids.get(count + 2));
            TextView to = (TextView) findViewById(ids.get(count + 3));
            times.setFrom(from.getText().toString());
            times.setTo(to.getText().toString());
//                        table.setFrom(String.valueOf(from.getText()));
//                        table.setTo(String.valueOf(to.getText()));
//                        list.add(table);
            count += 4;
            timesList.add(times);

        }
        switch (key) {
            case MON_KEY: {
                table.setMonday(timesList);
                break;
            }
            case TUES_KEY: {
                table.setTuesday(timesList);
                break;
            }
            case WEDN_KEY: {
                table.setWednesday(timesList);
                break;
            }
            case THURS_KEY: {
                table.setThursday(timesList);
                break;
            }
            case FRI_KEY: {
                table.setFriday(timesList);
                break;
            }
            case SAT_KEY: {
                table.setSaturday(timesList);
                break;
            }
            case SUN_KEY: {
                table.setSunday(timesList);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if ( resultCode == RESULT_OK) {
                list = (WorkingHours) data.getSerializableExtra(Constants.DATA);
                type = data.getIntExtra("type", 4);
                handleData(list);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("Specilaities", "onActivityResult", e);
        }

    }

    public void handleData(WorkingHours list) {
        if(type==3) {
            tvSchedule.setText(R.string.use_timetable);
            tableLayout.setVisibility(View.VISIBLE);
            if (list != null ) {
                if (list.getMonday() != null && list.getMonday().size() > 0)
                    for (Times times : list.getMonday()) {
                        addNewItem(monday_layout, MON_KEY, monadaySwitch, times.getFrom(), times.getTo(), monday_from_to);
                    }
                if (list.getTuesday() != null && list.getTuesday().size() > 0)
                    for (Times times : list.getTuesday()) {
                        addNewItem(tuesday_layout, TUES_KEY, tuesdaySwitch, times.getFrom(), times.getTo(), tuesday_from_to);
                    }
                if (list.getWednesday() != null && list.getWednesday().size() > 0)
                    for (Times times : list.getWednesday()) {
                        addNewItem(wendesday_layout, WEDN_KEY, wednesdaySwitch, times.getFrom(), times.getTo(), wendesday_layout);
                    }
                if (list.getThursday() != null && list.getThursday().size() > 0)
                    for (Times times : list.getThursday()) {
                        addNewItem(thurday_layout, THURS_KEY, thursdaySwitch, times.getFrom(), times.getTo(), thurday_from_to);
                    }
                if (list.getFriday() != null && list.getFriday().size() > 0)
                    for (Times times : list.getFriday()) {
                        addNewItem(friday_layout, FRI_KEY, fridaySwitch, times.getFrom(), times.getTo(), friday_from_to);
                    }
                if (list.getSaturday() != null && list.getSaturday().size() > 0)
                    for (Times times : list.getSaturday()) {
                        addNewItem(saturday_layout, SAT_KEY, saturdaySwitch, times.getFrom(), times.getTo(), saturday_from_to);
                    }
                if (list.getSunday() != null && list.getSunday().size() > 0)
                    for (Times times : list.getSunday()) {
                        addNewItem(sunday_layout, SUN_KEY, sundaySwitch, times.getFrom(), times.getTo(), sunday_from_to);
                    }
            }
        }else if(type==1)
        {
            tvSchedule.setText(R.string.always_open);
            tableLayout.setVisibility(View.GONE);
        }else if(type==2)
        {
            tvSchedule.setText(R.string.permenant_closed);
            tableLayout.setVisibility(View.GONE);
        } else
        {
            tvSchedule.setText(R.string.no_hours_available);
            tableLayout.setVisibility(View.GONE);
        }
    }

    private void showTimePicker(final TextView txt) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        ContextThemeWrapper wrapper = new ContextThemeWrapper(TimeTable.this, android.R.style.Theme_Holo_Light_Dialog);

        mTimePicker = new TimePickerDialog(wrapper, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (minute < 10)
                    txt.setText(hourOfDay + ":0" + minute);
                else
                    txt.setText(hourOfDay + ":" + minute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


}