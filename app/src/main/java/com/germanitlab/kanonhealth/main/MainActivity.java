package com.germanitlab.kanonhealth.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.FCViewPager;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.doctors.ChatsDoctorFragment;
import com.germanitlab.kanonhealth.doctors.DoctorListFragment;
import com.germanitlab.kanonhealth.doctors.DoctorListMapFragment;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.httpchat.HttpChatFragment;
import com.germanitlab.kanonhealth.interfaces.OnImgDoctorListMapClick;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.settings.SettingFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ParentActivity {


    private TabLayout mytablayout;
    private FCViewPager myviewpager;
    int speciality_id;
    private int type;
    Intent intent;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    public AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            tvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
            initTB();
            mytablayout = (TabLayout) findViewById(R.id.mytablayout);
            myviewpager = (FCViewPager) findViewById(R.id.myviewpager);
            appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

            toolbar.inflateMenu(R.menu.contacts_menu);

            myviewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));


            mytablayout.setupWithViewPager(myviewpager);

            setupTabIcons();
            intent = getIntent();

            myviewpager.setCurrentItem(0);

//            speciality_id = intent.getIntExtra("speciality_id", 0);
//            type = intent.getIntExtra("type", 2);


            myviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mytablayout.getTabAt(position).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    mytablayout.getTabAt(position).select();
                    toolbar.getMenu().clear();
                    invalidateOptionsMenu();
                    if (position == 0)
                        toolbar.inflateMenu(R.menu.contacts_menu);
                    else if (position == 1)
                        toolbar.inflateMenu(R.menu.menu_document);
                    else if (position == 2)
                        toolbar.inflateMenu(R.menu.contacts_menu);
                    else if (position == 3)
                        toolbar.inflateMenu(R.menu.menu_settings);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean touchesAllowed = true;
        if (touchesAllowed) {
            return super.onTouchEvent(event);
        } else {
            return MotionEventCompat.getActionMasked(event) != MotionEvent.ACTION_MOVE && super.onTouchEvent(event);
        }
    }

    private void initTB() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        tvToolbarTitle.setText("Contacts");
    }


    /*@Override
    public void OnImgDoctorListMapClick() {

        new Helper(MainActivity.this).replaceFragments(new DoctorListMapFragment(),
                R.id.doctor_list_continer, "DoctorListMapFragment");
    }*/


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
        builder1.setMessage(R.string.do_you_want_to_close_app);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.addCategory(Intent.CATEGORY_HOME);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });

        builder1.setNegativeButton(
                R.string.cancel,
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
                    return DoctorListFragment.newInstance();
                case 1:
                    return HttpChatFragment.newInstance(PrefHelper.get(MainActivity.this, PrefHelper.KEY_USER_ID, -1));
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
                    return getResources().getString(R.string.contacts);
                case 1:
                    return getResources().getString(R.string.documents);
                case 2:
                    return getResources().getString(R.string.chat);
                case 3:
                    return getResources().getString(R.string.settings);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }


}