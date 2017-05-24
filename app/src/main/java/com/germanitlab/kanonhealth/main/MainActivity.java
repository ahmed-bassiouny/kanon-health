package com.germanitlab.kanonhealth.main;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.doctors.ChatsDoctorFragment;
import com.germanitlab.kanonhealth.doctors.DoctorListFragment;
import com.germanitlab.kanonhealth.doctors.DoctorListMapFragment;
import com.germanitlab.kanonhealth.documents.DocumentsChatFragment;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.interfaces.FilterCallBackClickListener;
import com.germanitlab.kanonhealth.interfaces.OnImgDoctorListMapClick;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.settings.SettingFragment;

public class MainActivity extends AppCompatActivity implements OnImgDoctorListMapClick {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static TextView tab0, tab1, tab2, tab4;
    int speciality_id;

    ViewPagerAdapter adapter;

    private int[] tabIcons = {
            R.drawable.doctorr,
            R.drawable.my_document,
            R.drawable.chat,
            R.drawable.settings
    };

    private FilterCallBackClickListener filterCallBackClickListener;


    private int tabIndex;
    private boolean temp;
    private int type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        tabIndex = intent.getIntExtra("index", 0);
        if (tabIndex == -1) {
            tabIndex = 0;
            temp = true;
        }
        //-- set status open when open activity
        AppController.getInstance().getSocket().on("ChatMessageReceive", handleIncomingMessages);

