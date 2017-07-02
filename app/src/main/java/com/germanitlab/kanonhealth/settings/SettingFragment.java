package com.germanitlab.kanonhealth.settings;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.AddPractics;
import com.germanitlab.kanonhealth.DoctorProfileActivity;
import com.germanitlab.kanonhealth.PasscodeActivty;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.TimeTable;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.chat.ChatActivity;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.SettingResponse;
import com.germanitlab.kanonhealth.models.StatusResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.models.user.UserRegisterResponse;
import com.germanitlab.kanonhealth.profile.ProfileActivity;
import com.germanitlab.kanonhealth.settingsClinics.PrcticiesSAdapter;
import com.google.gson.Gson;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    private View view;
    private Toolbar toolbar;
    private TextView tvBack, tvSetting, trVersion;
    private ImageView imgQr;
    private ImageButton imgScan;
    private VideoView videoView;
    private TableRow profile, trChangePassCode, tvChangeMobileNumber, trSound, trTerms, trFaq, trSupport, trRecommend, trHelp, trDrStatus;
    private SettingResponse settingResponse;
    private PrefManager mPrefManager;
    private User user;
    private RecyclerView rvPracticies;

    //status doctor
    private TextView txt_status, tvAddPractice;
    private Button btn_change_status;
    View line;

    static private SettingFragment settingFragment;
    private StatusResponse statusResponse;
    private String UserStatus;
    private PrcticiesSAdapter mAdapter;
    PrefManager prefManager;

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
        mPrefManager = new PrefManager(getActivity());
        prefManager = new PrefManager(getContext());
        try {
            user = new Gson().fromJson(mPrefManager.getData(PrefManager.USER_KEY), UserInfoResponse.class).getUser();
        } catch (Exception e) {
        }
        initView();
        loadData();
        return view;
    }

    private void setAdapter() {
        try {

            UserInfoResponse userInfoResponse = new Gson().fromJson(mPrefManager.getData(PrefManager.USER_KEY), UserInfoResponse.class);

            List<ChooseModel> clinicsList = userInfoResponse.getUser().getMembers_at();


            mAdapter = new PrcticiesSAdapter(getContext(), clinicsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rvPracticies.setLayoutManager(mLayoutManager);
            rvPracticies.setAdapter(mAdapter);
            rvPracticies.setNestedScrollingEnabled(false);
        } catch (Exception e) {
        }
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
                Helper.ImportQr(mPrefManager, getActivity());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null && isVisibleToUser) {
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        loadData();
        getSetting();
    }

    private void loadData() {
        if(!Helper.isNetworkAvailable(getActivity()))
            return;
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(R.string.waiting_text);
            progressDialog.setCancelable(false);
            UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
            userRegisterResponse.setUser_id(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));
            userRegisterResponse.setPassword(prefManager.getData(PrefManager.USER_PASSWORD));
            new HttpCall(getActivity(), new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    if (response != null) {
                        Gson gson = new Gson();
                        mPrefManager.put(PrefManager.USER_KEY, gson.toJson(response));
                        user = new Gson().fromJson(mPrefManager.getData(PrefManager.USER_KEY), UserInfoResponse.class).getUser();
                        progressDialog.dismiss();

                    } else {
                        user = new Gson().fromJson(mPrefManager.getData(PrefManager.USER_KEY), UserInfoResponse.class).getUser();
                        Toast.makeText(getActivity(), R.string.error_message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    initView();
                    handelEvent();
                    setHasOptionsMenu(true);
                    setAdapter();
                }

                @Override
                public void onFailed(String error) {
                    user = new Gson().fromJson(mPrefManager.getData(PrefManager.USER_KEY), UserInfoResponse.class).getUser();
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    initView();
                    handelEvent();
                    setHasOptionsMenu(true);
                    setAdapter();
                }
            }).getProfile(userRegisterResponse);


        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        try {
            tvAddPractice = (TextView) view.findViewById(R.id.tv_add_practice);
            rvPracticies = (RecyclerView) view.findViewById(R.id.recycler_view);
            trSupport = (TableRow) view.findViewById(R.id.tr_support);
            profile = (TableRow) view.findViewById(R.id.my_profile);
            trDrStatus = (TableRow) view.findViewById(R.id.dr_status);

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
                    startActivityForResult(intent, 13);
                }
            });
            trSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), TimeTable.class));
                }
            });

            trDrStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("from_notification", 1);
                    intent.putExtra("from_id", 1);
                    startActivity(intent);

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
                    if (user.getIsDoc() == null || user.getIsClinic() == null) {
                        Toast.makeText(getActivity(), R.string.contact_support, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (user.getIsDoc() == 1 || user.getIsClinic() == 1) {
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

            UserStatus = new PrefManager(getActivity()).getData(PrefManager.USER_STATUS);

            if (UserStatus != null) {
                checkStatus(UserStatus);
            }

            btn_change_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserStatus = new PrefManager(getActivity()).getData(PrefManager.USER_STATUS);
                    if (UserStatus.equals("1")) {
                        changStatusService("0");

                    } else {

                        changStatusService("1");
                    }

                }
            });
            if (user.getIsDoc() == 1) {
                tvAddPractice.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                txt_status.setVisibility(View.VISIBLE);
                btn_change_status.setVisibility(View.VISIBLE);
            }
            if (user.getIsDoc() == 1) {
                rvPracticies.setVisibility(View.VISIBLE);
            } else {
                rvPracticies.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error while loading data", Toast.LENGTH_SHORT).show();
        }

    }

    private void changStatusService(String isAvailable) {

        new HttpCall(getActivity(), new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                statusResponse = (StatusResponse) response;
                new PrefManager(getActivity()).put(PrefManager.USER_STATUS, statusResponse.getIs_available());

                if (statusResponse.getIs_available().equals("1")) {
                    txt_status.setText(R.string.youareonline);
                    btn_change_status.setText(R.string.go_offline);

                } else {
                    txt_status.setText(R.string.youareoffline);
                    btn_change_status.setText(R.string.go_online);
                }
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getContext(), getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();
                Log.e("Error", error + "++");

            }
        }).goOnline(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), isAvailable);

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

    private void getSetting() {

        new HttpCall(getActivity(), new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                settingResponse = (SettingResponse) response;
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getContext(), getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();

                Log.e("Error", error + "++");

            }
        }).getSetting();

    }

    private void checkStatus(String userStatus) {
        // call rest to get data

        if (userStatus.equals("1")) {
            txt_status.setText(R.string.youareonline);
            btn_change_status.setText(R.string.go_offline);

        } else {
            txt_status.setText(R.string.youareoffline);
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
            startActivity(intent);
        }
    }
}
