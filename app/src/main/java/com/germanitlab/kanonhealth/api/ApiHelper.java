package com.germanitlab.kanonhealth.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.germanitlab.kanonhealth.api.models.Register;
import com.germanitlab.kanonhealth.api.parameters.RegisterParameters;
import com.germanitlab.kanonhealth.api.responses.RegisterResponse;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by milad on 7/31/17.
 */

public class ApiHelper {

    //region Api Urls

    private static final String API_CLINICS_LIST = "clinics/list";
    private static final String API_CLINICS_ADD = "clinics/add";
    private static final String API_CLINICS_EDIT = "clinics/edit";

    private static final String API_DOCTORS_ADD = "doctors/add";
    private static final String API_DOCTORS_UPDATE = "doctors/update";
    private static final String API_DOCTORS_LIST = "doctors/list";
    private static final String API_DOCTORS_ANOTHER = "doctors/another";
    private static final String API_DOCTORS_CHANGE_STATUS = "doctors/change_status";
    private static final String API_DOCTORS_CHANGE_RATE = "doctors/rate";

    private static final String API_DOCUMENTS_ADD = "document/add";

    private static final String API_LANGUAGES_LIST = "langs/list";

    private static final String API_MESSAGES_SEND = "messages/send";
    private static final String API_MESSAGES_FORWARD = "messages/forward";
    private static final String API_MESSAGES_DELETE = "messages/delete";
    private static final String API_MESSAGES_DELIVER = "messages/deliver";
    private static final String API_MESSAGES_SEEN = "messages/seen";
    private static final String API_MESSAGES_LIST = "messages/messages";
    private static final String API_MESSAGES_CHAT_ANOTHER = "messages/chat/another";
    private static final String API_MESSAGES_CHAT_CLINIC = "messages/chat/clinic";
    private static final String API_MESSAGES_CHAT_DOCTOR = "messages/chat/doctor";
    private static final String API_MESSAGES_CHAT_USER = "messages/chat/users";

    private static final String API_REQUESTS_OPEN = "requests/open";
    private static final String API_REQUESTS_CLOSE = "requests/close";
    private static final String API_REQUESTS_LIST = "requests/list";

    private static final String API_SPECIALITIES_LIST = "speciality/list";

    private static final String API_USERS_LIST = "users/me";
    private static final String API_USERS_ADD = "users/add";
    private static final String API_USERS_REGISTER = "users/register";


    private static final String API_UPLOAD = "upload";

    //endregion

    //region Configuration

    public static final String SERVER_API_URL = "http://192.168.1.51:8000/V1/";
    public static final String SERVER_IMAGE_URL = "";

    private static final String TAG = "ApiHelper";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private static String post(String url, String parameters) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, parameters);
        Request request = new Request.Builder()
                .url(SERVER_API_URL + url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    private static String postWithFile(String url, String parameters, File file, String fileParameterName, String mediaType) throws IOException {
        String result = "";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = MultipartBody.create(JSON, parameters);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileParameterName, file.getName(), RequestBody.create(MediaType.parse(mediaType), file))
                .addPart(body)
                .build();

        Request request = new Request.Builder()
                .url(SERVER_API_URL + url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

    }

    //endregion

    //region Api Calls

    public static Register postRegister(String countryCode, String phone, Context context) {
        Register result = null;
        try {
            RegisterParameters registerParameters = new RegisterParameters();
            registerParameters.setCountryCode(countryCode);
            registerParameters.setPhone(phone);

            String jsonString = post(API_USERS_REGISTER, registerParameters.toJson());
            Gson gson = new Gson();
            RegisterResponse registerResponse = gson.fromJson(jsonString, RegisterResponse.class);
            if (registerResponse.getStatus()) {
                result = registerResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "postRegister", e, -1, context);
        } finally {
            return result;
        }
    }


    //endregion
}