        tabLayout = (TabLayout) findViewById(R.id.home_tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        speciality_id = intent.getIntExtra("speciality_id", 0);
        type = intent.getIntExtra("type", 2);
        setupViewPager(viewPager, speciality_id , type);

        tabLayout.setupWithViewPager(viewPager);

        setupCustomTab();

        viewPager.setCurrentItem(tabIndex);

        tab0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Helper(MainActivity.this).replaceFragments(new DoctorListFragment(),
                        R.id.doctor_list_continer, "doctorList");
                viewPager.setCurrentItem(0);

            }
        });

        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Helper(MainActivity.this).replaceFragments(new ChatsDoctorFragment(),
                        R.id.doctor_list_continer, "chats");
                viewPager.setCurrentItem(2);
            }
        });


    }

    private void askForPermission(String[] permission, Integer requestCode) {
        ActivityCompat.requestPermissions(this, permission, requestCode);
    }


    private void setupCustomTab() {

        tab0 = (TextView) LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_text, null);
        tab0.setText(getString(R.string.docor_list));
        tab0.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[0], 0, 0);
        tab0.setTag("doctorList");
        tabLayout.getTabAt(0).setCustomView(tab0);


        tab1 = (TextView) LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_text, null);
        tab1.setText(getResources().getString(R.string.my_decument));
        tab1.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[1], 0, 0);
        tab0.setTag("myDocument");
        tabLayout.getTabAt(1).setCustomView(tab1);

        tab2 = (TextView) LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_text, null);
        tab2.setText(getResources().getString(R.string.chats));
        tab2.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[2], 0, 0);
        tab2.setTag("chats");
        tabLayout.getTabAt(2).setCustomView(tab2);

        tab4 = (TextView) LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_text, null);
        tab4.setText(getResources().getString(R.string.setting));
        tab4.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[3], 0, 0);
        tab4.setTag("setting");
        tabLayout.getTabAt(3).setCustomView(tab4);
    }


    /**************************************************/
    private void setupViewPager(ViewPager viewPager, int speciality_id , int type) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new DoctorListFragment(speciality_id , type), getString(R.string.all_doctors));
        adapter.addFragment(new DocumentsChatFragment(this), getString(R.string.my_decument));
        adapter.addFragment(new ChatsDoctorFragment(), getString(R.string.chats));
        adapter.addFragment(new SettingFragment(), getString(R.string.setting));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void OnImgDoctorListMapClick() {

        new Helper(MainActivity.this).replaceFragments(new DoctorListMapFragment(MainActivity.this),
                R.id.doctor_list_continer, "DoctorListMapFragment");
//        tab2.setText(getString(R.string.docor_list));
//        tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.docto_list_icon, 0, 0);
//        tab2.setTag("doctorList");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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


    private void handelMessage(String message) {

        Gson gson = new Gson();
        Message incomingMessage = gson.fromJson(message, Message.class);

        incomingMessage.setMine(false);
        Log.d("incoming Message ", incomingMessage.toString());

//        if (chatFragment == null) {//--- Not have any instance form chatFragment .
//            showNotification(message, 1, message.toString());
//
//        } else if (!chatFragment.isVisible() && chatFragment.doctor == null) {
//
//            Log.e("chat tab", "not open any chat details");
//            showNotification(message, 1, message.toString());
//        }
//        //open chat details but another user
//        else if (chatFragment.isVisible() && chatFragment.doctor.get_Id() != incomingMessage.getFrom_id()) {
//            Log.e("chat tab", "open chat details but another user");
//            showNotification(message, 1, message.toString());
//        }
//        //open same user but screen off
//        else if (!chatFragment.isVisible() && chatFragment.doctor.get_Id() == incomingMessage.getFrom_id()) {
//            Log.e("chat tab", "open same user but screen off");
//            chatFragment.addMessage(incomingMessage);
//            showNotification(message, 1, message.toString());
//        }
//        //open same user and screen light
//        else if (chatFragment.isVisible() && chatFragment.doctor.get_Id() == incomingMessage.getFrom_id()) {
//
//            Log.e("chat tab", "open same user and screen light");
//
//            chatFragment.addMessage(incomingMessage);
//        } else {
//
//            Log.e("------------------ chat tab", "last else");
//        }

    }


    private void showNotification(String message, int type, String obj) {


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("New Message")
                        .setSound(uri)
                        .setAutoCancel(true)
                        .setContentText(message);

        Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
        notificationIntent.putExtra("message", message);
        notificationIntent.putExtra("type", type);
        notificationIntent.putExtra("notification_type", Constants.OPEN_CHAT);
        notificationIntent.putExtra("userObj", obj);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 2, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//
//        int notificationType = intent.getIntExtra("notification_type", 0);
//        if (notificationType == Constants.OPEN_CHAT) {
//
//
//            tab2.setText(getString(R.string.request));
//            tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.request, 0, 0);
//            tab2.setTag("request");
//            User doctor = new User();
//
//            Log.d("User", intent.getStringExtra("message"));
//
//            doctor.getDoctor().setId(new Gson().fromJson(intent.getStringExtra("message"), Message.class).getFrom_id());
//            Log.d("User", doctor.toString());
//
//            chatFragment = getFragmentFromMap(doctor.get_Id(), doctor, MainActivity.this);
//
//            chatFragment = new ChatFragment(doctor , MainActivity.this);
//            new Helper(MainActivity.this).replaceFragments(chatFragment, R.id.doctor_list_continer, "request");
//        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanResult != null) {

            showDialog(scanResult.getContents());
        } else {

            Toast.makeText(MainActivity.this, "Invalid Qr please try again", Toast.LENGTH_LONG).show();

        }
    }


    private void showDialog(final String qr) {

        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.custom_dialog);

        Button btnLogin = (Button) dialog.findViewById(R.id.btn_login);

        Button btnShowProfile = (Button) dialog.findViewById(R.id.btn_show_profile);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new HttpCall(MainActivity.this, new ApiResponse() {
                    @Override
                    public void onSuccess(Object response) {

                        Log.d("Response", response.toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailed(String error) {

                        Log.d("Response", error);
                        dialog.dismiss();
                    }
                }).login(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                        , AppController.getInstance().getClientInfo().getPassword(), qr);
            }
        });


        btnShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Möchtest du aussteigen ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ja",
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
                "Nein",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    //    @Override
//    public void onBackPressed() {
//        int backStack = getSupportFragmentManager().getBackStackEntryCount();
//
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            finish();
//        } else {
//            // reset selected icons
//
//            super.onBackPressed();
//
//            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
//
//
//            if (backStackEntryCount == 0) {
//                tab2.setText(getString(R.string.chats));
//                tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chat, 0, 0);
//                tab2.setTag("chats");
//            } else {
//
//                FragmentManager.BackStackEntry backStackEntryAt = getSupportFragmentManager().getBackStackEntryAt(backStackEntryCount - 1);
//                String fragmentTag = backStackEntryAt.getName();
//                Log.e("fragment tag", fragmentTag);
//                if (fragmentTag.equalsIgnoreCase("chats")) {
//
//
//                    tab2.setText(getString(R.string.chats));
//                    tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chat, 0, 0);
//                    tab2.setTag("chats");
//
//                } else if (fragmentTag.equals("doctorList")) {
//                    tab2.setText(getString(R.string.docor_list));
//                    tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.docto_list_icon, 0, 0);
//                    tab2.setTag("doctorList");
//                }
//
//            }
//        }
//
//    }
}
