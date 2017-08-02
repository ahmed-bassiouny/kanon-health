package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Document;
import com.google.gson.annotations.SerializedName;

/**
 * Created by norhan on 8/2/17.
 */

public class AddDocumentResponse extends ParentResponse {

    @SerializedName(KEY_DATA)
    private
    Document data;

    public Document getData() {
        return data;
    }

    public void setData(Document data) {
        this.data = data;
    }
}
