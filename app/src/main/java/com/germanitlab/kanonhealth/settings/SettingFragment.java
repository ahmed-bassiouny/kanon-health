package com.germanitlab.kanonhealth.settings;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.germanitlab.kanonhealth.AddPractics;
import com.germanitlab.kanonhealth.DoctorProfileActivity;
import com.germanitlab.kanonhealth.PasscodeActivty;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.TimeTable;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.httpchat.HttpChatActivity;
import com.germanitlab.kanonhealth.intro.StartQrScan;
import com.germanitlab.kanonhealth.models.SettingResponse;
import com.germanitlab.kanonhealth.models.StatusResponse;
import com.germanitlab.kanonhealth.profile.ProfileActivity;
import com.germanitlab.kanonhealth.settingsClinics.PrcticiesSAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    private View view ,vLineUnderDrStatus;
    private Toolbar toolbar;
    private TextView tvBack, tvSetting, trVersion;
    private ImageView imgQr;
    private ImageButton imgScan;
    private VideoView videoView;
    private TableRow profile, trChangePassCode, tvChangeMobileNumber, trSound, trTerms, trFaq, trSupport, trRecommend, trHelp, trDrStatus, trWeblogin;
    private SettingResponse settingResponse;
    private UserInfo user;
    private RecyclerView rvPracticies;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
     View vLineUnderClinics ;

    //status doctor
    private TextView txt_status, tvAddPractice;
    private Button btn_change_status;
    View line;

    static private SettingFragment settingFragment;
    private StatusResponse statusResponse;
    private int UserStatus;
    private PrcticiesSAdapter mAdapter;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        if (settingFragment == null)
            settingFragment = new SettingFragment();
        return settingFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        try {
            user = new Gson().fromJson(PrefHelper.get(getActivity(),PrefHelper.KEY_USER_KEY,""), UserInfo.class);
        } catch (Exception e) {
        }
        initView();
        loadData();
        return view;
    }

    private void setAdapter() {


//            UserInfo userInfo= new Gson().fromJson(mPrefManager.getData(PrefManager.USER_KEY), UserInfo.class);
//            List<ChooseModel> clinicsList = userInfo.get

        ArrayList<UserInfo> allClinics= new ArrayList<>();
       // allClinics.addAll(user.getClinics());
        allClinics.addAll(user.getMyClinics());
        if (allClinics.size()> 0) {
                vLineUnderClinics.setVisibility(View.VISIBLE);
            }
            else {
                vLineUnderClinics.setVisibility(View.GONE);
            }
            mAdapter = new PrcticiesSAdapter(getContext(), allClinics);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rvPracticies.setLayoutManager(mLayoutManager);
            rvPracticies.setAdapter(mAdapter);
            rvPracticies.setNestedScrollingEnabled(false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_qr_code:
                new Helper(getActivity()).ImportQr();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null && isVisibleToUser) {
            loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (!Helper.isNetworkAvailable(getActivity()))
            return;

        ((ParentActivity) getActivity()).showProgressBar();
        new Thread( new Runnable() {
            @Override
            public void run() {
                UserInfo temp = user;
                user=ApiHelper.getUserInfo(getContext(),String.valueOf(user.getUserID()));
                if(user!=null)
                {
                    Gson gson = new Gson();
                    PrefHelper.put(getActivity(),PrefHelper.KEY_USER_KEY,gson.toJson(user));

                }else
                {
                   user=temp;
                }
                SettingFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ParentActivity) getActivity()).hideProgressBar();
                        initView();
                        handelEvent();
                        setHasOptionsMenu(true);
                        setAdapter();
                    }
                });
            }
        }).start();


    }

    private void initView() {
        try {
            tvAddPractice = (TextView) view.findViewById(R.id.tv_add_practice);
            rvPracticies = (RecyclerView) view.findViewById(R.id.recycler_view);
            trWeblogin = (TableRow) view.findViewById(R.id.setting_weblogin);
            trSupport = (TableRow) view.findViewById(R.id.tr_support);
            profile = (TableRow) view.findViewById(R.id.my_profile);
            trDrStatus = (TableRow) view.findViewById(R.id.dr_status);
            vLineUnderDrStatus =  view.findViewById(R.id.v_under_dr_status);
            if(user.getUserType() == UserInfo.DOCTOR) {
                trDrStatus.setVisibility(View.GONE);
                vLineUnderDrStatus.setVisibility(View.GONE);
            }
            vLineUnderClinics = view.findViewById(R.id.v_line_under_clinics);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);


            tvBack = (TextView) view.findViewById(R.id.tv_back);

            videoView = (VideoView) view.findViewById(R.id.video_view);
            tvSetting = (TextView) view.findViewById(R.id.tv_setting);

            trChangePassCode = (TableRow) view.findViewById(R.id.tr_change_pass);
            tvChangeMobileNumber = (TableRow) view.findViewById(R.id.tr_mobile_number);
            trSound = (TableRow) view.findViewById(R.id.tr_sound);
            trFaq = (TableRow) view.findViewById(R.id.tr_faq);
            trTerms = (TableRow) view.findViewById(R.id.tr_terms);
            trHelp = (TableRow) view.findViewById(R.id.tr_help);
            trVersion = (TextView) view.findViewById(R.id.tv_version);
            line = (View) view.findViewById(R.id.line);
            //status doctor
            txt_status = (TextView) view.findViewById(R.id.txt_status);
            btn_change_status = (Button) view.findViewById(R.id.btn_change_status);

            PackageInfo pInfo = null;

            try {
                pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
                String version = pInfo.versionName;
                trVersion.setText("Version : " + version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            trChangePassCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PasscodeActivty.class);
                    intent.putExtra("checkPassword", true);
                    intent.putExtra("finish", true);
                    intent.putExtra("has_back", true);
                    startActivityForResult(intent, 13);
                }
            });

            trSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), TimeTable.class));
                }
            });

            trWeblogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA )!=PackageManager.PERMISSION_GRANTED) {
                        requestPermissions( new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    } else {
                        startActivity(new Intent(getActivity(), StartQrScan.class));
                    }
                }
            });

            trDrStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), CustomerSupportActivity.class));
                }
            });
            tvAddPractice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), AddPractics.class);
                    intent.putExtra("CLINIC", "CLINIC");
                    startActivity(intent);
                }
            });


            trFaq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), DoctorProfileActivity.class));
                }
            });
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user.getUserType()==UserInfo.DOCTOR ){
                        Intent intent = new Intent(getActivity(), DoctorProfileActivity.class);
                        intent.putExtra("doctor_data", user);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        intent.putExtra("from", true);
                        startActivity(intent);
                    }
                }
            });
            trSupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), CustomerSupportActivity.class));
                }
            });
            trHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            UserStatus = user.getAvailable();

                checkStatus(UserStatus);


            btn_change_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Helper.isNetworkAvailable(getContext())) {
                        UserStatus = user.getAvailable();
                        if (UserStatus==1) {
                            changStatusService(0);

                        } else {

                            changStatusService(1);
                        }
                    } else
                        Toast.makeText(getContext(), getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();

                }
            });
            if (user.getUserType() == UserInfo.DOCTOR) {
                tvAddPractice.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                txt_status.setVisibility(View.VISIBLE);
                btn_change_status.setVisibility(View.VISIBLE);
            }
            if (user.getUserType() == UserInfo.DOCTOR) {
                rvPracticies.setVisibility(View.VISIBLE);
            } else {
                rvPracticies.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.error_while_loading_data, Toast.LENGTH_SHORT).show();
        }

    }

    private void changStatusService(final int isAvailable) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                int result= ApiHelper.postChangeStatus(Integer.valueOf(PrefHelper.get(getActivity(),PrefHelper.KEY_USER_ID,-1)),isAvailable,getContext());
                if(result!=-1)
                {
                    if (isAvailable==1) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt_status.setText(R.string.you_are_now_online);
                                btn_change_status.setText(R.string.go_offline);
                                user.setAvailable(1);

                            }
                        });



                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt_status.setText(R.string.you_are_now_offline);
                                btn_change_status.setText(R.string.go_online);
                                user.setAvailable(0);

                            }
                        });


                    }
                    Gson gson=new Gson();
                    PrefHelper.put(getActivity(),PrefHelper.KEY_USER_KEY,gson.toJson(user));
                }
            }
        }).start();

    }

    private void handelEvent() {
        try {
            trTerms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (settingResponse != null) {
                        Intent intent = new Intent(getActivity(), TermsConditonActivity.class);
                        intent.putExtra(Constants.TERMS, settingResponse.getTerms());
                        getActivity().startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
        }
    }


    private void checkStatus(Integer userStatus) {
        // call rest to get data

        if (userStatus==1) {
            txt_status.setText(R.string.you_are_now_online);
            btn_change_status.setText(R.string.go_offline);

        } else {
            txt_status.setText(R.string.you_are_now_offline);
            btn_change_status.setText(R.string.go_online);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 13 && resultCode == getActivity().RESULT_OK) {
            Intent intent = new Intent(getActivity(), PasscodeActivty.class);
            intent.putExtra("checkPassword", false);
            intent.putExtra("finish", true);
            intent.putExtra("has_back", true);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CAMERA_PERMISSION_REQUEST_CODE && permissions.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                startActivity(new Intent(getActivity(), StartQrScan.class));
        }
    }
}
