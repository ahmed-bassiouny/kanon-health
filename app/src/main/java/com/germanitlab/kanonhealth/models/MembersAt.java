package com.germanitlab.kanonhealth.models;

import java.io.Serializable;

/**
 * Created by halima on 07/06/17.
 */

public class MembersAt implements Serializable{
    /*
    id": "2",
"avatar": "spcs_icons/spc-icon.png",
"last_name": "sayed",
"first_name": "omar"
     */
    private int id;
    private String avatar;
    private String last_name;
    private String first_name;

    public int getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getName() {
        return first_name;
    }
}
