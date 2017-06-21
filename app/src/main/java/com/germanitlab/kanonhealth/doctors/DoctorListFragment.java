package com.germanitlab.kanonhealth.doctors;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.DoctorProfileActivity;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.Specilaities;
import com.germanitlab.kanonhealth.adapters.DoctorListAdapter;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.chat.MapsActivity;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.interfaces.FilterCallBackClickListener;
import com.germanitlab.kanonhealth.interfaces.MyClickListener;
import com.germanitlab.kanonhealth.interfaces.RecyclerTouchListener;
import com.germanitlab.kanonhealth.intro.StartQrScan;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.ormLite.UserRepository;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

// Edit By Ahmed 29-5-2017

public class DoctorListFragment extends Fragment implements ApiResponse {

    private View view;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    //    private ImageButton imgScan;
    private ImageView filter, map, ImgBtnSearch;

    /*
    private TextView filter_to_list;
*/
    List<User> doctorList;
    private Button doctor_list, praxis_list;
    int speciality_id;
    String jsonString;
    int type;
    PrefManager prefManager;
    Gson gson;
    private UserRepository mDoctorRepository;
    private EditText edtDoctorListFilter;
    private FilterCallBackClickListener filterCallBackClickListener;
    static DoctorListFragment doctorListFragment;
    private Util util;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;

    public DoctorListFragment() {
    }

