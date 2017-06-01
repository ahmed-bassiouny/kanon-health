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

public class UntersuchungFragment extends Fragment {


    private String mFirstLevelName;
    OnChoiceSelectedListener mCallback;

    @BindView(R.id.button_koperliche)
    Button buttonKoperliche;
    @BindView(R.id.button_laboruntersuchung)
    Button buttonLaboruntersuchung;
    @BindView(R.id.button_techische)
    Button buttonTechische;
    @BindView(R.id.button_sonstiges)
    Button buttonSonstiges;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.untersuchung, container, false);
        ButterKnife.bind(this, root);


        Bundle bundle = getArguments();
        if (bundle != null) {
            mFirstLevelName = bundle.getString(getResourceString(R.string.first_level));
        }
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //Make sure the container activity implemented the callback interface.
        try {
            mCallback = (OnChoiceSelectedListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnChoiceSelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_11)))
            buttonKoperliche.setEnabled(false);
        if (InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_12)))
            buttonLaboruntersuchung.setEnabled(false);
        if (InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_13)))
            buttonTechische.setEnabled(false);
        if (InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_14)))
            buttonSonstiges.setEnabled(false);
    }

    @OnClick({R.id.button_koperliche, R.id.button_laboruntersuchung, R.id.button_techische ,R.id.button_sonstiges})
    public void onClickListener(View view) {
        OneQuestionFragment oneQuestionFragment = new OneQuestionFragment();
        Bundle bundle = new Bundle();

        switch (view.getId()) {
            case R.id.button_koperliche:

                bundle.putString(getResourceString(R.string.first_level), mFirstLevelName);
                bundle.putString(getResourceString(R.string.secondLevel), getResourceString(R.string.question_11));
                oneQuestionFragment.setArguments(bundle);
                mCallback.OnChoiceSelected(getResourceString(R.string.question_11), oneQuestionFragment);
                break;
            case R.id.button_laboruntersuchung:

                bundle.putString(getResourceString(R.string.first_level), mFirstLevelName);
                bundle.putString(getResourceString(R.string.secondLevel), getResourceString(R.string.question_12));
                oneQuestionFragment.setArguments(bundle);
                mCallback.OnChoiceSelected(getResourceString(R.string.question_12), oneQuestionFragment);
                break;
            case R.id.button_techische:

                bundle.putString(getResourceString(R.string.first_level), mFirstLevelName);
                bundle.putString(getResourceString(R.string.secondLevel), getResourceString(R.string.question_13));
                oneQuestionFragment.setArguments(bundle);
                mCallback.OnChoiceSelected(getResourceString(R.string.question_13), oneQuestionFragment);
                break;
            case R.id.button_sonstiges:

                bundle.putString(getResourceString(R.string.first_level), mFirstLevelName);
                bundle.putString(getResourceString(R.string.secondLevel), getResourceString(R.string.question_14));
                oneQuestionFragment.setArguments(bundle);
                mCallback.OnChoiceSelected(getResourceString(R.string.question_14), oneQuestionFragment);
                break;
        }
    }

    public String getResourceString (int resource){
        return getResources().getString(resource);
    }

}
