package com.germanitlab.kanonhealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.germanitlab.kanonhealth.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Geram IT Lab on 26/02/2017.
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
        String question = (new ArrayList<String>(questionAnswer.keySet())).get(position);
        holder.tv_question.setText(question);
        String answer = (new ArrayList<String>(questionAnswer.values())).get(position);
        holder.tv_answer.setText(answer);
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
