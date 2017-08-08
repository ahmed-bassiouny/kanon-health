package com.germanitlab.kanonhealth.api.responses;

import com.germanitlab.kanonhealth.api.models.Review;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bassiouny on 08/08/17.
 */

public class ReviewResponse extends ParentResponse{

    @SerializedName(KEY_DATA)
    private Review data;

    public Review getData() {
        return data;
    }

    public void setData(Review data) {
        this.data = data;
    }
}
