package com.germanitlab.kanonhealth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.adapters.FilterAdapter;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.doctors.DoctorListFragment;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.Speciality;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpecialitiesFragment extends Fragment {
    private List<Speciality> specialityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FilterAdapter mAdapter;
    TypeToken<List<Speciality>> token;
    private LinearLayout toolbar;
    private Boolean from;
    private int type ;


    public SpecialitiesFragment() {
        // Required empty public constructor
    }

/*
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = get.getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            setStyle(STYLE_NO_FRAME, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.MATCH_PARENT  );

        }
    }
*/

    @Override
    public void onResume() {
        super.onResume();
        try {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK){

                        new Helper(getActivity()).replaceFragments(new DoctorListFragment(),
                                R.id.doctor_list_continer, "doctorList");
                        return true;

                    }

                    return false;
                }
            });
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_specialities, container, false);
        token = new TypeToken<List<Speciality>>() {
        };
        toolbar = (LinearLayout) view.findViewById(R.id.toolbar);
        Bundle bundle = getArguments();
/*            String dd = savedInstanceState.getString("data");
            Log.d("my string ", dd.toString());*/
        from = bundle.getBoolean("from");
        type = bundle.getInt("type");
        try {
            boolean hide = bundle.getBoolean("hide");
            if (hide)
                toolbar.setVisibility(View.GONE);
        } catch (Exception e) {

        }
        new HttpCall(getActivity(), new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                try {
                    Gson gson = new Gson();
                    String json = gson.toJson(response);
                    Log.e("returned msg ", json);
                    specialityList = (List<Speciality>) response;
                    createAdapter(specialityList, view, from);
                }catch (Exception e)
                {
                    Crashlytics.logException(e);
                    Toast.makeText(getContext(), getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailed(String error) {
                Log.e("error in returned json", error);
                Toast.makeText(getContext(), getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        }).getSpecialities((String.valueOf(AppController.getInstance().getClientInfo().getUser_id()))
                , AppController.getInstance().getClientInfo().getPassword());
        return view;
    }

    public void createAdapter(List<Speciality> specialityList, View view, Boolean from) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new FilterAdapter(specialityList, getActivity(), from , type);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }


}
