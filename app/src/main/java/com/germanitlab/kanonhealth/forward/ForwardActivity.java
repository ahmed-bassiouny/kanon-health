package com.germanitlab.kanonhealth.forward;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.MyClickListener;
import com.germanitlab.kanonhealth.interfaces.RecyclerTouchListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ForwardActivity extends ParentActivity {
    private ArrayList<ChatModel> listDoctors;
    private List<ChatModel> chooseList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ForwardAdapter mAdapter;
    HashMap<String, Integer> messagesForward;
    ArrayList<Integer> doctorsForward;
    ArrayList<Integer> doctorsForwardType;
    @BindView(R.id.btn_forward_document)
    Button foward_document;
    Boolean search;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    int chat_doctor_id;
    Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward);
        util = Util.getInstance(this);
        doctorsForward = new ArrayList<>();
        doctorsForwardType = new ArrayList<>();
        messagesForward = new HashMap<String, Integer>();
        ButterKnife.bind(this);
        search = false;
        chat_doctor_id = getIntent().getIntExtra("chat_doctor_id", 0);
        if (chat_doctor_id == 0) {
            finish();
            Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
        }
        messagesForward = (HashMap<String, Integer>) getIntent().getSerializableExtra("list");
        listDoctors = new ArrayList<>();
        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {

                listDoctors = ApiHelper.getChatAll(ForwardActivity.this, String.valueOf(PrefHelper.get(ForwardActivity.this, PrefHelper.KEY_USER_ID, 0)));
                ForwardActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listDoctors.size() > 0) {
                            chooseList.addAll(listDoctors);
                            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                            setAdapter(chooseList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                            addListener(recyclerView);
                            hideProgressBar();
                        } else {
                            hideProgressBar();
                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        }).start();
    }

    @OnTextChanged(value = R.id.edt_doctor_list_filter, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        try {
            if (listDoctors != null) {
                chooseList.clear();
                if (charSequence.toString().trim().length() > 0) {
                    for (int j = 0; j < listDoctors.size(); j++) {

                        String name = listDoctors.get(j).getFullName();

                        if (name != null && name.toLowerCase().contains(charSequence.toString().toLowerCase())) {

                            chooseList.add(listDoctors.get(j));
                        }
                        search = true;
                    }
                    setAdapter(chooseList);
                } else {
                    setAdapter(listDoctors);
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
        if (doctorsForward.size() != doctorsForwardType.size()) {
            Toast.makeText(ForwardActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Integer> doctorWithType = new HashMap<String, Integer>();
                for(int index = 0 ; index < doctorsForward.size(); index++){
                    doctorWithType.put(doctorsForward.get(index).toString(),doctorsForwardType.get(index));
                }
                final boolean result = ApiHelper.sendForward(ForwardActivity.this, PrefHelper.get(ForwardActivity.this, PrefHelper.KEY_USER_ID, 0), doctorWithType, messagesForward);
                ForwardActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result) {
                            Toast.makeText(ForwardActivity.this, R.string.message_sent, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.message_not_send), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }

    private void addListener(RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new MyClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(listDoctors.get(position).getUserType()==UserInfo.CLINIC)
                    addToListClinic(view, position);
                else
                    addToListDoctor(view, position);
            }

            @Override
            public void onClick(Object object) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void addToListDoctor(View view, int position) {
        try {
            PercentRelativeLayout percentRelativeLayout = (PercentRelativeLayout) view.findViewById(R.id.myrow);
            if (chooseList.size() == 0) {
                if (!doctorsForward.contains(listDoctors.get(position).getUserID())) {
                    doctorsForward.add(listDoctors.get(position).getUserID());
                    doctorsForwardType.add(listDoctors.get(position).getUserType());
                    percentRelativeLayout.setBackgroundResource(R.color.gray_black);
                    //listDoctors.get(position).setChosen(true);
                } else {
                    int index = doctorsForward.indexOf(listDoctors.get(position).getUserID());
                    doctorsForward.remove(index);
                    doctorsForwardType.remove(index);
                    percentRelativeLayout.setBackgroundResource(0);
                    //listDoctors.get(position).setChosen(false);
                }
            } else {
                if (!doctorsForward.contains(chooseList.get(position).getUserID())) {
                    doctorsForward.add(chooseList.get(position).getUserID());
                    doctorsForwardType.add(chooseList.get(position).getUserType());
                    percentRelativeLayout.setBackgroundResource(R.color.gray_black);
                    //chooseList.get(position).setChosen(true);
                } else {
                    int index = doctorsForward.indexOf(chooseList.get(position).getUserID());
                    doctorsForward.remove(index);
                    doctorsForwardType.remove(index);
                    percentRelativeLayout.setBackgroundResource(0);
                    //chooseList.get(position).setChosen(false);
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void addToListClinic(View view, int position) {
        try {
            PercentRelativeLayout percentRelativeLayout = (PercentRelativeLayout) view.findViewById(R.id.myrow);
            if (chooseList.size() == 0) {
                if (!doctorsForward.contains(listDoctors.get(position).getId())) {
                    doctorsForward.add(listDoctors.get(position).getId());
                    doctorsForwardType.add(listDoctors.get(position).getUserType());
                    percentRelativeLayout.setBackgroundResource(R.color.gray_black);
                    //listDoctors.get(position).setChosen(true);
                } else {
                    int index = doctorsForward.indexOf(listDoctors.get(position).getId());
                    doctorsForward.remove(index);
                    doctorsForwardType.remove(index);
                    percentRelativeLayout.setBackgroundResource(0);
                    //listDoctors.get(position).setChosen(false);
                }
            } else {
                if (!doctorsForward.contains(chooseList.get(position).getId())) {
                    doctorsForward.add(chooseList.get(position).getId());
                    doctorsForwardType.add(chooseList.get(position).getUserType());
                    percentRelativeLayout.setBackgroundResource(R.color.gray_black);
                    //chooseList.get(position).setChosen(true);
                } else {
                    int index = doctorsForward.indexOf(chooseList.get(position).getId());
                    doctorsForward.remove(index);
                    doctorsForwardType.remove(index);
                    percentRelativeLayout.setBackgroundResource(0);
                    //chooseList.get(position).setChosen(false);
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }
    private void setAdapter(List<ChatModel> DoctorList) {
        try {
            if (DoctorList != null) {
                ArrayList<UserInfo> userInfoArrayList = new ArrayList<>();
                userInfoArrayList.addAll(DoctorList);
                mAdapter = new ForwardAdapter(userInfoArrayList, ForwardActivity.this);
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
            //doctorsForward.clear();
            //doctorsForward.add(Integer.parseInt(PrefHelper.get(ForwardActivity.this, PrefHelper.KEY_USER_ID, "")));
            //sendForward();
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

                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                    final String key = result.getContents();
                    //sendRequest(key);

                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    /*public void sendRequest(String key) {
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
    }*/

    private String ListtoString(List<Integer> list) {
        String result = "";
        for (Integer item : list) {
            result += item + ",";
        }
        return result.substring(0, (result.length() - 1));
    }

}