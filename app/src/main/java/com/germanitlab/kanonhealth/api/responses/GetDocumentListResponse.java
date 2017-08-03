package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Document;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by norhan on 8/2/17.
 */

public class GetDocumentListResponse extends ParentResponse{

    @SerializedName(KEY_DATA)
    private
    ArrayList<Document> data;

    public ArrayList<Document> getData() {
        return data;
    }

    public void setData(ArrayList<Document> data) {
        this.data = data;
    }
}
