package com.germanitlab.kanonhealth.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.germanitlab.kanonhealth.api.models.Clinic;
import com.germanitlab.kanonhealth.api.models.Document;
import com.germanitlab.kanonhealth.api.models.Language;
import com.germanitlab.kanonhealth.api.models.Message;
import com.germanitlab.kanonhealth.api.models.Register;
import com.germanitlab.kanonhealth.api.models.Review;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.SupportedLang;
import com.germanitlab.kanonhealth.api.parameters.AddClinicParameters;
import com.germanitlab.kanonhealth.api.parameters.AddDocumentParameters;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.parameters.AddOrEditClinicParameters;
import com.germanitlab.kanonhealth.api.parameters.CloseSessionParameters;
import com.germanitlab.kanonhealth.api.parameters.ChangeStatusParameters;
import com.germanitlab.kanonhealth.api.parameters.DocumentPrivacyParameters;
import com.germanitlab.kanonhealth.api.parameters.EditClinicParameters;
import com.germanitlab.kanonhealth.api.parameters.EditDoctorParameter;
import com.germanitlab.kanonhealth.api.parameters.EditPatientParameter;
import com.germanitlab.kanonhealth.api.parameters.GetClinicParameters;
import com.germanitlab.kanonhealth.api.parameters.GetDocumentListParameters;
import com.germanitlab.kanonhealth.api.parameters.MessageForwardParameter;
import com.germanitlab.kanonhealth.api.parameters.MessageOperationParameter;
import com.germanitlab.kanonhealth.api.parameters.MessageSendParameters;
import com.germanitlab.kanonhealth.api.parameters.MessagesParameter;
import com.germanitlab.kanonhealth.api.parameters.OpenSessionParameters;
import com.germanitlab.kanonhealth.api.parameters.RateDoctorParameter;
import com.germanitlab.kanonhealth.api.parameters.RegisterParameters;
import com.germanitlab.kanonhealth.api.parameters.TokenAddParameter;
import com.germanitlab.kanonhealth.api.parameters.UserAddParameter;
import com.germanitlab.kanonhealth.api.parameters.UserInfoParameter;
import com.germanitlab.kanonhealth.api.responses.AddClinicResponse;
import com.germanitlab.kanonhealth.api.responses.AddDocumentResponse;
import com.germanitlab.kanonhealth.api.responses.ChangeStatusResponse;
import com.germanitlab.kanonhealth.api.responses.ChatResponse;
import com.germanitlab.kanonhealth.api.responses.DocumentPrivacyResponse;
import com.germanitlab.kanonhealth.api.responses.EditClinicResponse;
import com.germanitlab.kanonhealth.api.responses.GetClinicListResponse;
import com.germanitlab.kanonhealth.api.responses.GetClinicResponse;
import com.germanitlab.kanonhealth.api.responses.GetDoctorListResponse;
import com.germanitlab.kanonhealth.api.responses.GetDocumentListResponse;
import com.germanitlab.kanonhealth.api.responses.LanguageResponse;
import com.germanitlab.kanonhealth.api.responses.MessageSendResponse;
import com.germanitlab.kanonhealth.api.responses.MessagesResponse;
import com.germanitlab.kanonhealth.api.responses.NetworkLocationResponse;
import com.germanitlab.kanonhealth.api.responses.OpenSessionResponse;
import com.germanitlab.kanonhealth.api.responses.ParentResponse;
import com.germanitlab.kanonhealth.api.responses.RateDoctorResponse;
import com.germanitlab.kanonhealth.api.responses.RegisterResponse;
import com.germanitlab.kanonhealth.api.responses.ReviewResponse;
import com.germanitlab.kanonhealth.api.responses.SpecialityResponse;
import com.germanitlab.kanonhealth.api.responses.UserInfoResponse;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.MimeUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    private static final String API_CLINICS_GET = "clinics/get";

    private static final String API_DOCTORS_ADD = "doctors/add";
    private static final String API_DOCTORS_LIST = "doctors/list";
    private static final String API_DOCTORS_ANOTHER = "doctors/another";
    private static final String API_DOCTORS_CHANGE_STATUS = "doctors/change_status";
    private static final String API_DOCTORS_RATE = "doctors/rate";

    private static final String API_DOCUMENTS_ADD = "document/add";
    private static final String API_DOCUMENTS_LIST = "document/list";
    private static final String API_DOCUMENT_PRIVACY = "document/privacy";

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

    private static final String API_USERS_ME = "users/me";
    private static final String API_USERS_ADD = "users/add";
    private static final String API_USERS_EDIT = "users/edit";
    private static final String API_USERS_REGISTER = "users/register";


    private static final String API_RATE_REVIEW = "users/rate_reviews";

    private static final String API_TOKEN_REGISTER = "token/add";


    private static final String API_UPLOAD = "upload";


    private static final String API_NETWORK_LOCATION = "http://ip-api.com/json";

    //endregion

    //region Configuration

    public static final String SERVER_API_URL = "http://api.gagabay.com/V1/";
    public static final String SERVER_IMAGE_URL = "http://api.gagabay.com/";

    private static final String TAG = "ApiHelper";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String EMPTY_JSON = "{}";


    private static String post(String url, String parameters) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, parameters);
        Request request = new Request.Builder()
                .url(SERVER_API_URL + url)
                .post(body)
                .build();
        Log.i(TAG, request.toString());
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static String postWithoutServerPath(String url, String parameters) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, parameters);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Log.i(TAG, request.toString());
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    private static String postWithFile(String url, String parameters, File file, String fileParameterName) throws IOException {
        String result = "";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = MultipartBody.create(JSON, parameters);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileParameterName, file.getName(), RequestBody.create(MediaType.parse(MimeUtils.getType(file.getName())), file))
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

    public static ArrayList<Clinic> postGetClinicList(Context context) {
        ArrayList<Clinic> result = new ArrayList<>();
        try {

            String jsonString = post(API_CLINICS_LIST, EMPTY_JSON);
            Gson gson = new Gson();
            GetClinicListResponse getClinicListResponse = gson.fromJson(jsonString, GetClinicListResponse.class);
            if (getClinicListResponse.getStatus()) {
                result = getClinicListResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "postGetClinicList", e, -1, context);
        } finally {
            return result;
        }
    }

    public static Clinic postAddClinic(Integer userId, String name, String speciality, String streetName, String houseNumber, String zipCode, String city, String province, String country, String phone, String supportedLangs, File file, Context context) {
        Clinic result = null;
        try {
            AddClinicParameters addClinicParameters = new AddClinicParameters();
            addClinicParameters.setUserId(userId);
            addClinicParameters.setName(name);
            addClinicParameters.setSpeciality(speciality);
            addClinicParameters.setStreetName(streetName);
            addClinicParameters.setHouseNumber(houseNumber);
            addClinicParameters.setZipCode(zipCode);
            addClinicParameters.setCity(city);
            addClinicParameters.setProvince(province);
            addClinicParameters.setCountry(country);
            addClinicParameters.setPhone(phone);
            addClinicParameters.setSupportedLangs(supportedLangs);
            String jsonString = "";
            if (file == null) {
                jsonString = post(API_CLINICS_ADD, addClinicParameters.toJson());
            } else {
                jsonString = postWithFile(API_CLINICS_ADD, addClinicParameters.toJson(), file, addClinicParameters.PARAMETER_AVATAR);
            }

            Gson gson = new Gson();
            AddClinicResponse addClinicResponse = gson.fromJson(jsonString, AddClinicResponse.class);
            if (addClinicResponse.getStatus()) {
                result = addClinicResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "postClinicAdd", e, -1, context);
        } finally {
            return result;
        }
    }

    public static Clinic postGetClinic(Integer clinicId, Context context) {
        Clinic result = null;
        try {
            GetClinicParameters getClinicParameters = new GetClinicParameters();
            getClinicParameters.setClinicId(clinicId);
            String jsonString = post(API_CLINICS_GET, getClinicParameters.toJson());
            Gson gson = new Gson();
            GetClinicResponse getClinicResponse = gson.fromJson(jsonString, GetClinicResponse.class);
            if (getClinicResponse.getStatus()) {
                result = getClinicResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "postClinicGet", e, -1, context);
        } finally {
            return result;
        }
    }

    public static Integer postEditClinic(Integer clinicId, String name, String speciality, String streetName, String houseNumber, String zipCode, String city, String province, String country, String phone, String supportedLangs, File file, Context context) {
        Integer result = -1;
        try {
            EditClinicParameters EditClinicParameters = new EditClinicParameters();
            EditClinicParameters.setUserId(clinicId);
            EditClinicParameters.setName(name);
            EditClinicParameters.setSpeciality(speciality);
            EditClinicParameters.setStreetName(streetName);
            EditClinicParameters.setHouseNumber(houseNumber);
            EditClinicParameters.setZipCode(zipCode);
            EditClinicParameters.setCity(city);
            EditClinicParameters.setProvince(province);
            EditClinicParameters.setCountry(country);
            EditClinicParameters.setPhone(phone);
            EditClinicParameters.setSupportedLangs(supportedLangs);

            String jsonString = "";
            if (file == null) {
                jsonString = post(API_CLINICS_EDIT, EditClinicParameters.toJson());
            } else {
                jsonString = postWithFile(API_CLINICS_EDIT, EditClinicParameters.toJson(), file, EditClinicParameters.PARAMETER_AVATAR);
            }
            Gson gson = new Gson();
            EditClinicResponse editClinicResponse = gson.fromJson(jsonString, EditClinicResponse.class);
            Toast.makeText(context, editClinicResponse.getMsg(), Toast.LENGTH_LONG).show();
            result = editClinicResponse.getData();

        } catch (Exception e) {
            Helper.handleError(TAG, "postEditClinic", e, -1, context);
        } finally {
            return result;
        }
    }

    // get all message in chat
    public static ArrayList<Message> getMessages(int UserID, int toID, Context context) {
        ArrayList<Message> result = null;
        try {
            MessagesParameter messagesParamater = new MessagesParameter();
            messagesParamater.setUserID(UserID);
            messagesParamater.setToID(toID);

            String jsonString = post(API_MESSAGES_LIST, messagesParamater.toJson());
            Gson gson = new Gson();
            MessagesResponse messageResponse = gson.fromJson(jsonString, MessagesResponse.class);
            if (messageResponse.getStatus()) {
                result = new ArrayList<>();
                result = messageResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "getMessages", e, -1, context);
        } finally {
            return result;
        }
    }

    // send message in chat
    public static Message sendMessage(Message msg, File media, Context context) {
        Message message = null;
        try {
            MessageSendParameters messageSendParamaters = new MessageSendParameters();
            messageSendParamaters.setFromID(msg.getFromID());
            messageSendParamaters.setToID(msg.getToID());
            messageSendParamaters.setMessage(msg.getMessage());
            messageSendParamaters.setTypeMessage(msg.getType());
            messageSendParamaters.setIsForward(0);
            String jsonString = "";
            if (media != null) {
                jsonString = postWithFile(API_MESSAGES_SEND, messageSendParamaters.toJson(), media, MessageSendParameters.PARAMATER_MEDIA);
            } else {
                jsonString = post(API_MESSAGES_SEND, messageSendParamaters.toJson());
            }
            Gson gson = new Gson();
            MessageSendResponse messageSendResponse = gson.fromJson(jsonString, MessageSendResponse.class);
            if (messageSendResponse.getStatus()) {
                message = messageSendResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "sendMessage", e, -1, context);
        } finally {
            return message;
        }
    }

    // get all speciality
    public static ArrayList<Speciality> getSpecialities(Context context) {
        ArrayList<Speciality> specialityArrayList = new ArrayList<>();
        try {
            String jsonString = post(API_SPECIALITIES_LIST, EMPTY_JSON);
            Gson gson = new Gson();
            SpecialityResponse specialityResponse = gson.fromJson(jsonString, SpecialityResponse.class);
            if (specialityResponse.getStatus()) {
                specialityArrayList = specialityResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "getSpecialities", e, -1, context);
        } finally {
            return specialityArrayList;
        }
    }

    // get all Language
    public static ArrayList<Language> getLanguage(Context context) {
        ArrayList<Language> languageArrayList = new ArrayList<>();
        try {
            String jsonString = post(API_LANGUAGES_LIST, EMPTY_JSON);
            Gson gson = new Gson();
            LanguageResponse languageResponse = gson.fromJson(jsonString, LanguageResponse.class);
            if (languageResponse.getStatus()) {
                languageArrayList = languageResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "getSpecialities", e, -1, context);
        } finally {
            return languageArrayList;
        }
    }

    public static ArrayList<UserInfo> postGetDoctorList(Context context) {
        ArrayList<UserInfo> result = new ArrayList<>();
        try {

            String jsonString = post(API_DOCTORS_LIST, EMPTY_JSON);
            Gson gson = new Gson();
            GetDoctorListResponse getDoctorListResponse = gson.fromJson(jsonString, GetDoctorListResponse.class);
            if (getDoctorListResponse.getStatus()) {
                result = getDoctorListResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "postGetDoctorList", e, -1, context);
        } finally {
            return result;
        }
    }

    public static int postChangeStatus(Integer userId, Integer isAvailable, Context context) {
        int result = -1;
        try {
            ChangeStatusParameters changeStatusParameters = new ChangeStatusParameters();
            changeStatusParameters.setUserId(userId);
            changeStatusParameters.setAvailable(isAvailable);

            String jsonString = "";

            jsonString = post(API_DOCTORS_CHANGE_STATUS, changeStatusParameters.toJson());

            Gson gson = new Gson();
            ChangeStatusResponse changeStatusResponse = gson.fromJson(jsonString, ChangeStatusResponse.class);
            Toast.makeText(context, changeStatusResponse.getMsg(), Toast.LENGTH_LONG).show();
            if (changeStatusResponse.getStatus()) {
                result = 1;
            }

        } catch (Exception e) {
            Helper.handleError(TAG, "postChangeStatus", e, -1, context);
        } finally {
            return result;
        }
    }

    public static Document postAddDocument(Integer userId, Document doc, File file, Context context) {
        Document result = null;
        try {
            AddDocumentParameters addDocumentParameters = new AddDocumentParameters();
            addDocumentParameters.setUserId(userId);
            addDocumentParameters.setDocument(doc.getDocument());
            addDocumentParameters.setType(doc.getType());

            String jsonString = "";
            if (file == null) {
                jsonString = post(API_DOCUMENTS_ADD, addDocumentParameters.toJson());
            } else {
                jsonString = postWithFile(API_DOCUMENTS_ADD, addDocumentParameters.toJson(), file, addDocumentParameters.PARAMETER_IMAGE);
            }

            Gson gson = new Gson();
            AddDocumentResponse addDocumentResponse = gson.fromJson(jsonString, AddDocumentResponse.class);
            if (addDocumentResponse.getStatus()) {
                result = addDocumentResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "postAddDocument", e, -1, context);
        } finally {
            return result;
        }
    }

    public static ArrayList<Document> postGetDocumentList(Integer userId, Context context) {
        ArrayList<Document> result = new ArrayList<>();
        GetDocumentListParameters getDocumentListParameters = new GetDocumentListParameters();
        getDocumentListParameters.setUserId(userId);
        try {

            String jsonString = post(API_DOCUMENTS_LIST, getDocumentListParameters.toJson());
            Gson gson = new Gson();
            GetDocumentListResponse getDocumentListResponse = gson.fromJson(jsonString, GetDocumentListResponse.class);
            if (getDocumentListResponse.getStatus()) {
                result = getDocumentListResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "postGetDocumentList", e, -1, context);
        } finally {
            return result;
        }
    }

    public static boolean postDocumentPrivacy(Integer documentId, Integer privacy, Context context) {
        boolean result = false;
        try {
            DocumentPrivacyParameters documentPrivacyParameters = new DocumentPrivacyParameters();
            documentPrivacyParameters.setDocumentId(documentId);
            documentPrivacyParameters.setPrivacy(privacy);

            String jsonString = "";

            jsonString = post(API_DOCUMENT_PRIVACY, documentPrivacyParameters.toJson());

            Gson gson = new Gson();
            DocumentPrivacyResponse documentPrivacyResponse = gson.fromJson(jsonString, DocumentPrivacyResponse.class);
            if (documentPrivacyResponse.getStatus()) {
                result = true;
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "postDocumentPrivacy", e, -1, context);
        } finally {
            return result;
        }
    }

    // add user return true if user registered
    public static boolean addUser(Context context, int userID, String password, String title, String firstName, String lastName, String birthday, String gender, File avatar) {
        boolean result = false;
        try {
            UserAddParameter userAddParamater = new UserAddParameter();
            userAddParamater.setUserID(userID);
            userAddParamater.setPassword(password);
            userAddParamater.setTitle(title);
            userAddParamater.setFirstName(firstName);
            userAddParamater.setLastName(lastName);
            userAddParamater.setBirthday(birthday);
            userAddParamater.setGender(gender);
            String jsonString;
            if (avatar != null) {
                jsonString = postWithFile(API_USERS_ADD, userAddParamater.toJson(), avatar, UserAddParameter.PARAMETER_AVATAR);
            } else {
                jsonString = post(API_USERS_ADD, userAddParamater.toJson());
            }
            Gson gson = new Gson();
            ParentResponse parentResponse = gson.fromJson(jsonString, ParentResponse.class);
            if (parentResponse.getStatus()) {
                result = true;
            }

        } catch (Exception e) {
            Helper.handleError(TAG, "addUser", e, -1, context);
        } finally {
            return result;
        }
    }

    // edit doctor return true if doctor edited
    public static boolean editDoctor(Context context, UserInfo userInfo, File avatar) {
        boolean result = false;
        try {
            EditDoctorParameter editDoctorParamater = new EditDoctorParameter();
            editDoctorParamater.setUserID(userInfo.getUserID());
            editDoctorParamater.setPassword(userInfo.getPassword());
            editDoctorParamater.setTitle(userInfo.getTitle());
            editDoctorParamater.setFirstName(userInfo.getFirstName());
            editDoctorParamater.setLastName(userInfo.getLastName());
            editDoctorParamater.setBirthday(userInfo.getBirthday());
            editDoctorParamater.setGender(userInfo.getGender());
            editDoctorParamater.setEmail(userInfo.getEmail());
            editDoctorParamater.setHouseNumber(userInfo.getHouseNumber());
            editDoctorParamater.setProvidence(userInfo.getProvidence());
            editDoctorParamater.setStreetName(userInfo.getStreetName());
            editDoctorParamater.setZipCode(userInfo.getZipCode());
            String jsonString;
            if (avatar != null) {
                jsonString = postWithFile(API_USERS_EDIT, editDoctorParamater.toJson(), avatar, UserAddParameter.PARAMETER_AVATAR);
            } else {
                jsonString = post(API_USERS_EDIT, editDoctorParamater.toJson());
            }
            Gson gson = new Gson();
            ParentResponse parentResponse = gson.fromJson(jsonString, ParentResponse.class);
            if (parentResponse.getStatus()) {
                result = true;
            }

        } catch (Exception e) {
            Helper.handleError(TAG, "editDoctor", e, -1, context);
        } finally {
            return result;
        }
    }

    // edit patient return true if doctor edited
    public static boolean editPatient(Context context, UserInfo userInfo, File avatar) {
        boolean result = false;
        try {
            EditPatientParameter editDoctorParamater = new EditPatientParameter();
            editDoctorParamater.setUserID(userInfo.getUserID());
            editDoctorParamater.setFirstName(userInfo.getFirstName());
            editDoctorParamater.setLastName(userInfo.getLastName());
            editDoctorParamater.setTitle(userInfo.getTitle());
            editDoctorParamater.setCountryCode(userInfo.getCountry_code());
            editDoctorParamater.setPhone(userInfo.getPhone());
            editDoctorParamater.setGender(userInfo.getGender());
            editDoctorParamater.setBirthday(userInfo.getBirthday());
            String jsonString;
            if (avatar != null) {
                jsonString = postWithFile(API_USERS_EDIT, editDoctorParamater.toJson(), avatar, UserAddParameter.PARAMETER_AVATAR);
            } else {
                jsonString = post(API_USERS_EDIT, editDoctorParamater.toJson());
            }
            Gson gson = new Gson();
            ParentResponse parentResponse = gson.fromJson(jsonString, ParentResponse.class);
            if (parentResponse.getStatus()) {
                result = true;
            }

        } catch (Exception e) {
            Helper.handleError(TAG, "editPatient", e, -1, context);
        } finally {
            return result;
        }
    }

    public static UserInfo getUserInfo(Context context, String userID) {
        UserInfo userInfo = null;
        try {
            UserInfoParameter userInfoParameter = new UserInfoParameter();
            userInfoParameter.setUserID(userID);
            System.out.println(userInfoParameter.toJson());

            String jsonString = post(API_USERS_ME, userInfoParameter.toJson());
            System.out.println(jsonString);
            Gson gson = new Gson();
            UserInfoResponse userInfoResponse = gson.fromJson(jsonString, UserInfoResponse.class);
            if (userInfoResponse.getStatus()) {
                userInfo = userInfoResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "getUserInfo", e, -1, context);
        } finally {
            return userInfo;
        }
    }

    public static int openSession(Context context, String userID, String doctorID) {
        int result = -1;
        try {
            OpenSessionParameters openSessionParameters = new OpenSessionParameters();
            openSessionParameters.setUserID(userID);
            openSessionParameters.setDoctorID(doctorID);
            String jsonString = post(API_REQUESTS_OPEN, openSessionParameters.toJson());
            Gson gson = new Gson();
            OpenSessionResponse parentResponse = gson.fromJson(jsonString, OpenSessionResponse.class);
            if (parentResponse.getStatus()) {
                result = Integer.parseInt(parentResponse.getData().get(OpenSessionResponse.KEY_REQUEST_ID));
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "openSession", e, -1, context);
        } finally {
            return result;
        }
    }

    public static boolean closeSession(Context context, int requestID) {
        boolean result = false;
        try {
            CloseSessionParameters closeSessionParameters = new CloseSessionParameters();
            closeSessionParameters.setRequestID(requestID);
            String jsonString = post(API_REQUESTS_CLOSE, closeSessionParameters.toJson());
            Gson gson = new Gson();
            ParentResponse parentResponse = gson.fromJson(jsonString, ParentResponse.class);
            if (parentResponse.getStatus()) {
                result = true;
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "closeSession", e, -1, context);
        } finally {
            return result;
        }
    }

    public static Message sendForward(Context context, int userID, String doctorID, String messageID) {
        Message result = null;
        try {
            MessageForwardParameter messageForwardParameter = new MessageForwardParameter();
            messageForwardParameter.setUserID(userID);
            messageForwardParameter.setToID(doctorID);
            messageForwardParameter.setMessagesID(messageID);
            String jsonString = post(API_MESSAGES_FORWARD, messageForwardParameter.toJson());
            Gson gson = new Gson();
            MessageSendResponse messageSendResponse = gson.fromJson(jsonString, MessageSendResponse.class);
            if (messageSendResponse.getStatus()) {
                result = messageSendResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "sendForward", e, -1, context);
        } finally {
            return result;
        }
    }

    private static boolean MessageOperation(Context context, int user_id, String msg_id, int operationType) {
        boolean result = false;
        try {
            MessageOperationParameter messageOperationParameter = new MessageForwardParameter();
            messageOperationParameter.setUserID(user_id);
            messageOperationParameter.setMessagesID(msg_id);
            String url;
            if (operationType == MessageOperationParameter.SEEN) {
                url = API_MESSAGES_SEEN;
            } else if (operationType == MessageOperationParameter.DELIVER) {
                url = API_MESSAGES_DELIVER;
            } else if (operationType == MessageOperationParameter.DELETE) {
                url = API_MESSAGES_DELETE;
            } else {
                return false;
            }
            String jsonString = post(url, messageOperationParameter.toJson());
            Gson gson = new Gson();
            ParentResponse parentResponse = gson.fromJson(jsonString, ParentResponse.class);
            if (parentResponse.getStatus()) {
                result = true;
            }

        } catch (Exception e) {
            Helper.handleError(TAG, "MessageOperation", e, -1, context);
        } finally {
            return result;
        }
    }

    public static boolean deleteMessgae(Context context, int user_id, String msg_id) {
        return MessageOperation(context, user_id, msg_id, MessageOperationParameter.DELETE);
    }

    public static boolean seenMessage(Context context, int user_id, String msg_id) {
        return MessageOperation(context, user_id, msg_id, MessageOperationParameter.SEEN);
    }

    public static boolean deliveredMessgae(Context context, int user_id, String msg_id) {
        return MessageOperation(context, user_id, msg_id, MessageOperationParameter.DELIVER);
    }

    private static ArrayList<ChatModel> getChatMessages(Context context, String userID, int chatType) {
        ArrayList<ChatModel> messageArrayList = new ArrayList<>();
        try {
            UserInfoParameter userInfoParameter = new UserInfoParameter();
            userInfoParameter.setUserID(userID);
            String url;
            if (chatType == UserInfoParameter.CHATUSER) {
                url = API_MESSAGES_CHAT_USER;
            } else if (chatType == UserInfoParameter.CHATCLINIC) {
                url = API_MESSAGES_CHAT_CLINIC;
            } else if (chatType == UserInfoParameter.CHATDOCTOR) {
                url = API_MESSAGES_CHAT_DOCTOR;
            } else if (chatType == UserInfoParameter.CHATANOTHER) {
                url = API_MESSAGES_CHAT_ANOTHER;
            } else {
                return messageArrayList;
            }
            String jsonString = post(url, userInfoParameter.toJson());
            Gson gson = new Gson();
            ChatResponse chatResponse = gson.fromJson(jsonString, ChatResponse.class);
            if (chatResponse.getStatus()) {
                messageArrayList = chatResponse.getChatModels();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "getChatMessages", e, -1, context);
        } finally {
            return messageArrayList;
        }
    }

    public static ArrayList<ChatModel> getChatDoctor(Context context, String userID) {
        return getChatMessages(context, userID, UserInfoParameter.CHATDOCTOR);
    }

    public static ArrayList<ChatModel> getChatClinic(Context context, String userID) {
        return getChatMessages(context, userID, UserInfoParameter.CHATCLINIC);
    }

    public static ArrayList<ChatModel> getChatUser(Context context, String userID) {
        return getChatMessages(context, userID, UserInfoParameter.CHATUSER);
    }

    public static ArrayList<ChatModel> getChatAnother(Context context, String userID) {
        return getChatMessages(context, userID, UserInfoParameter.CHATANOTHER);
    }

    // Add or Update Push Notification Token
    public static void addToken(Context context, String userId, String token) {
        try {
            TokenAddParameter parameter = new TokenAddParameter();
            parameter.setUserId(userId);
            parameter.setToken(token);
            String jsonString = post(API_SPECIALITIES_LIST, parameter.toJson());
            Gson gson = new Gson();
            ParentResponse response = gson.fromJson(jsonString, ParentResponse.class);
            Log.d(TAG, response.getMsg());
        } catch (Exception e) {
            Helper.handleError(TAG, "addToken", e, -1, context);
        }
    }

    public static RateDoctorResponse rateDoctor(Context context, String userId, String doctorId, String requestId, String comment, String rate) {
        RateDoctorResponse result = null;
        try {
            RateDoctorParameter rateDoctorParameter = new RateDoctorParameter();
            rateDoctorParameter.setUserId(userId);
            rateDoctorParameter.setDoctorId(doctorId);
            rateDoctorParameter.setRequestId(requestId);
            rateDoctorParameter.setComment(comment);
            rateDoctorParameter.setRate(rate);
            String jsonString = post(API_USERS_ME, rateDoctorParameter.toJson());
            Gson gson = new Gson();
            RateDoctorResponse rateDoctorResponse = gson.fromJson(jsonString, RateDoctorResponse.class);
            if (rateDoctorResponse.getStatus()) {
                result = rateDoctorResponse;
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "rateDoctor", e, -1, context);
        } finally {
            return result;
        }
    }

    public static Review getReview(String userId, Context context) {
        Review review = null;
        try {
            UserInfoParameter userInfoParameter = new UserInfoParameter();
            userInfoParameter.setUserID(userId);
            String jsonString = post(API_RATE_REVIEW, userInfoParameter.toJson());
            Gson gson = new Gson();
            ReviewResponse reviewResponse = gson.fromJson(jsonString, ReviewResponse.class);
            if (reviewResponse.getStatus()) {
                review = reviewResponse.getData();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "getReview", e, -1, context);

        } finally {
            return review;
        }
    }


    public static String getNetworkCountryCode() {
        String result = "";
        try {

            String jsonString = postWithoutServerPath(API_NETWORK_LOCATION, EMPTY_JSON);
            Gson gson = new Gson();
            NetworkLocationResponse networkLocationResponse = gson.fromJson(jsonString, NetworkLocationResponse.class);
            if (networkLocationResponse != null && !TextUtils.isEmpty(networkLocationResponse.getCountryCode())) {
                result = networkLocationResponse.getCountryCode();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "getNetworkCountryCode", e, -1, null);
        } finally {
            return result;
        }
    }

    //endregion
}
