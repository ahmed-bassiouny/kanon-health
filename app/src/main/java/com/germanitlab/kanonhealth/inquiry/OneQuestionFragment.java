package com.germanitlab.kanonhealth.inquiry;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
 * Created by Mo on 3/8/17.
 */

public class OneQuestionFragment extends Fragment {

    private String mFirstLevelName;
    private String mSecondLevelName;
    OnChoiceSelectedListener mCallback;
    @BindView(R.id.text_question_fragment)
    TextView question;
    @BindView(R.id.edit_answer_fragment)
    EditText answer;
    private boolean questionVisibility = true;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.question_1_fragment, container, false);
        ButterKnife.bind(this, root);

        Bundle bundle = getArguments();
        if (bundle != null) {

            mFirstLevelName = bundle.getString(getResources().getString(R.string.first_level));

            if (mFirstLevelName.equals(getResources().getString(R.string.question_3)) || mFirstLevelName.equals(getResources().getString(R.string.question_4))) {

                if (mFirstLevelName.equals(getResources().getString(R.string.question_3))) {
                    question.setText(getResources().getString(R.string.beratung_frage));
                } else if (mFirstLevelName.equals(getResources().getString(R.string.question_4))) {
                    question.setText(getResources().getString(R.string.termin_frage));
                }
            } else {

                mSecondLevelName = bundle.getString(getResources().getString(R.string.second_Level));
                if (mSecondLevelName.equals(getResources().getString(R.string.question_21))) {
                    question.setText(getResources().getString(R.string.rp_question));
                } else if (mSecondLevelName.equals(getResources().getString(R.string.question_11))) {
                    question.setText(getResourceString(R.string.question_11_12_13));
                } else if (mSecondLevelName.equals(getResources().getString(R.string.question_12))) {
                    question.setText(getResourceString(R.string.question_11_12_13));
                } else if (mSecondLevelName.equals(getResources().getString(R.string.question_13))) {
                    question.setText(getResourceString(R.string.question_11_12_13));
                } else if (mSecondLevelName.equals(getResources().getString(R.string.question_14))) {
                    question.setVisibility(View.INVISIBLE);
                    questionVisibility = false;
                }
            }

        }
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


    public void onFinishedButtonClicked() {
        String text = answer.getText().toString();
        if (!text.equals("")) {

            HashMap<String, String> oneQuestionMap = new HashMap<>();
            oneQuestionMap.put(getResources().getString(R.string.first_level), mFirstLevelName);

            if (mFirstLevelName.equals(getResources().getString(R.string.question_3)) || mFirstLevelName.equals(getResources().getString(R.string.question_4))) {

                if (mFirstLevelName.equals(getResources().getString(R.string.question_3))) {
                    oneQuestionMap.put(getResourceString(R.string.second_Level), getResourceString(R.string.question_31));
                    oneQuestionMap.put(getResources().getString(R.string.question_31), text);

                } else if (mFirstLevelName.equals(getResources().getString(R.string.question_4))) {
                    oneQuestionMap.put(getResources().getString(R.string.question_41), text);
                    oneQuestionMap.put(getResourceString(R.string.second_Level), getResourceString(R.string.question_41));
                }
                InquiryActivity.finishedFirstLevelOptions.add(mFirstLevelName);

            } else {
                if (mSecondLevelName.equals(getResourceString(R.string.question_12)) ||
                        mSecondLevelName.equals(getResourceString(R.string.question_11)) ||
                        mSecondLevelName.equals(getResourceString(R.string.question_13))) {

                    if (mSecondLevelName.equals(getResourceString(R.string.question_11))) {
                        oneQuestionMap.put(getResourceString(R.string.second_Level), getResourceString(R.string.question_11));
                        oneQuestionMap.put(getResourceString(R.string.question_11), text);
                        InquiryActivity.finishedSecondLevelOptions.add(getResourceString(R.string.question_11));

                    } else if (mSecondLevelName.equals(getResourceString(R.string.question_12))) {
                        oneQuestionMap.put(getResourceString(R.string.second_Level), getResourceString(R.string.question_12));
                        oneQuestionMap.put(getResourceString(R.string.question_12), text);
                        InquiryActivity.finishedSecondLevelOptions.add(getResourceString(R.string.question_12));

                    } else if (mSecondLevelName.equals(getResourceString(R.string.question_13))) {
                        oneQuestionMap.put(getResourceString(R.string.second_Level), getResourceString(R.string.question_13));
                        oneQuestionMap.put(getResourceString(R.string.question_13), text);
                        InquiryActivity.finishedSecondLevelOptions.add(getResourceString(R.string.question_13));
                    }
                } else if (!questionVisibility) {
                    oneQuestionMap.put(getResourceString(R.string.second_Level), getResourceString(R.string.question_14));
                    oneQuestionMap.put(getResourceString(R.string.question_14), text);
                    InquiryActivity.finishedSecondLevelOptions.add(getResourceString(R.string.question_14));
                } else {
                    oneQuestionMap.put(getResourceString(R.string.second_Level),
                            getResourceString(R.string.question_21));
                    oneQuestionMap.put(getResourceString(R.string.question_212), text);

                    InquiryActivity.finishedSecondLevelOptions.add(getResourceString(R.string.question_21));
                }
            }


            //Check finished levels
            checkFinishedLevels();

            InquiryActivity.inquiryResult.add(oneQuestionMap);
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }


           getFragmentManager().popBackStack();
            // mCallback.OnChoiceSelected(mFirstLevelName, null);

        } else {
            Toast.makeText(getActivity(), R.string.please_write_your_answer_here, Toast.LENGTH_SHORT).show();
        }
    }

    public void checkFinishedLevels() {
        if (InquiryActivity.finishedSecondLevelOptions.size() >= 4) {
            //Checks if all the buttons were finished and added already to the set
            if (InquiryActivity.finishedSecondLevelOptions.contains(getResources().getString(R.string.question_11)) &&
                    InquiryActivity.finishedSecondLevelOptions.contains(getResources().getString(R.string.question_12)) &&
                    InquiryActivity.finishedSecondLevelOptions.contains(getResources().getString(R.string.question_13)) &&
                    InquiryActivity.finishedSecondLevelOptions.contains(getResources().getString(R.string.question_14))) {
                InquiryActivity.finishedFirstLevelOptions.add(getResourceString(R.string.question_1));
            }

            if (InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_21)) &&
                    InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_22)) &&
                    InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_23)) &&
                    InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_24)) &&
                    InquiryActivity.finishedSecondLevelOptions.contains(getResourceString(R.string.question_25))) {
                InquiryActivity.finishedFirstLevelOptions.add(getResourceString(R.string.question_2));
            }
        }
    }

    public String getResourceString(int resource) {
        return getResources().getString(resource);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.question, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save){
            onFinishedButtonClicked();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
}