    public static DoctorListFragment newInstance(int mspeciality_id, int mtype) {
        Bundle bundle = new Bundle();
        bundle.putInt("speciality_id", mspeciality_id);
        bundle.putInt("type", mtype);
        if (doctorListFragment == null)
            doctorListFragment = new DoctorListFragment();
        doctorListFragment.setArguments(bundle);
        return doctorListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            view = inflater.inflate(R.layout.fragment_doctor_list, container, false);
            setHasOptionsMenu(true);
            prefManager = new PrefManager(getActivity());
            util = Util.getInstance(getActivity());
            initView();
            gson = new Gson();
            util.showProgressDialog();
            if (type == 0)
                type = 2;
            mDoctorRepository = new UserRepository(getContext());
            loadData();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contacts_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_search:
                if (edtDoctorListFilter.getVisibility() == View.GONE)
                    edtDoctorListFilter.setVisibility(View.VISIBLE);
                else if (edtDoctorListFilter.getVisibility() == View.VISIBLE)
                    edtDoctorListFilter.setVisibility(View.GONE);
                break;
            case R.id.mi_scan:

                if (!checkPermissionForCamera()) {
                    requestPermissionForCamera();
                } else {
                    startActivity(new Intent(getActivity(), StartQrScan.class));
                }
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            Toast.makeText(getContext().getApplicationContext(), "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            startActivity(new Intent(getActivity(), StartQrScan.class));
        }
    }


    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        speciality_id = getArguments().getInt("speciality_id");
        type = getArguments().getInt("type");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanResult != null) {

            showDialog(scanResult.getContents());
        } else {

            Toast.makeText(getActivity(), "Invalid Qr please try again", Toast.LENGTH_LONG).show();

        }
    }

    public void showDialog(final String qr) {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.custom_dialog);

        Button btnLogin = (Button) dialog.findViewById(R.id.btn_login);

        Button btnShowProfile = (Button) dialog.findViewById(R.id.btn_show_profile);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new HttpCall(getActivity(), new ApiResponse() {
                    @Override
                    public void onSuccess(Object response) {

                        Log.d("Response", response.toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailed(String error) {
                        Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
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

    private void getBySpeciality() {
        new HttpCall(getActivity(), new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                try {
                    if (type == 2) {
                        doctor_list.setBackgroundResource(R.color.blue);
                        doctor_list.setTextColor(getResources().getColor(R.color.white));
                        praxis_list.setBackgroundResource(R.color.gray);
                        praxis_list.setTextColor(getResources().getColor(R.color.black));
                    }
                    Log.e("Update user response :", "no response found");
                    jsonString = gson.toJson(response);
                    prefManager.put(PrefManager.DOCTOR_LIST, jsonString);
                    doctorList = (List<User>) response;
                    saveInDB(doctorList);
                    setAdapter(doctorList);
                    util.dismissProgressDialog();
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailed(String error) {
                util.dismissProgressDialog();
                Log.e("Error", error);
                Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            }
        }).getlocations(String.valueOf(AppController.getInstance().getClientInfo().getUser_id()), String.valueOf(AppController.getInstance().getClientInfo().getPassword()),
                speciality_id, type);
    }

    private void saveInDB(List<User> doctorList) {
        for (User user : doctorList
                ) {
            if (mDoctorRepository.getDoctor(user) == null) {
                mDoctorRepository.create(user);
            } else {
                if (mDoctorRepository.getDoctor(user).getJsonDocument() == null || mDoctorRepository.getDoctor(user).getJsonDocument() == "")
                    mDoctorRepository.updateColoumn(user);
            }
        }
    }

    private void setAdapter(List<User> doctorList) {
        if (doctorList != null) {
            DoctorListAdapter doctorListAdapter = new DoctorListAdapter(doctorList, getActivity(), View.VISIBLE, 1);
            recyclerView.setAdapter(doctorListAdapter);
        }
    }


    @Override
    public void onSuccess(Object response) {
        try {
            doctorList = (List<User>) response;
            setAdapter(doctorList);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailed(String error) {
        Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        Log.e("error", error);
    }

    private void loadData() {
        if (Helper.isNetworkAvailable(getContext())) {
            getBySpeciality();
        } else {
            //clinic 3 doctor 2
            doctorList = mDoctorRepository.getAll(type);
            setAdapter(doctorList);
            util.dismissProgressDialog();
        }
    }

    private void initView() {
        edtDoctorListFilter = (EditText) view.findViewById(R.id.edt_doctor_list_filter);
        doctor_list = (Button) view.findViewById(R.id.doctor_list);
        praxis_list = (Button) view.findViewById(R.id.praxis_list);
        ImgBtnSearch = (ImageView) view.findViewById(R.id.img_btn_search);

        if (type == 2) {
            doctor_list.setBackgroundResource(R.color.blue);
            doctor_list.setTextColor(getResources().getColor(R.color.white));
            praxis_list.setBackgroundResource(R.color.gray);
            praxis_list.setTextColor(getResources().getColor(R.color.black));
        }
        if (type == 3) {
            doctor_list.setBackgroundResource(R.color.gray);
            doctor_list.setTextColor(getResources().getColor(R.color.black));
            praxis_list.setBackgroundResource(R.color.blue);
            praxis_list.setTextColor(getResources().getColor(R.color.white));
        }

        edtDoctorListFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (doctorList != null) {

                    if (charSequence.toString().trim().length() > 0) {
                        List<User> fileDoctorResponses = new ArrayList<>();
                        for (int j = 0; j < doctorList.size(); j++) {

                            String name = doctorList.get(j).getName();

                            if (name != null && name.toLowerCase().contains(charSequence.toString().toLowerCase())) {

                                fileDoctorResponses.add(doctorList.get(j));
                            }
                        }
                        setAdapter(fileDoctorResponses);
                    } else setAdapter(doctorList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        edtDoctorListFilter.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return true;
                }
                return false;
            }
        });
/*
        filter_to_list = (TextView) view.findViewById(R.id.filter_to_list);
*/
        map = (ImageView) view.findViewById(R.id.map);
        filter = (ImageView) view.findViewById(R.id.img_filter);
//        toolbar = (Toolbar) view.findViewById(R.id.doctor_chat_toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        doctor_list = (Button) view.findViewById(R.id.doctor_list);
        praxis_list = (Button) view.findViewById(R.id.praxis_list);
//        imgScan = (ImageButton) toolbar.findViewById(R.id.scan);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_doctor_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

/*         filter_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("from", false);
                SpecialitiesFragment fragment = new SpecialitiesFragment();
                fragment.setArguments(bundle);
                new Helper(getActivity()).replaceFragments(fragment,
                        R.id.doctor_list_continer, "Specialty");
            }
        });*/

        edtDoctorListFilter.setVisibility(View.GONE);
        ImgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtDoctorListFilter.getVisibility() == View.GONE)
                    edtDoctorListFilter.setVisibility(View.VISIBLE);
                else if (edtDoctorListFilter.getVisibility() == View.VISIBLE)
                    edtDoctorListFilter.setVisibility(View.GONE);

            }
        });


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Specilaities.class);
                intent.putExtra("from", false);
                intent.putExtra("type", type);
                startActivity(intent);

            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*getBySpeciality(speciality_id);*/
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("from", true);
                intent.putExtra("Location", jsonString);
                getActivity().startActivity(intent);
            }
        });
        praxis_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                praxis_list.setBackgroundResource(R.color.blue);
                praxis_list.setTextColor(getResources().getColor(R.color.white));
                doctor_list.setBackgroundResource(R.color.gray);
                doctor_list.setTextColor(getResources().getColor(R.color.black));
                if (type != 3) {
                    type = 3;
                    util.showProgressDialog();
                    loadData();
                }

            }
        });
        doctor_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                praxis_list.setBackgroundResource(R.color.gray);
                praxis_list.setTextColor(getResources().getColor(R.color.black));
                doctor_list.setBackgroundResource(R.color.blue);
                doctor_list.setTextColor(getResources().getColor(R.color.white));
                if (type != 2) {
                    type = 2;
                    util.showProgressDialog();
                    loadData();
                    getBySpeciality();
                }
            }
        });
    }


}
