package com.germanitlab.kanonhealth.inquiry;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.httpchat.HttpChatActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mo on 3/6/17.
 */

public class InquiryMainFragment extends Fragment {

    ProgressDialog progressDialog;
    OnChoiceSelectedListener mCallback;
    String jsonString;

    public InquiryMainFragment() {

    }

    @SuppressLint("ValidFragment")
    public InquiryMainFragment(String jsonString) {
        this.jsonString = jsonString;
    }

    private String mFirstLevelName;

    @BindView(R.id.button_medizinische)
    Button buttonMedizinische;
    @BindView(R.id.button_Ich)
    Button buttonIch;
    @BindView(R.id.button_beratung)
    Button buttonBeratung;
    @BindView(R.id.button_untersuchung)
    Button buttonUntersuchung;
    @BindView(R.id.button_submit)
    FloatingActionButton floatingActionButton;
    UserInfo doctor;
    Util util;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.questions, container, false);
        ButterKnife.bind(this, root);
        util = Util.getInstance(getActivity());

        //hide the keyboard
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        return root;
    }

    private void disableFinishedButtons(ArrayList<String> fragmentsNames) {


        for (String name : fragmentsNames) {
            if (name.equals(getResourceString(R.string.question_2))) {

                buttonMedizinische.setEnabled(false);
            } else if (name.equals(getResourceString(R.string.question_1))) {
                buttonUntersuchung.setEnabled(false);
            } else if (name.equals(getResourceString(R.string.question_3))) {
                buttonBeratung.setEnabled(false);
            } else if (name.equals(getResourceString(R.string.question_4))) {
                buttonIch.setEnabled(false);
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //Make sure the container activity implemented the callback interface.
        try {
            mCallback = (OnChoiceSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnChoiceSelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();        //hide the keyboard
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//
//            mFirstLevelName = bundle.getString(getResourceString(R.string.first_level));
//
//            if (!mFirstLevelName.equals("firstTime")) {
        disableFinishedButtons(InquiryActivity.finishedFirstLevelOptions);
        // }
        //  }
    }

    @OnClick({R.id.button_medizinische, R.id.button_Ich, R.id.button_beratung, R.id.button_untersuchung, R.id.button_submit})
    public void onClick(View view) {

        final Fragment secondLevelFragment;

        /**
         init the fragment
         Adding the firstLevel key and value and saving it to the local hashMap
         then Calling the callBack of the InquiryFragment And passing to it
         the next fragment to display
         **/
        switch (view.getId()) {
            case R.id.button_medizinische:
                secondLevelFragment = new MedizinischeFragment();
                secondLevelFragment.setArguments(addFirstLevelBundle(getResourceString(R.string.question_2)));
                mCallback.OnChoiceSelected(getResourceString(R.string.question_1), secondLevelFragment);
                break;
            case R.id.button_untersuchung:
                secondLevelFragment = new UntersuchungFragment();
                secondLevelFragment.setArguments(addFirstLevelBundle(getResourceString(R.string.question_1)));
                mCallback.OnChoiceSelected(getResourceString(R.string.question_2), secondLevelFragment);
                break;
            case R.id.button_beratung:
                secondLevelFragment = new OneQuestionFragment();
                secondLevelFragment.setArguments(addFirstLevelBundle(getResourceString(R.string.question_3)));
                mCallback.OnChoiceSelected(getResourceString(R.string.question_3), secondLevelFragment);
                break;
            case R.id.button_Ich:
                secondLevelFragment = new OneQuestionFragment();
                secondLevelFragment.setArguments(addFirstLevelBundle(getResourceString(R.string.question_4)));
                mCallback.OnChoiceSelected(getResourceString(R.string.question_4), secondLevelFragment);
                break;
            case R.id.button_submit:
                if (InquiryActivity.inquiryResult.size() > 0) {
                    final Gson gson = new Gson();
                    doctor = gson.fromJson(jsonString, UserInfo.class);
                    ((ParentActivity) getActivity()).showProgressBar();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final int requestId = ApiHelper.openSession(getContext(), String.valueOf(PrefHelper.get(getContext(), PrefHelper.KEY_USER_ID, 0)), String.valueOf(doctor.getId()), InquiryActivity.inquiryResult,UserInfo.CLINIC);
                            InquiryMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((ParentActivity) getActivity()).hideProgressBar();
                                    if (requestId != -1) {
                                        doctor.setIsSessionOpen(1);
                                        doctor.setRequestID(requestId);
                                        Intent intent = new Intent(getActivity(), HttpChatActivity.class);
                                        intent.putExtra("doctorID", doctor.getUserID());
                                        intent.putExtra("userInfo", doctor);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getActivity(), R.string.cant_start_session, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(getActivity(), R.string.please_choose_an_option_cancel_via_back, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public Bundle addFirstLevelBundle(String levelName) {

        Bundle bundle = new Bundle();
        bundle.putString(getResourceString(R.string.first_level), levelName);
        return bundle;
    }

    public String getResourceString(int resource) {
        return getResources().getString(resource);
    }


}
