package com.germanitlab.kanonhealth.doctors;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.AddPractics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.adapters.ClinicListAdapter;
import com.germanitlab.kanonhealth.adapters.DoctorListAdapter;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.germanitlab.kanonhealth.api.models.Clinic;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.FilterCallBackClickListener;
import com.germanitlab.kanonhealth.intro.StartQrScan;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
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

public class DoctorListFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    //    private ImageButton imgScan;

    /*
    private TextView filter_to_list;
*/
    ArrayList<UserInfo> doctorList;
    ArrayList<Clinic> clinics;


    ArrayList<ChatModel> chatModel;
    private Button btnLeftList, btnRightList;
    int speciality_id;
    String jsonString;
    int type;
    PrefManager prefManager;
    private PrefManager mPrefManager;
    UserInfo user;
    Gson gson;
    private UserRepository mDoctorRepository;
    private EditText edtDoctorListFilter;
    private FilterCallBackClickListener filterCallBackClickListener;
    static DoctorListFragment doctorListFragment;
    private Util util;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    public Boolean is_doctor_data = false, is_clinic_data = false, is_chat_data_left = false, is_chat_data_right = false;
    LinearLayoutManager llm;


    private  static boolean leftTabVisible=true;

    public DoctorListFragment() {
    }

    public static DoctorListFragment newInstance() {
        Bundle bundle = new Bundle();
//        bundle.putInt("speciality_id", mspeciality_id);
//        bundle.putInt("type", mtype);
        if (doctorListFragment == null) {
            doctorListFragment = new DoctorListFragment();
            doctorListFragment.setArguments(bundle);
        } else {
            doctorListFragment.getArguments().putAll(bundle);
        }
        return doctorListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            view = inflater.inflate(R.layout.fragment_doctor_list, container, false);
            setHasOptionsMenu(true);
            mPrefManager = new PrefManager(getActivity());
            prefManager = new PrefManager(getActivity());
            try {
                user = new Gson().fromJson(mPrefManager.getData(PrefManager.USER_KEY), UserInfo.class);
            } catch (Exception e) {
            }
            util = Util.getInstance(getActivity());
            initView();
           // is_doc = prefManager.get(PrefManager.IS_DOCTOR);
            gson = new Gson();
            btnLeftList.setText(R.string.doctors);
            btnRightList.setText(R.string.practices);
          //  type = User.DOCTOR_TYPE;
            doctorList = new ArrayList<>();
            clinics= new ArrayList<>();
            chatModel= new ArrayList<>();

            mDoctorRepository = new UserRepository(getContext());


        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void loadFirstTime() {
        util.showProgressDialog();
                setDoctorList();
                setClinicList();
                getChatData();

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

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionForCamera();
                } else {
                    startActivity(new Intent(getActivity(), StartQrScan.class));
                }
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(leftTabVisible) {
            btnLeftList.setBackgroundResource(R.color.blue);
            btnLeftList.setTextColor(getResources().getColor(R.color.white));
            btnRightList.setBackgroundResource(R.color.gray);
            btnRightList.setTextColor(getResources().getColor(R.color.black));
        }else
        {
            btnRightList.setBackgroundResource(R.color.blue);
            btnRightList.setTextColor(getResources().getColor(R.color.white));
            btnLeftList.setBackgroundResource(R.color.gray);
            btnLeftList.setTextColor(getResources().getColor(R.color.black));
        }
        if (!prefManager.get(PrefManager.IS_OLD)) {
            loadFirstTime();
        }

        else{
            loadData();
        }
    }

    public void requestPermissionForCamera() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && permissions.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startActivity(new Intent(getActivity(), StartQrScan.class));
        }
    }

    public void getChatData() {
        if(user.getUserType()==user.PATIENT) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    chatModel = ApiHelper.getChatDoctor(getContext(), Integer.valueOf(prefManager.getData(PrefManager.USER_ID)));
                    is_chat_data_left = true;
                    isAllDataLoaded();

                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    chatModel = ApiHelper.getChatClinic(getContext(), Integer.valueOf(prefManager.getData(PrefManager.USER_ID)));
                    is_chat_data_right = true;
                    isAllDataLoaded();

                }
            }).start();
        }else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    chatModel = ApiHelper.getChatAnother(getContext(), Integer.valueOf(prefManager.getData(PrefManager.USER_ID)));
                    is_chat_data_left = true;
                    isAllDataLoaded();

                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    chatModel = ApiHelper.getChatClinic(getContext(), Integer.valueOf(prefManager.getData(PrefManager.USER_ID)));
                    is_chat_data_right = true;
                    isAllDataLoaded();

                }
            }).start();

        }




