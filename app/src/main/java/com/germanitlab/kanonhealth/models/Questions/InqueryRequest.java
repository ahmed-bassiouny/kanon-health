package com.germanitlab.kanonhealth.models.Questions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mo on 2/15/17.
 */

public class InqueryRequest {

    private int user_id;
    private String password;
    private String doc_id;
    private ArrayList<HashMap<String, String>> answers;

    public InqueryRequest(int user_id, String password, String doc_id, ArrayList<HashMap<String, String>> answers) {
        this.user_id = user_id;
        this.doc_id = doc_id;
        this.password = password;
        this.answers = answers;
    }
}
