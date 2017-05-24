package com.germanitlab.kanonhealth.models.Questions;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by eslam on 2/4/17.
 */

public class SubmitQuestionRequest implements Serializable {

    @SerializedName("user_id")
    private String userID;
    @SerializedName("password")
    private String password;
    @SerializedName("doc_id")
    private String docID;
    @SerializedName("answers")
    private List<Answers> answersList;

    public SubmitQuestionRequest(String userID, String password, String docID, List<Answers> answersList) {
        this.userID = userID;
        this.password = password;
        this.docID = docID;
        this.answersList = answersList;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public List<Answers> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(List<Answers> answersList) {
        this.answersList = answersList;
    }
}