//
//        new HttpCall(getActivity(), new ApiResponse() {
//            @Override
//            public void onSuccess(Object response) {
//                doctorList = (List<User>) response;
//                updateDataBase(doctorList);
//                is_chat_data_left = true;
//                if (is_chat_data_left && is_chat_data_right && is_clinic_data && is_doctor_data) {
//                    util.dismissProgressDialog();
//                    prefManager.put(PrefManager.IS_OLD, true);
//                }
//            }
//
//            @Override
//            public void onFailed(String error) {
//                is_chat_data_left = true;
//                if (is_chat_data_left && is_chat_data_right && is_clinic_data && is_doctor_data) {
//                    util.dismissProgressDialog();
//                    prefManager.put(PrefManager.IS_OLD, true);
//                }
////                            tvLoadingError.setVisibility(View.VISIBLE);
////                            if (error != null && error.length() > 0)
////                                tvLoadingError.setText(error);
////                            else tvLoadingError.setText("Some thing went wrong");
//            }
//        }).getChatDoctors(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), getIam());
//
//        new HttpCall(getActivity(), new ApiResponse() {
//            @Override
//            public void onSuccess(Object response) {
//                doctorList = (List<User>) response;
//                updateDataBase(doctorList);
//                is_chat_data_right = true;
//                if (is_chat_data_left && is_chat_data_right && is_clinic_data && is_doctor_data) {
//                    util.dismissProgressDialog();
//                    prefManager.put(PrefManager.IS_OLD, true);
//                }
//            }
//
//            @Override
//            public void onFailed(String error) {
//                is_chat_data_right = true;
//                if (is_chat_data_left && is_chat_data_right && is_clinic_data && is_doctor_data) {
//                    util.dismissProgressDialog();
//                    prefManager.put(PrefManager.IS_OLD, true);
//                }
////                            tvLoadingError.setVisibility(View.VISIBLE);
////                            if (error != null && error.length() > 0)
////                                tvLoadingError.setText(error);
////                            else tvLoadingError.setText("Some thing went wrong");
////                            chat_layout.setVisibility(View.GONE);
//            }
//        }).getChatClinics(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), getIam());
    }

    private void updateDataBase(List<User> doctorList) {
        for (User user : doctorList) {
            user.setIs_chat(1);
            mDoctorRepository.create(user);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanResult != null) {

            showDialog(scanResult.getContents());
        } else {

            Toast.makeText(getActivity(), R.string.invalid_qr_please_try_again, Toast.LENGTH_LONG).show();

        }
    }

    public void showDialog(final String qr) {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.custom_dialog);

        Button btnLogin = (Button) dialog.findViewById(R.id.btn_login);

        Button btnShowProfile = (Button) dialog.findViewById(R.id.btn_show_profile);

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new HttpCall(getActivity(), new ApiResponse() {
//                    @Override
//                    public void onSuccess(Object response) {
//
//                        Log.d("Response", response.toString());
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onFailed(String error) {
//                        Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
//                        Log.d("Response", error);
//                        dialog.dismiss();
//                    }
//                }).login(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), qr);
//            }
//        });


        btnShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }




    private void setDoctorList ()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
             doctorList= ApiHelper.postGetDoctorList(getContext());

                if (!prefManager.get(PrefManager.IS_OLD)) {
                    is_doctor_data = true;
                    if(leftTabVisible) {
                        setDoctorAdapter(doctorList);
                    }
                       isAllDataLoaded();
                } else {
                    if(leftTabVisible) {
                        setDoctorAdapter(doctorList);
                    }
                  //  CheckTabToScrollTo();
                }

            }
        }).start();
    }

    private void setClinicList()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                clinics= ApiHelper.postGetClinicList(getContext());
                if (!prefManager.get(PrefManager.IS_OLD)) {
                    is_clinic_data = true;
                    if(!leftTabVisible) {
                        setClinicsAdapter(clinics);
                    }
                    isAllDataLoaded();
                } else {
                    if(!leftTabVisible) {
                        setClinicsAdapter(clinics);
                    }
                   // CheckTabToScrollTo();
                }

            }
        }).start();


    }

//    private void CheckTabToScrollTo() {
//        if (type == User.DOCTOR_TYPE) {
//            scrollToPosition(firstVisibleItemPositionForLeftTab);
//        } else {
//            scrollToPosition(firstVisibleItemPositionForRightTab);
//        }
//    }

//    private void scrollToPosition(int mScrollPosition) {
//        try {
//            recyclerView.scrollToPosition(mScrollPosition);
//        } catch (Exception e) {
//        }
//
//    }

