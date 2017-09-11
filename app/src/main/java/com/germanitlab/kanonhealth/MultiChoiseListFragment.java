package com.germanitlab.kanonhealth;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.adapters.SelectLanguageAdapter;
import com.germanitlab.kanonhealth.adapters.SelectMemberDoctorAdapter;
import com.germanitlab.kanonhealth.adapters.SelectSpecialityAdapter;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Language;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.callback.Message;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.interfaces.MyClickListener;
import com.germanitlab.kanonhealth.interfaces.RecyclerTouchListener;

import java.util.ArrayList;

public class MultiChoiseListFragment extends DialogFragment {
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView error_message;
    SelectSpecialityAdapter selectSpecialityAdapter;
    SelectLanguageAdapter selectLanguageAdapter;
    SelectMemberDoctorAdapter selectMemberDoctorAdapter;
    Button save;
    LinearLayout ll_view;
    ArrayList<Speciality> allSpecialList;
    ArrayList<Language> allLanguageList;
    ArrayList<UserInfo> allDoctorList;
    ArrayList<Speciality> mySpecialList;
    ArrayList<Language> myLanguageList;
    ArrayList<UserInfo> myDoctorList;
    int type;

    public MultiChoiseListFragment() {
        super();
        setStyle(STYLE_NO_TITLE, 0);
    }

    Message message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mulitchoise_list_layout, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        save = (Button) view.findViewById(R.id.save);
        error_message = (TextView) view.findViewById(R.id.error_message);
        ll_view = (LinearLayout) view.findViewById(R.id.ll_view);

        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                type = bundle.getInt("Constants");
                if (type == Constants.SPECIALITIES) {
                    mySpecialList = (ArrayList<Speciality>) bundle.getSerializable(Constants.CHOSED_LIST);
                } else if (type == Constants.LANGUAUGE) {
                    myLanguageList = (ArrayList<Language>) bundle.getSerializable(Constants.CHOSED_LIST);
                } else if (type == Constants.DoctorAll) {
                    myDoctorList = (ArrayList<UserInfo>) bundle.getSerializable(Constants.CHOSED_LIST);
                }
            }
            getData();

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type == Constants.SPECIALITIES) {
                        message.returnChoseSpecialityList(selectSpecialityAdapter.mySpecialList);
                    } else if (type == Constants.LANGUAUGE) {
                        message.returnChoseLanguageList(selectLanguageAdapter.myLanguages);
                    } else if (type == Constants.DoctorAll) {
                        message.returnChoseDoctorList(selectMemberDoctorAdapter.myDoctors);
                    }
                    getDialog().dismiss();
                }
            });


        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            Log.i("MultiChoiselist", "Fragment" + e);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        message = (Message) context;
    }

    public void getData() {
        switch (type) {
            case Constants.SPECIALITIES:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        allSpecialList = ApiHelper.getSpecialities(getActivity().getApplicationContext());
                        selectSpecialityAdapter = new SelectSpecialityAdapter(getContext(), allSpecialList, mySpecialList);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initRecyclerView(selectSpecialityAdapter);

                            }
                        });

                    }
                }).start();

                break;
            case Constants.LANGUAUGE:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        allLanguageList = ApiHelper.getLanguage(getActivity().getApplicationContext());
                        selectLanguageAdapter = new SelectLanguageAdapter(getContext(), allLanguageList, myLanguageList);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initRecyclerView(selectLanguageAdapter);
                            }
                        });

                    }
                }).start();
                break;
            case Constants.DoctorAll:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        allDoctorList = ApiHelper.postGetDoctorList(getActivity().getApplicationContext(), String.valueOf(PrefHelper.get(getContext(), PrefHelper.KEY_USER_ID, -1)));
                        selectMemberDoctorAdapter = new SelectMemberDoctorAdapter(getContext(), allDoctorList, myDoctorList);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initRecyclerView(selectMemberDoctorAdapter);
                            }
                        });

                    }
                }).start();
                break;
        }
    }


    private void initRecyclerView(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }
}