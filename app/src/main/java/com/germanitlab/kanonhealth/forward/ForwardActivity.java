package com.germanitlab.kanonhealth.forward;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.adapters.DoctorListAdapter;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.chat.ChatActivity;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.interfaces.MyClickListener;
import com.germanitlab.kanonhealth.interfaces.RecyclerTouchListener;
import com.germanitlab.kanonhealth.main.MainActivity;
import com.germanitlab.kanonhealth.models.doctors.User;
import com.germanitlab.kanonhealth.models.user.User1;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ForwardActivity extends AppCompatActivity {
    private List<User> ListDoctors;
    private List<User> ChooseList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DoctorListAdapter mAdapter;
    ProgressDialog progressDialog;
    private EditText edtDoctorListFilter;
    ArrayList<Integer> doctorsForward, messagesForward;
    @BindView(R.id.btn_forward_document)
    Button foward_document;
    private User1 user;
    Boolean search;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private int entity_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward);
        doctorsForward = new ArrayList<>();
        messagesForward = new ArrayList<>();
        ButterKnife.bind(this);
        user = new User1();
        search = false;
        user = (User1) getIntent().getSerializableExtra("chat_doctor");
        messagesForward = getIntent().getIntegerArrayListExtra("list");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListDoctors = new ArrayList<>();
        edtDoctorListFilter = (EditText) findViewById(R.id.edt_doctor_list_filter);
        showProgressDialog();
        new HttpCall(new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                dismissProgressDialog();
                ListDoctors = (List<User>) response;
                ChooseList.addAll(ListDoctors);
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                setAdapter(ChooseList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                addListener(recyclerView);
            }

            @Override
            public void onFailed(String error) {
                /*tvLoadingError.setVisibility(View.VISIBLE);
                if (error != null && error.length() > 0)
                    tvLoadingError.setText(error);
                else tvLoadingError.setText("Some thing went wrong");*/
            }
        }).getChatDoctors(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                , AppController.getInstance().getClientInfo().getPassword());

    }

    @OnTextChanged(value = R.id.edt_doctor_list_filter, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (ListDoctors != null) {
            ChooseList.clear();
            if (charSequence.toString().trim().length() > 0) {
                for (int j = 0; j < ListDoctors.size(); j++) {

                    String name = ListDoctors.get(j).getName();

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
    }


    @OnClick(R.id.imgbtn_forward)
    public void forwardTo(View view) {
        if (doctorsForward.size() > 0)
            sendForward();
        else
            Toast.makeText(getApplicationContext(), "please Select", Toast.LENGTH_LONG).show();
    }

    private void sendForward() {
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                Log.e("Update user response :", "no response found");
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("from_notification", 1);
                if (doctorsForward.size() > 1) {
                    intent.putExtra("from_id", user.get_Id());
                } else {
                    intent.putExtra("from_id", doctorsForward.get(0));
                }
                startActivity(intent);


            }

            @Override
            public void onFailed(String error) {
                Log.e("Error", error);
            }
        }).forward(String.valueOf(AppController.getInstance().getClientInfo().getUser_id()), String.valueOf(AppController.getInstance().getClientInfo().getUser_id()), messagesForward, doctorsForward);
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
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.background);
        if (ChooseList.size() == 0) {
            if (!doctorsForward.contains(ListDoctors.get(position).get_Id())) {
                doctorsForward.add(ListDoctors.get(position).get_Id());
                linearLayout.setBackgroundResource(R.color.gray_black);
                ListDoctors.get(position).setChosen(true);
            } else {
                doctorsForward.remove(doctorsForward.indexOf(ListDoctors.get(position).get_Id()));
                linearLayout.setBackgroundResource(0);
                ListDoctors.get(position).setChosen(false);
            }
        } else {
            if (!doctorsForward.contains(ChooseList.get(position).get_Id())) {
                doctorsForward.add(ChooseList.get(position).get_Id());
                linearLayout.setBackgroundResource(R.color.gray_black);
                ChooseList.get(position).setChosen(true);
            } else {
                doctorsForward.remove(doctorsForward.indexOf(ChooseList.get(position).get_Id()));
                linearLayout.setBackgroundResource(0);
                ChooseList.get(position).setChosen(false);
            }
        }
    }

    private void setAdapter(List<User> DoctorList) {
        if (DoctorList != null) {
            mAdapter = new DoctorListAdapter(DoctorList, this);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @OnClick(R.id.btn_forward_document)
    public void toDocument(View view) {
        doctorsForward.clear();
        doctorsForward.add(AppController.getInstance().getClientInfo().getUser_id());
        sendForward();
    }

    @OnClick(R.id.scan)
    public void scanId(View view) {
        askForPermission();
    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //Asking for the camera permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            //Opening the QR Scanner
            new IntentIntegrator(this).initiateScan();
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

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
/*
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
*/
                final String key = result.getContents();
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.activity_qr_activity);
                dialog.setTitle("Custom Alert Dialog");
                dialog.setCanceledOnTouchOutside(false);
                Button btnDoctor = (Button) dialog.findViewById(R.id.doctor);
                Button btnClinic = (Button) dialog.findViewById(R.id.clinic);
                Button btnUser = (Button) dialog.findViewById(R.id.doctor);
                Button btnCancel = (Button) dialog.findViewById(R.id.cancel);
                btnDoctor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendRequest(key, 2);
                    }
                });
                btnClinic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendRequest(key, 3);
                    }
                });
                btnUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendRequest(key, 1);
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });

                dialog.show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void sendRequest(String key, int entity_type) {
        showProgressDialog();
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                User1 user1 = (User1) response;
                doctorsForward.clear();
                doctorsForward.add(user1.get_Id());
                sendForward();


            }

            @Override
            public void onFailed(String error) {
                Log.e("My error ", error.toString());

            }
        }).getDoctor(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                , AppController.getInstance().getClientInfo().getPassword(), key, entity_type);
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.waiting_text), true);
    }
}
