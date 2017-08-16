package com.germanitlab.kanonhealth.api.parameters;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by bassiouny on 02/08/17.
 */

public class OpenSessionParameters extends ParentParameters {

    public static final String PARAMETER_USERID = "user_id";
    public static final String PARAMETER_DOCTOR_ID = "doc_id";
    public static final String PARAMETER_QUESTIONS_ANSWERS="questions_answers";

    @SerializedName(PARAMETER_USERID)
    private String userID;
    @SerializedName(PARAMETER_DOCTOR_ID)
    private String doctorID;
    @SerializedName(PARAMETER_QUESTIONS_ANSWERS)
    private HashMap<String,String> questionsAnswers;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public HashMap<String, String> getQuestionsAnswers() {
        return questionsAnswers;
    }

    public void setQuestionsAnswers(HashMap<String, String> questionsAnswers) {
        this.questionsAnswers = questionsAnswers;
    }
}
