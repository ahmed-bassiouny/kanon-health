package com.germanitlab.kanonhealth.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mo on 3/22/17.
 */

public class UpdateDocumentPrivacyRequest implements Serializable {

    @SerializedName("user_id")
    private String userId;
    @SerializedName("password")
    private String password;
    private int messageId;
    private int privacy;

}
