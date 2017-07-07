package com.germanitlab.kanonhealth.httpchat;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.Comment;
import com.germanitlab.kanonhealth.DoctorProfileActivity;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.chat.ChatActivity;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.inquiry.InquiryActivity;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.payment.PaymentActivity;
import com.google.gson.Gson;

public class HttpChatActivity extends AppCompatActivity {

    String doctorName = "";
    String doctorUrl="";
    int doctorID;
    User doctor;
    PrefManager prefManager;
    HttpChatFragment fr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_chat);
         doctorID = getIntent().getIntExtra("doctorID",0);
         doctorName = getIntent().getStringExtra("doctorName");
         doctorUrl = getIntent().getStringExtra("doctorUrl");
        if (doctorID==0)
            finish();


        Bundle bundle = new Bundle();
        bundle.putInt("doctorID", doctorID);
        bundle.putString("doctorName",doctorName);
        bundle.putString("doctorUrl",doctorUrl);

         fr = new HttpChatFragment();
        fr.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main, fr).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setSupportActionBar(fr.gettoolbar());
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case R.id.start_session:
                try {
                    AlertDialog.Builder adb_end = new AlertDialog.Builder(this);
                    adb_end.setTitle(R.string.close_conversation);
                    adb_end.setCancelable(false);
                    adb_end.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new HttpCall(ChatActivity.this, new ApiResponse() {
                                @Override
                                public void onSuccess(Object response) {


                                    Toast.makeText(ChatActivity.this, R.string.session_ended, Toast.LENGTH_SHORT).show();
                                    doctor.setIsOpen(0);
                                    checkSessionOpen();
                                    if (doctor.isClinic == 1) {
                                        Intent intent = new Intent(ChatActivity.this, InquiryActivity.class);
                                        UserInfoResponse userInfoResponse = new UserInfoResponse();
                                        userInfoResponse.setUser(doctor);
                                        Gson gson = new Gson();
                                        intent.putExtra("doctor_data", gson.toJson(userInfoResponse));
                                        startActivity(intent);
                                    } else if (doctor.getIsDoc() == 1) {
                                        openPayment();
                                    } else {
                                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailed(String error) {
                                    Toast.makeText(ChatActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                                }
                            }).closeSession(String.valueOf(doctor.getId()));
                        }

                    });
                    adb_end.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    adb_end.show();
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.end_session:
                try {
                    AlertDialog.Builder adb_end = new AlertDialog.Builder(this);
                    adb_end.setTitle(R.string.close_conversation);
                    adb_end.setCancelable(false);
                    adb_end.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new HttpCall(ChatActivity.this, new ApiResponse() {
                                @Override
                                public void onSuccess(Object response) {
                                    try {

                                        Toast.makeText(ChatActivity.this, R.string.session_ended, Toast.LENGTH_SHORT).show();
                                        doctor.setIsOpen(0);
                                        checkSessionOpen();
                                        if (doctor.isClinic == 1) {
                                            AlertDialog.Builder adb = new AlertDialog.Builder(ChatActivity.this);
                                            adb.setTitle(R.string.rate_conversation);
                                            adb.setCancelable(true);
                                            adb.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    new HttpCall(ChatActivity.this, new ApiResponse() {
                                                        @Override
                                                        public void onSuccess(Object response) {
                                                            Intent intent = new Intent(ChatActivity.this, Comment.class);
                                                            intent.putExtra("doc_id", String.valueOf(doctor.get_Id()));
                                                            startActivity(intent);
                                                        }

                                                        @Override
                                                        public void onFailed(String error) {
                                                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).closeSession(String.valueOf(doctor.getId()));
                                                }
                                            });
                                            adb.show();
                                        }
                                    } catch (Exception e) {
                                        Crashlytics.logException(e);
                                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailed(String error) {
                                    Toast.makeText(ChatActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                                }
                            }).closeSession(String.valueOf(doctor.getId()));
                        }

                    });
                    adb_end.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    adb_end.show();


                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
*/
}