//    private void updateDatabase(List<User> doctorList) {
//        for (User user : doctorList) {
//            User temp = mDoctorRepository.getDoctor(user);
//            if (temp != null) {
//                user.setIs_chat(temp.getIs_chat());
//            }
//            mDoctorRepository.create(user);
//        }
//    }


    private void setDoctorAdapter(List<UserInfo> doctorList) {
        if (doctorList != null) {
            final DoctorListAdapter doctorListAdapter = new DoctorListAdapter(doctorList, getActivity());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(doctorListAdapter);

                }
            });
        }
    }

    private void setClinicsAdapter(List<Clinic> clinicList) {
        if (clinicList != null) {
            final ClinicListAdapter clinicListAdapter = new ClinicListAdapter(clinicList, getActivity());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(clinicListAdapter);

                }
            });
        }
    }


//    @Override
//    public void onPause() {
//        if (type == User.DOCTOR_TYPE) {
//            firstVisibleItemPositionForLeftTab = getScrolled();
//        } else {
//            firstVisibleItemPositionForRightTab = getScrolled();
//        }
//        super.onPause();
//    }

    private void loadData() {
//        doctorList = mDoctorRepository.getAll(type);
//        setAdapter(doctorList);
  //      CheckTabToScrollTo();
        if (Helper.isNetworkAvailable(getContext())) {
         if(leftTabVisible)
         {
          setDoctorList();
         }else
         {
             setClinicList();
         }
        }
    }

    private void initView() {
        edtDoctorListFilter = (EditText) view.findViewById(R.id.edt_doctor_list_filter);
        btnLeftList = (Button) view.findViewById(R.id.doctor_list);
        btnRightList = (Button) view.findViewById(R.id.praxis_list);

        if (type == User.DOCTOR_TYPE) {
            btnLeftList.setBackgroundResource(R.color.blue);
            btnLeftList.setTextColor(getResources().getColor(R.color.white));
            btnRightList.setBackgroundResource(R.color.gray);
            btnRightList.setTextColor(getResources().getColor(R.color.black));
        }
        if (type == User.CLINICS_TYPE) {
            btnLeftList.setBackgroundResource(R.color.gray);
            btnLeftList.setTextColor(getResources().getColor(R.color.black));
            btnRightList.setBackgroundResource(R.color.blue);
            btnRightList.setTextColor(getResources().getColor(R.color.white));
        }

        edtDoctorListFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(leftTabVisible) {
                    if (doctorList != null) {

                        if (charSequence.toString().trim().length() > 0) {
                            List<UserInfo> fileDoctorResponses = new ArrayList<>();
                            for (int j = 0; j < doctorList.size(); j++) {

                                String name = doctorList.get(j).getFullName();

                                if (name != null && name.toLowerCase().contains(charSequence.toString().toLowerCase())) {

                                    fileDoctorResponses.add(doctorList.get(j));
                                }
                            }
                            setDoctorAdapter(fileDoctorResponses);
                        } else setDoctorAdapter(doctorList);
                    }
                }else
                {
                    if (charSequence.toString().trim().length() > 0) {
                        List<Clinic> fileClinicResponses = new ArrayList<>();
                        for (int j = 0; j < clinics.size(); j++) {

                            String name = clinics.get(j).getName();

                            if (name != null && name.toLowerCase().contains(charSequence.toString().toLowerCase())) {

                                fileClinicResponses.add(clinics.get(j));
                            }
                        }
                        setClinicsAdapter(fileClinicResponses);
                    } else  setClinicsAdapter(clinics);
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
        btnLeftList = (Button) view.findViewById(R.id.doctor_list);
        btnRightList = (Button) view.findViewById(R.id.praxis_list);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_doctor_chat);
        recyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
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

        btnRightList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftTabVisible=false;
                btnRightList.setBackgroundResource(R.color.blue);
                btnRightList.setTextColor(getResources().getColor(R.color.white));
                btnLeftList.setBackgroundResource(R.color.gray);
                btnLeftList.setTextColor(getResources().getColor(R.color.black));
                    loadData();


            }
        });
        btnLeftList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftTabVisible=true;
                btnRightList.setBackgroundResource(R.color.gray);
                btnRightList.setTextColor(getResources().getColor(R.color.black));
                btnLeftList.setBackgroundResource(R.color.blue);
                btnLeftList.setTextColor(getResources().getColor(R.color.white));

                    loadData();
            }
        });
    }



    private void isAllDataLoaded(){
        if (is_chat_data_left && is_chat_data_right && is_clinic_data && is_doctor_data) {
            util.dismissProgressDialog();
            prefManager.put(PrefManager.IS_OLD, true);
        }

    }

}

