package com.germanitlab.kanonhealth.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eslam on 1/28/17.
 */

public class UserInfoResponse implements Serializable {

    /* {
    "doctor": {
        "id": 132,
        "email": "eslam@gmail.com",
        "password": "c81b580994365f0b49e14ca841d9d90f",
        "last_login": null,
        "name": "Eslam A.Gwad",
        "avatar": "",
        "subtitle": " ",
        "phone": "166253101",
        "country_code": "+020",
        "active": 1,
        "is_doc": 0,
        "platform": 0,
        "token": "",
        "info": {
            "Birthday": "",
            "Streetname": "",
            "House number": "",
            "Zip Code": "",
            "Provinz": "",
            "Country": ""
        },
        "location_lat": null,
        "location_long": null,
        "payment": null,
        "settings": null,
        "questions": null,
        "address": null,
        "birth_date": null,
        "speciality": null,
        "about": null,
        "last_online": null,
        "unreaded_count": null,
        "qr_url": "qr/get_image/132:9de228839b2ccc8d08071bcada88ca6d"
    },
    "status": 1
}
*/

    @SerializedName("user")
    private User user;
    @SerializedName("status")
    private String status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
