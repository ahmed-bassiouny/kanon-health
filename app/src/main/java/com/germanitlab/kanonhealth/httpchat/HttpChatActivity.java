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
    private MenuItem endSession;
    private MenuItem startSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_chat);
         doctorID = getIntent().getIntExtra("doctorID",0);
        String doctorName = getIntent().getStringExtra("doctorName");
        String doctorUrl = getIntent().getStringExtra("doctorUrl");
        if (doctorID==0)
            finish();

        Bundle bundle = new Bundle();
        bundle.putInt("doctorID", doctorID);
        bundle.putString("doctorName",doctorName);
        bundle.putString("doctorUrl",doctorUrl);

        HttpChatFragment fr = new HttpChatFragment();
        fr.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main, fr).commit();
        Gson gson=new Gson();
        try {
            prefManager = new PrefManager(this);

            User user = gson.fromJson(prefManager.getData(prefManager.USER_INTENT), User.class);
            doctor = user;
        }catch (Exception e){
            Toast.makeText(HttpChatActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        endSession = menu.findItem(R.id.end_session);
        startSession = menu.findItem(R.id.start_session);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start_session:
                try {
                    AlertDialog.Builder adb_end = new AlertDialog.Builder(this);
                    adb_end.setTitle(R.string.close_conversation);
                    adb_end.setCancelable(false);
                    adb_end.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new HttpCall(HttpChatActivity.this, new ApiResponse() {
                                @Override
                                public void onSuccess(Object response) {


                                    Toast.makeText(HttpChatActivity.this, R.string.session_ended, Toast.LENGTH_SHORT).show();
                                    doctor.setIsOpen(0);
                                    if (doctor.isClinic == 1) {
                                        Intent intent = new Intent(HttpChatActivity.this, InquiryActivity.class);
                                        UserInfoResponse userInfoResponse = new UserInfoResponse();
                                        userInfoResponse.setUser(doctor);
                                        Gson gson = new Gson();
                                        intent.putExtra("doctor_data", gson.toJson(userInfoResponse));
                                        startActivity(intent);
                                    } else if (doctor.getIsDoc() == 1) {
                                        openpayment();
                                    } else {
                                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailed(String error) {
                                    Toast.makeText(HttpChatActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
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
                            new HttpCall(HttpChatActivity.this, new ApiResponse() {
                                @Override
                                public void onSuccess(Object response) {
                                    try {

                                        Toast.makeText(HttpChatActivity.this, R.string.session_ended, Toast.LENGTH_SHORT).show();
                                        doctor.setIsOpen(0);
                                        if (doctor.isClinic == 1) {
                                            AlertDialog.Builder adb = new AlertDialog.Builder(HttpChatActivity.this);
                                            adb.setTitle(R.string.rate_conversation);
                                            adb.setCancelable(true);
                                            adb.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    new HttpCall(HttpChatActivity.this, new ApiResponse() {
                                                        @Override
                                                        public void onSuccess(Object response) {
                                                            Intent intent = new Intent(HttpChatActivity.this, Comment.class);
                                                            intent.putExtra("doc_id", doctorID);
                                                            startActivity(intent);
                                                        }

                                                        @Override
                                                        public void onFailed(String error) {
                                                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).closeSession(doctorID+"");
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
                                    Toast.makeText(HttpChatActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                                }
                            }).closeSession(doctorID+"");
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
    private void openpayment(){
        try {
            if (doctor.isClinic == null)
                doctor.setIsClinic(0);

            if (doctor.isClinic == 1) {
                Intent intent = new Intent(this, DoctorProfileActivity.class);
                intent.putExtra("doctor_data", doctor);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, PaymentActivity.class);
                Gson gson = new Gson();
                prefManager.put(prefManager.USER_INTENT, gson.toJson(doctor));
                intent.putExtra("doctor_obj", doctor);

                startActivity(intent);
                finish();
            }

        } catch (
                Exception e)

        {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

}
