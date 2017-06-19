package com.germanitlab.kanonhealth.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Geram IT Lab on 19/03/2017.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder>

{
    private Context mContext ;

    LinkedHashMap<String , String> questionAnswer ;

    public QuestionAdapter(LinkedHashMap<String , String> questionAnswer , Context mContext)
    {
        this.questionAnswer = questionAnswer ;
        this.mContext = mContext  ;

    }


    @Override
    public QuestionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.question_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuestionAdapter.MyViewHolder holder, int position)
    {
        try {
            String question = (new ArrayList<String>(questionAnswer.keySet())).get(position);
            holder.tv_question.setText(question);
            String answer = (new ArrayList<String>(questionAnswer.values())).get(position);
            holder.tv_answer.setText(answer);
        }catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(mContext, mContext.getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return questionAnswer.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_question, tv_answer;

        public MyViewHolder(View view) {
            super(view);
            tv_question = (TextView) view.findViewById(R.id.question);
            tv_answer = (TextView) view.findViewById(R.id.answer);
        }
    }


}
