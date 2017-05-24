package com.germanitlab.kanonhealth.models.Questions;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eslam on 2/4/17.
 */

public class Answers implements Serializable {

    @SerializedName("question")
    private String question;
    @SerializedName("answer")
    private String answer;

    public Answers(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
