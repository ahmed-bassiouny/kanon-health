package com.germanitlab.kanonhealth.forward;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.MyClickListener;
import com.germanitlab.kanonhealth.interfaces.RecyclerTouchListener;
import com.germanitlab.kanonhealth.main.MainActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ForwardActivity extends AppCompatActivity {
    /*private List<User> ListDoctors;
    private List<User> ChooseList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ForwardAdapter mAdapter;
    ArrayList<Integer> doctorsForward, messagesForward;
    @BindView(R.id.btn_forward_document)
    Button foward_document;
    Boolean search;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    PrefManager prefManager;
    int chat_doctor_id;
    Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward);
        util = Util.getInstance(this);
        try {
            prefManager = new PrefManager(this);
            doctorsForward = new ArrayList<>();
            messagesForward = new ArrayList<>();
            ButterKnife.bind(this);
            search = false;
            chat_doctor_id = getIntent().getIntExtra("chat_doctor_id", 0);
            if (chat_doctor_id == 0) {
                finish();
                Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
            }
            messagesForward = getIntent().getIntegerArrayListExtra("list");
            ListDoctors = new ArrayList<>();
            util.showProgressDialog();
            new HttpCall(getApplicationContext(), new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    try {
                        util.dismissProgressDialog();
                        ListDoctors = (List<User>) response;
                        ChooseList.addAll(ListDoctors);
                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                        setAdapter(ChooseList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);
                        addListener(recyclerView);
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                        finish();
                    }


                }

                @Override
                public void onFailed(String error) {
                    util.dismissProgressDialog();
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).getChatDoctors(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), "");
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    @OnTextChanged(value = R.id.edt_doctor_list_filter, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        try {
            if (ListDoctors != null) {
                ChooseList.clear();
                if (charSequence.toString().trim().length() > 0) {
                    for (int j = 0; j < ListDoctors.size(); j++) {

                        String name = ListDoctors.get(j).getFullName();

                        if (name != null && name.toLowerCase().contains(charSequence.toString().toLowerCase())) {

                            ChooseList.add(ListDoctors.get(j));
                        }
                        search = true;
                    }
                    setAdapter(ChooseList);
                } else {
                    setAdapter(ListDoctors);
                    search = false;
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick(R.id.imgbtn_forward)
    public void forwardTo(View view) {
        try {
            if (doctorsForward.size() > 0)
                sendForward();
            else
                Toast.makeText(getApplicationContext(), R.string.please_select_a_contact, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void sendForward() {
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                try {
                    Toast.makeText(ForwardActivity.this, R.string.message_sent, Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(String error) {
                Log.e("Error", error);
                Toast.makeText(ForwardActivity.this, getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        }).forward(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), ListtoString(messagesForward), ListtoString(doctorsForward));
    }

    private void addListener(RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new MyClickListener() {
            @Override
            public void onClick(View view, int position) {
                addToList(view, position);
            }

            @Override
            public void onClick(Object object) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void addToList(View view, int position) {
        try {
            PercentRelativeLayout percentRelativeLayout = (PercentRelativeLayout) view.findViewById(R.id.myrow);
            if (ChooseList.size() == 0) {
                if (!doctorsForward.contains(ListDoctors.get(position).get_Id())) {
                    doctorsForward.add(ListDoctors.get(position).get_Id());
                    percentRelativeLayout.setBackgroundResource(R.color.gray_black);
                    ListDoctors.get(position).setChosen(true);
                } else {
                    doctorsForward.remove(doctorsForward.indexOf(ListDoctors.get(position).get_Id()));
                    percentRelativeLayout.setBackgroundResource(0);
                    ListDoctors.get(position).setChosen(false);
                }
            } else {
                if (!doctorsForward.contains(ChooseList.get(position).get_Id())) {
                    doctorsForward.add(ChooseList.get(position).get_Id());
                    percentRelativeLayout.setBackgroundResource(R.color.gray_black);
                    ChooseList.get(position).setChosen(true);
                } else {
                    doctorsForward.remove(doctorsForward.indexOf(ChooseList.get(position).get_Id()));
                    percentRelativeLayout.setBackgroundResource(0);
                    ChooseList.get(position).setChosen(false);
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void setAdapter(List<User> DoctorList) {
        try {
            if (DoctorList != null) {
                mAdapter = new ForwardAdapter(DoctorList, this);
                recyclerView.setAdapter(mAdapter);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.btn_forward_document)
    public void toDocument(View view) {
        try {
            doctorsForward.clear();
            doctorsForward.add(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));
            sendForward();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.scan)
    public void scanId(View view) {
        askForPermission();
    }

    private void askForPermission() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //Asking for the camera permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                //Opening the QR Scanner
                new IntentIntegrator(this).initiateScan();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    new IntentIntegrator(this).initiateScan();
                } else {

                    askForPermission();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, R.string.cancelled, Toast.LENGTH_LONG).show();
                } else {
/*
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
*/
     /*               final String key = result.getContents();
                    sendRequest(key);

                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void sendRequest(String key) {
        util.showProgressDialog();
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                try {
                    User user = (User) response;
                    doctorsForward.clear();
                    doctorsForward.add(user.get_Id());
                    sendForward();
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                Log.e("My error ", error.toString());

            }
        }).getDoctor(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), key);
    }

    private String ListtoString(List<Integer> list) {
        String result = "";
        for (Integer item : list) {
            result += item + ",";
        }
        return result.substring(0, (result.length() - 1));
    }
*/
}
