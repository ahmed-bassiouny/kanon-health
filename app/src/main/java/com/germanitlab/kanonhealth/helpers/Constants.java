package com.germanitlab.kanonhealth.helpers;

import android.os.Environment;


import java.io.File;
import java.util.HashMap;

/**
 * Created by eslam on 12/30/16.
 */

public class Constants {

    public static final String CHAT_SERVER_URL = "http://api.gagabay.com";
    public static final String CHAT_SERVER_URL_IMAGE = "http://api.gagabay.com";
    public static final String UPLOAD_URL = "https://chat.gagabay.com/upload";




    public static final String REGISER_RESPONSE = "registerResponse";
    public static final String USER_ID = "userID";
    public static final String PASSWORD = "password";

    public static final String TEXT = "text";
    public static final String AUDIO = "audio";
    public static final String VIDEO = "video";
    public static final String IMAGE = "image";
    public static final String UNDEFINED = "undefined";
    public static final String IMAGE_TEXT = "imagePlusText";
    public static final String LOCATION = "location";
    public static final int IMAGE_REQUEST = 150;
    public static final int GALLERY_PERMISSION_CODE = 6;

    // Edit ahmed 11-6-2017
    public static final int SPECIALITIES = 8;
    public static final int LANGUAUGE = 9;
    public static final int MEMBERAT = 10;
    public static final int DoctorAll = 11;


    public static final int OPEN_CHAT = 1;

    public static final int TEXT_MESSAGE = 1;
    public static final int IMAGE_MESSAGE = 4;
    public static final int UNDEFINED_MESSAGE = 9;
    public static final int AUDIO_MESSAGE = 2;
    public static final int VIDEO_MESSAGE = 3;
    public static final int LOCATION_MESSAGE = 5;
    public static final int CHAT_LAYOUT = 6;
    public static final String USER_INFO = "userInfo";
    public static final java.lang.String TERMS = "terms";
    public static final java.lang.String FAQ = "faq";


    public static final int CAMERA_PERMISSION_CODE = 1;
    public static final int VIDEO_PERMISSION_CODE = 2;
    public static final int LAST_LOCATION_PERMISSION_CODE = 3;
    public static final int READ_EXTERNAL_STORARE_PERMISSION_CODE = 4;
    public static final int WRITE_EXTERNAL_STORAGE=6;
    public static final int AUDIO_PERMISSION_CODE = 7;
    public static final int GET_LAST_LOCATION_PERMISSION_CODE = 5;
    public static final String DATA = "data";
    public static final int HOURS_CODE = 11;
    public static final int HOURS_TYPE_CODE = 22;
    public static final int HOURS_OPENING = 23;
    public static final int TIME_TABLE_TYPE = 30;


    public static File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Health");


    public static final int PENDING_STATUS = 1;
    public static final int SENT_STATUS = 2;
    public static final int DELIVER_STATUS = 3;
    public static final int SEEN_STATUS = 4;

    public static final String CHOSED_LIST = "Chosed_list";





}
