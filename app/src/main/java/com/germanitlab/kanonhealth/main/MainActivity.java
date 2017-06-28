package com.germanitlab.kanonhealth.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.doctors.ChatsDoctorFragment;
import com.germanitlab.kanonhealth.doctors.DoctorListFragment;
import com.germanitlab.kanonhealth.doctors.DoctorListMapFragment;
import com.germanitlab.kanonhealth.documents.DocumentsChatFragment;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.interfaces.OnImgDoctorListMapClick;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.settings.SettingFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

import static com.germanitlab.kanonhealth.chat.ChatActivity.indexFromIntent;

public class MainActivity extends AppCompatActivity implements OnImgDoctorListMapClick {


    private TabLayout mytablayout;
    private ViewPager myviewpager;
    int speciality_id;
    private int type;
    Intent intent;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            AppController.getInstance().getSocket().on("ChatMessageReceive", handleIncomingMessages);
            tvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
            initTB();
            mytablayout = (TabLayout) findViewById(R.id.mytablayout);
            myviewpager = (ViewPager) findViewById(R.id.myviewpager);


            myviewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));


            mytablayout.setupWithViewPager(myviewpager);
            setupTabIcons();
            intent = getIntent();

            myviewpager.setCurrentItem(indexFromIntent);

            speciality_id = intent.getIntExtra("speciality_id", 0);
            type = intent.getIntExtra("type", 2);


            myviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mytablayout.getTabAt(position).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    mytablayout.getTabAt(position).select();
                    invalidateOptionsMenu();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mytablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tvToolbarTitle.setText(tab.getText().toString());
                    myviewpager.setCurrentItem(tab.getPosition(), false);
                    tab.getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    invalidateOptionsMenu();

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void initTB() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        tvToolbarTitle.setText("Contacts");
    }


    @Override
    public void OnImgDoctorListMapClick() {

        new Helper(MainActivity.this).replaceFragments(new DoctorListMapFragment(MainActivity.this),
                R.id.doctor_list_continer, "DoctorListMapFragment");
    }

    private Emitter.Listener handleIncomingMessages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("Incoming Message", args[0].toString());

                    try {
                        if (data.get("type").equals(Constants.LOCATION)) {
                            String msgLoc = data.getString("msg");
                            JSONObject jsonObject = new JSONObject(msgLoc);
                            double lat = jsonObject.getDouble("lat");
                            double lng = jsonObject.getDouble("long");
                            data.put("msg", "long:" + lng + ",lat:" + lat);
                            handelMessage(data.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handelMessage(data.toString());
                    }


                }
            });
        }
    };


    private void setupTabIcons() {
//        mytablayout.getTabAt(0).setIcon(R.drawable.ic_contacts_black_24dp);
//        mytablayout.getTabAt(1).setIcon(R.drawable.ic_assignment_black_24dp);
//        mytablayout.getTabAt(2).setIcon(R.drawable.ic_chat_black_24dp);
//        mytablayout.getTabAt(3).setIcon(R.drawable.ic_settings_black_24dp);

        mytablayout.getTabAt(0).setIcon(R.drawable.doctor_tab);
        mytablayout.getTabAt(1).setIcon(R.drawable.document_tab);
        mytablayout.getTabAt(2).setIcon(R.drawable.chat_tab);
        mytablayout.getTabAt(3).setIcon(R.drawable.setting_tab);

        mytablayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        mytablayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        mytablayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        mytablayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);

    }

    private void handelMessage(String message) {

        Gson gson = new Gson();
        Message incomingMessage = gson.fromJson(message, Message.class);

        incomingMessage.setMine(false);
        Log.d("incoming Message ", incomingMessage.toString());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (myviewpager.getCurrentItem() != 0) {
            myviewpager.setCurrentItem(0);
            return;
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to close app?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return DoctorListFragment.newInstance(speciality_id, type);
                case 1:
                    return DocumentsChatFragment.newInstance();
                case 2:
                    return ChatsDoctorFragment.newInstance();
                case 3:
                    return SettingFragment.newInstance();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Contact";
                case 1:
                    return "Documents";
                case 2:
                    return "Chat";
                case 3:
                    return "Settings";
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

}