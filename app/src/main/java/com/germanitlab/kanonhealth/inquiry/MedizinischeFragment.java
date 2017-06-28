package com.germanitlab.kanonhealth.inquiry;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.germanitlab.kanonhealth.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mo on 3/6/17.
 */

public class MedizinischeFragment extends Fragment {

    @BindView(R.id.button_rp)
    Button buttonRp;
    @BindView(R.id.button_uberweisung)
    Button buttonUberweisung;
    @BindView(R.id.button_krankenhaus)
    Button buttonKrankenhaus;
    @BindView(R.id.button_au)
    Button buttonAu;
    @BindView(R.id.button_attest)
    Button buttonAttest;


    private Bundle mMedizinischeBundle;
    private String mFirstLevelName;
    private OnChoiceSelectedListener mCallback;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root;

        root = inflater.inflate(R.layout.mediziinische, container, false);
        ButterKnife.bind(this, root);

        mMedizinischeBundle = getArguments();
        if (mMedizinischeBundle != null) {
            mFirstLevelName = mMedizinischeBundle.getString(getResourceString(R.string.first_level));
        }


        return root;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_21)))
            buttonRp.setEnabled(false);
        if (InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_22)))
            buttonUberweisung.setEnabled(false);
        if (InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_23)))
            buttonKrankenhaus.setEnabled(false);
        if (InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_24)))
            buttonAu.setEnabled(false);
        if (InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_25)))
            buttonAttest.setEnabled(false);
    }

    @OnClick({R.id.button_rp, R.id.button_uberweisung, R.id.button_krankenhaus, R.id.button_au, R.id.button_attest})
    public void OnClickListener(View view) {

        Fragment thirdLevelFragment = new TwoQuestionsFragment();
        switch (view.getId()) {
            case R.id.button_rp:
                OneQuestionFragment oneQuestionFragment = new OneQuestionFragment();

                oneQuestionFragment.setArguments(addSecondLevelName(getResourceString(R.string.question_21)));

                mCallback.OnChoiceSelected(mFirstLevelName, oneQuestionFragment);
                break;
            case R.id.button_uberweisung:

                thirdLevelFragment.setArguments(addSecondLevelName(getResourceString(R.string.question_22)));

                mCallback.OnChoiceSelected(mFirstLevelName, thirdLevelFragment);
                break;
            case R.id.button_krankenhaus:

                thirdLevelFragment.setArguments(addSecondLevelName(getResourceString(R.string.question_23)));

                mCallback.OnChoiceSelected(mFirstLevelName, thirdLevelFragment);
                break;
            case R.id.button_au:

                thirdLevelFragment.setArguments(addSecondLevelName(getResourceString(R.string.question_24)));

                mCallback.OnChoiceSelected(mFirstLevelName, thirdLevelFragment);
                break;
            case R.id.button_attest:

                thirdLevelFragment.setArguments(addSecondLevelName(getResourceString(R.string.question_25)));

                mCallback.OnChoiceSelected(mFirstLevelName, thirdLevelFragment);
                break;
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

    public Bundle addSecondLevelName(String secondLevelName) {
        Bundle bundle = new Bundle();
        bundle.putString(getResourceString(R.string.first_level), mFirstLevelName);
        bundle.putString(getResourceString(R.string.secondLevel), secondLevelName);
        return bundle;
    }

    public String getResourceString(int resource) {
        return getResources().getString(resource);
    }
}
