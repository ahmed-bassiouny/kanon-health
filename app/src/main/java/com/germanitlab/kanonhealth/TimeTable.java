package com.germanitlab.kanonhealth;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
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
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.models.Table;

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
    List<Table> list;
    int type;
    public static Boolean active;

    public static OpeningHoursActivity instance;
    public static Activity TimetableInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table_activity);

        initTB();

        try {
            TimetableInstance = this;

            ButterKnife.bind(this);
            instance = new OpeningHoursActivity();
            map = new HashMap<>();
            list = (List<Table>) getIntent().getSerializableExtra(Constants.DATA);
            type = getIntent().getIntExtra("type", 0);
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
                        if (checkKeys(0))
                            addNewItem(monday_layout, 0, monadaySwitch, null, null, monday_from_to);
                    } else {
                        removeAll(0, monday_layout, monday_from_to);

                    }
                    break;
                case R.id.tuesday_switch:
                    if (isChecked) {
                        if (checkKeys(1))
                            addNewItem(tuesday_layout, 1, tuesdaySwitch, null, null, tuesday_from_to);
                    } else {
                        removeAll(1, tuesday_layout, tuesday_from_to);
                    }
                    break;
                case R.id.wednesday_switch:
                    if (isChecked) {
                        if (checkKeys(2))
                            addNewItem(wendesday_layout, 2, wednesdaySwitch, null, null, wednesday_from_to);
                    } else {
                        removeAll(2, wendesday_layout, wednesday_from_to);
                    }
                    break;
                case R.id.thursday_switch:
                    if (isChecked) {
                        if (checkKeys(3))
                            addNewItem(thurday_layout, 3, thursdaySwitch, null, null, thurday_from_to);
                    } else {
                        removeAll(3, thurday_layout, thurday_from_to);
                    }
                    break;
                case R.id.friday_switch:
                    if (isChecked) {
                        if (checkKeys(4))
                            addNewItem(friday_layout, 4, fridaySwitch, null, null, friday_from_to);
                    } else {
                        removeAll(4, friday_layout, friday_from_to);
                    }
                    break;
                case R.id.saturday_switch:
                    if (isChecked) {
                        if (checkKeys(5))
                            addNewItem(saturday_layout, 5, saturdaySwitch, null, null, saturday_from_to);
                    } else {
                        removeAll(5, saturday_layout, saturday_from_to);
                    }
                    break;
                case R.id.sunday_switch:
                    if (isChecked) {
                        if (checkKeys(6))
                            addNewItem(sunday_layout, 6, sundaySwitch, null, null, sunday_from_to);
                    } else {
                        removeAll(6, sunday_layout, sunday_from_to);
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
                from.setText("10:00");
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
                to.setText("10:00");
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
            linearLayout1.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            ImageView minus = new ImageView(this);
            minus.setBackgroundResource(R.drawable.ic_remove_black_24dp);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getPixal(40), LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.RIGHT;
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
        List<Integer> Ids = new ArrayList<>();
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
            openingHoursIntent.putExtra(Constants.DATA, (Serializable) list);
            openingHoursIntent.putExtra("type", type);
            openingHoursIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(openingHoursIntent);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("TimeTable", "schedule: ", e);
        }

    }


    //    @OnClick(R.id.save)
    public void save() {
        try {
            Intent intent = new Intent();
            intent.putExtra("type", 0);
            int key = 0 ;
            List<Table> list = new ArrayList<>();
            while (key < 8) {
                if (map.containsKey(key)) {
                    List<Integer> Ids = map.get(key);
                    int count = 0;
                    while (count < Ids.size()) {
                        Table table = new Table();
                        table.setDayweek(String.valueOf(key));
                        TextView from = (TextView) findViewById(Ids.get(count + 2));
                        TextView to = (TextView) findViewById(Ids.get(count + 3));
                        table.setFrom(String.valueOf(from.getText()));
                        table.setTo(String.valueOf(to.getText()));
                        list.add(table);
                        count += 4;
                    }
                }
                key++;
            }
            intent.putExtra(Constants.DATA, (Serializable) list);
            try {
                if (OpeningHoursActivity.active)
                    instance.finish();
            } catch (Exception e) {
            }

            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_OK) {
                list = (List<Table>) data.getSerializableExtra(Constants.DATA);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("Specilaities", "onActivityResult", e);
        }

    }

    public void handleData(List<Table> list) {
        for (Table table : list) {
            if (table.getDayweek().equals("0"))
                addNewItem(monday_layout, 0, monadaySwitch, table.getFrom(), table.getTo(), sunday_from_to);
            else if (table.getDayweek().equals("1"))
                addNewItem(tuesday_layout, 1, tuesdaySwitch, table.getFrom(), table.getTo(), sunday_from_to);
            else if (table.getDayweek().equals("2"))
                addNewItem(wendesday_layout, 2, wednesdaySwitch, table.getFrom(), table.getTo(), sunday_from_to);
            else if (table.getDayweek().equals("3"))
                addNewItem(thurday_layout, 3, thursdaySwitch, table.getFrom(), table.getTo(), sunday_from_to);
            else if (table.getDayweek().equals("4"))
                addNewItem(friday_layout, 4, fridaySwitch, table.getFrom(), table.getTo(), sunday_from_to);
            else if (table.getDayweek().equals("5"))
                addNewItem(saturday_layout, 5, saturdaySwitch, table.getFrom(), table.getTo(), sunday_from_to);
            else if (table.getDayweek().equals("6"))
                addNewItem(sunday_layout, 6, sundaySwitch, table.getFrom(), table.getTo(), sunday_from_to);
        }
    }
    private void showTimePicker(final TextView txt){
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