package com.germanitlab.kanonhealth.inquiry;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.germanitlab.kanonhealth.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mo on 3/6/17.
 */

public class TwoQuestionsFragment extends Fragment {

    @BindView(R.id.text_question1_fragment)
    TextView textFirstQuestion;
    @BindView(R.id.edit_answer1_fragment)
    EditText textFirstAnswer;
    @BindView(R.id.text_question2_fragment)
    TextView textSecondQuestion;
    @BindView(R.id.edit_answer2_fragment)
    EditText textSecondAnswer;
    @BindView(R.id.button_finish)
    ImageView finish;

    private OnChoiceSelectedListener mCallback;

    private String mFirstLevelName;

    private String mSecondLevelName;

    private String firstQuestionKey, secondQuestionKey;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.question_2_fragment, container, false);
        ButterKnife.bind(this, root);

        Bundle bundle = getArguments();
        if (bundle != null) {

            mFirstLevelName = bundle.getString(getResourceString(R.string.first_level));
            mSecondLevelName = bundle.getString(getResourceString(R.string.secondLevel));

            if (mSecondLevelName.equals(getResourceString(R.string.question_22))) {
                textFirstQuestion.setText(getResources().getString(R.string.uberweisung_first_question));
                textSecondQuestion.setText(getResources().getString(R.string.uberweisung_second_question));
                firstQuestionKey = getResourceString(R.string.question_221);
                secondQuestionKey = getResourceString(R.string.question_222);
            } else if (mSecondLevelName.equals(getResourceString(R.string.question_23))) {
                textFirstQuestion.setText(getResources().getString(R.string.krankenhaus_first_question));
                textSecondQuestion.setText(getResources().getString(R.string.krankenhaus_second_question));
                firstQuestionKey = getResourceString(R.string.question_231);
                secondQuestionKey = getResourceString(R.string.question_232);
            } else if (mSecondLevelName.equals(getResourceString(R.string.question_24))) {
                textFirstQuestion.setText(getResources().getString(R.string.au_first_question));
                textSecondQuestion.setText(getResources().getString(R.string.au_second_question));
                firstQuestionKey = getResourceString(R.string.question_241);
                secondQuestionKey = getResourceString(R.string.question_242);
            } else if (mSecondLevelName.equals(getResourceString(R.string.question_25))) {
                textFirstQuestion.setText(getResources().getString(R.string.attest_first_question));
                textSecondQuestion.setText(getResources().getString(R.string.attest_second_question));
                firstQuestionKey = getResourceString(R.string.question_251);
                secondQuestionKey = getResourceString(R.string.question_252);
            }
        }
        textSecondAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                onFinishedButtonClicked();
                return true;
            }
        });
        return root;
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

    @OnClick(R.id.button_finish)
    public void onFinishedButtonClicked() {

        String firstAnswer = textFirstAnswer.getText().toString();
        String secondAnswer = textSecondAnswer.getText().toString();
        if (!firstAnswer.equals("") && !secondAnswer.equals("")) {

            //Adding the Choice to the finished choices Set
            InquiryActivity.finishedSecondLevelOptions.add(mSecondLevelName);

            if (InquiryActivity.finishedSecondLevelOptions.size() >= 4) {
                //Checks if all the buttons were finished and added already to the set
                if (InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_21)) &&
                        InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_22)) &&
                        InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_23)) &&
                        InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_24)) &&
                        InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_25))) {
                    InquiryActivity.finishedFirstLevelOptions.add(mFirstLevelName);
                }
            }

            //Creating the hashMap Object for the end result of this choice
            HashMap<String, String> mTwoQuestionMap = new HashMap<>();
            mTwoQuestionMap.put(getResourceString(R.string.first_level), mFirstLevelName);
            mTwoQuestionMap.put(getResourceString(R.string.secondLevel), mSecondLevelName);
            mTwoQuestionMap.put(firstQuestionKey, firstAnswer);
            mTwoQuestionMap.put(secondQuestionKey, secondAnswer);

            InquiryActivity.inquiryResult.add(mTwoQuestionMap);

            mCallback.OnChoiceSelected(mFirstLevelName, null);
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }



        } else {
            Toast.makeText(getActivity(), "Bitte schreibe deine Antwort heir", Toast.LENGTH_SHORT).show();
        }
    }

    public String getResourceString(int resource) {
        return getResources().getString(resource);
    }
}
