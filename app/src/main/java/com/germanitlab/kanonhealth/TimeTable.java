package com.germanitlab.kanonhealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.germanitlab.kanonhealth.models.Table;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table_activity);
        ButterKnife.bind(this);
        map = new HashMap<>();
/*        monadaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               *//* else {
                    final int count = monday_layout.getChildCount();
                    for( int i = 0 ; i < count ;  i++){
                        View v = monday_layout.getChildAt(i);
                        monday_layout.removeView(v);
                    }

                }*//*
            }
        });
        tuesdaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    addNewItem(tuesday_layout);
               *//* else {
                    final int count = monday_layout.getChildCount();
                    for( int i = 0 ; i < count ;  i++){
                        View v = monday_layout.getChildAt(i);
                        monday_layout.removeView(v);
                    }

                }*//*
            }
        });*/
        getFromServer();

    }


    @OnCheckedChanged({R.id.monday_switch, R.id.tuesday_switch, R.id.wednesday_switch, R.id.thursday_switch, R.id.friday_switch, R.id.saturday_switch, R.id.sunday_switch})
    public void checkboxToggled(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.monday_switch:
                if (isChecked) {
                    if(checkKeys(1))
                    addNewItem(monday_layout, 1, monadaySwitch, null, null);
                } else {
                    removeAll(1, monday_layout);

                }
                break;
            case R.id.tuesday_switch:
                if (isChecked) {
                    if(checkKeys(2))
                        addNewItem(tuesday_layout, 2, tuesdaySwitch, null, null);
                } else {
                    removeAll(2, tuesday_layout);
                }
                break;
            case R.id.wednesday_switch:
                if (isChecked) {
                    if(checkKeys(3))
                        addNewItem(wendesday_layout, 3, wednesdaySwitch, null, null);
                } else {
                    removeAll(3, wendesday_layout);
                }
                break;
            case R.id.thursday_switch:
                if (isChecked) {
                    if(checkKeys(4))
                    addNewItem(thurday_layout, 4, thursdaySwitch, null, null);
                } else {
                    removeAll(4, thurday_layout);
                }
                break;
            case R.id.friday_switch:
                if (isChecked) {
                    if(checkKeys(5))
                    addNewItem(friday_layout, 5, fridaySwitch, null, null);
                } else {
                    removeAll(5, friday_layout);
                }
                break;
            case R.id.saturday_switch:
                if (isChecked) {
                    if(checkKeys(6))
                    addNewItem(saturday_layout, 6, saturdaySwitch, null, null);
                } else {
                    removeAll(6, saturday_layout);
                }
                break;
            case R.id.sunday_switch:
                if (isChecked) {
                    if(checkKeys(7))
                    addNewItem(sunday_layout, 7, sundaySwitch, null, null);
                } else {
                    removeAll(7, sunday_layout);
                }
                break;
        }

    }

    private void removeAll(int key, LinearLayout layout) {
        if (!checkKeys(key)) {
            List<Integer> Ids = getArray(key);
            for (int i = 0; i < Ids.size(); i++) {
                layout.removeView(findViewById(Ids.get(i)));
                layout.removeView(findViewById(Ids.get(++i)));

            }
            map.remove(key);
        }
    }


    private void addNewItem(final LinearLayout layout, final int day, final Switch tempSwitch, String tfrom, String tto) {
        final LinearLayout line = new LinearLayout(this);
        final int lineId = line.generateViewId();
        line.setId(lineId);
        line.setMinimumWidth(1);
        line.setMinimumHeight(1);
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
                Calendar now = Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                if (minute < 10)
                                    from.setText(hourOfDay + ":0" + minute);
                                else
                                    from.setText(hourOfDay + ":" + minute);
                            }
                        },
                        now.get(Calendar.MINUTE),
                        now.get(Calendar.SECOND),
                        false
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
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
                getPixal(1), LinearLayout.LayoutParams.MATCH_PARENT));
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
                Calendar now = Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                if (minute < 10)
                                    to.setText(hourOfDay + ":0" + minute);
                                else
                                    to.setText(hourOfDay + ":" + minute);
                            }
                        },
                        now.get(Calendar.HOUR),
                        now.get(Calendar.MINUTE),
                        false
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
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
                if (checkKeys(day))
                    tempSwitch.setChecked(false);
            }
        });
        linearLayout1.addView(minus);
        final ImageView add = new ImageView(this);
        add.setBackgroundResource(R.drawable.ic_add_black_24dp);
        add.setLayoutParams(layoutParams);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItem(layout, day, tempSwitch , null , null);
            }
        });
        linearLayout1.addView(add);
        linearLayout.addView(linearLayout1);
        layout.addView(linearLayout);
        tempSwitch.setChecked(true);
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
        if(map.containsKey(day)) {
            List<Integer> Ids = map.get(day);
            if (Ids.size() == 0)
                return true;
            else
                return false;
        }
        else return true ;
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

    @OnClick(R.id.save)
    public void save(View view) {
        int key = 1;
        List<Table> list = new ArrayList<>();
        while (key < 8) {
            if (map.containsKey(key)) {
                List<Integer> Ids = map.get(key);
                int count = 0;
                while (count < Ids.size()) {
                    Table table = new Table();
                    table.setDayOfWeek(String.valueOf(key));
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
        Toast.makeText(this, new Gson().toJson(list), Toast.LENGTH_SHORT).show();
    }

    public void getFromServer() {
        String json = "[{\"dayOfWeek\":\"1\",\"from\":\"10:00\",\"to\":\"2.24\"},{\"dayOfWeek\":\"1\",\"from\":\"11:05\",\"to\":\"15:44\"},{\"dayOfWeek\":\"2\",\"from\":\"10:44\",\"to\":\"17.50\"}]";
        List<Table> list = new Gson().fromJson(json, new TypeToken<List<Table>>() {
        }.getType());
        for (Table table : list) {
            if (table.getDayOfWeek().equals("1"))
                addNewItem(monday_layout, 1, monadaySwitch, table.getFrom(), table.getTo());
            else if (table.getDayOfWeek().equals("2"))
                addNewItem(tuesday_layout, 2, tuesdaySwitch, table.getFrom(), table.getTo());
            else if (table.getDayOfWeek().equals("3"))
                addNewItem(wendesday_layout, 3, wednesdaySwitch, table.getFrom(), table.getTo());
            else if (table.getDayOfWeek().equals("4"))
                addNewItem(thurday_layout, 4, thursdaySwitch, table.getFrom(), table.getTo());
            else if (table.getDayOfWeek().equals("5"))
                addNewItem(friday_layout, 5, fridaySwitch, table.getFrom(), table.getTo());
            else if (table.getDayOfWeek().equals("6"))
                addNewItem(saturday_layout, 6, saturdaySwitch, table.getFrom(), table.getTo());
            else if (table.getDayOfWeek().equals("7"))
                addNewItem(sunday_layout, 7, sundaySwitch, table.getFrom(), table.getTo());
        }
    }
}