package com.germanitlab.kanonhealth.inquiry;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.chat.ChatActivity;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.google.gson.Gson;

/**
 * Created by Mo on 3/6/17.
 */

public class InquiryMainFragment extends Fragment {

    ProgressDialog progressDialog;
    OnChoiceSelectedListener mCallback;
    String jsonString ;

    public InquiryMainFragment (){

    }

    @SuppressLint("ValidFragment")
    public InquiryMainFragment (String jsonString){
        this.jsonString = jsonString ;
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
    UserInfoResponse doctor ;

    private PrefManager prefManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.questions, container, false);
        ButterKnife.bind(this, root);
        prefManager = new PrefManager(getActivity());

        //hide the keyboard
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {

            mFirstLevelName = bundle.getString(getResourceString(R.string.first_level));

            if (!mFirstLevelName.equals("firstTime")) {
                disableFinishedButtons(InquiryActivity.finishedFirstLevelOptions);
            }
        }
        return root;
    }

    private void disableFinishedButtons (ArrayList<String> fragmentsNames){


        for (String name : fragmentsNames) {
            if (name.equals(getResourceString(R.string.question_2))){

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
                    Gson gson = new Gson();
                    doctor = gson.fromJson(jsonString , UserInfoResponse.class);
                    Log.d("doctor Data from QR",jsonString );
                    showProgressDialog();
                    new HttpCall(getActivity(), new ApiResponse() {
                        @Override
                        public void onSuccess(Object response) {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("doctor_data" ,jsonString);
                            startActivity(intent);
                            dismissProgressDialog();
                            getActivity().finish();
                        }

                        @Override
                        public void onFailed(String error) {
                            dismissProgressDialog();
                        }
                    }).sendPopUpResult(AppController.getInstance().getClientInfo().getUser_id(),
                            AppController.getInstance().getClientInfo().getPassword(),
                            String.valueOf(doctor.getUser().get_Id()),
                            InquiryActivity.inquiryResult);
                    break;
                }
                else {
                    Toast.makeText(getActivity() , "Bitte wählen Sie eine Option. Abbrechen über Zurück." , Toast.LENGTH_LONG).show();
                }
        }
    }

    public Bundle addFirstLevelBundle(String levelName) {

        Bundle bundle = new Bundle();
        bundle.putString(getResourceString(R.string.first_level), levelName);
        return bundle;
    }

    public String getResourceString (int resource){
        return getResources().getString(resource);
    }

    public void showProgressDialog(){progressDialog = ProgressDialog.show(getActivity(), "", "Bitte, Warten Sie...", true);}

    public void dismissProgressDialog() {progressDialog.dismiss();}

}
