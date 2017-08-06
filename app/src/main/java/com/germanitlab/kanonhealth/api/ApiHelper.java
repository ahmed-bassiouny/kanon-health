package com.germanitlab.kanonhealth.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.germanitlab.kanonhealth.api.models.Clinic;
import com.germanitlab.kanonhealth.api.models.Document;
import com.germanitlab.kanonhealth.api.models.Language;
import com.germanitlab.kanonhealth.api.models.Message;
import com.germanitlab.kanonhealth.api.models.Register;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.SupportedLang;
import com.germanitlab.kanonhealth.api.parameters.AddDocumentParameters;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.parameters.AddOrEditClinicParameters;
import com.germanitlab.kanonhealth.api.parameters.CloseSessionParameters;
import com.germanitlab.kanonhealth.api.parameters.ChangeStatusParameters;
import com.germanitlab.kanonhealth.api.parameters.DocumentPrivacyParameters;
import com.germanitlab.kanonhealth.api.parameters.EditDoctorParameter;
import com.germanitlab.kanonhealth.api.parameters.EditPatientParameter;
import com.germanitlab.kanonhealth.api.parameters.GetClinicParameters;
import com.germanitlab.kanonhealth.api.parameters.GetDocumentListParameters;
import com.germanitlab.kanonhealth.api.parameters.MessageForwardParameter;
import com.germanitlab.kanonhealth.api.parameters.MessageOperationParameter;
import com.germanitlab.kanonhealth.api.parameters.MessageSendParameters;
import com.germanitlab.kanonhealth.api.parameters.MessagesParameter;
import com.germanitlab.kanonhealth.api.parameters.OpenSessionParameters;
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
import com.germanitlab.kanonhealth.api.responses.ParentResponse;
import com.germanitlab.kanonhealth.api.responses.RegisterResponse;
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
    private static final String API_DOCTORS_CHANGE_RATE = "doctors/rate";

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


    private static final String API_TOKEN_REGISTER = "token/add";


    private static final String API_UPLOAD = "upload";

    //endregion

    //region Configuration

    public static final String SERVER_API_URL = "http://192.168.1.35:8000/V1/";
    public static final String SERVER_IMAGE_URL = "";

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

    public static Clinic postAddClinic(Integer userId, String name, String speciality, Float rateNum, HashMap<String, String> ratePercentage, String address, String streetName, String houseNumber, String zipCode, String city, String province, String country, String phone, String fax, ArrayList<SupportedLang> supportedLangs, File file, Context context) {
        Clinic result = null;
        try {
            AddOrEditClinicParameters addOrEditClinicParameters = new AddOrEditClinicParameters();
            addOrEditClinicParameters.setUserId(userId);
            addOrEditClinicParameters.setName(name);
            addOrEditClinicParameters.setSpeciality(speciality);
            addOrEditClinicParameters.setRateNum(rateNum);
            addOrEditClinicParameters.setRatePercentage(ratePercentage);
            addOrEditClinicParameters.setAddress(address);
            addOrEditClinicParameters.setStreetName(streetName);
            addOrEditClinicParameters.setHouseNumber(houseNumber);
            addOrEditClinicParameters.setZipCode(zipCode);
            addOrEditClinicParameters.setCity(city);
            addOrEditClinicParameters.setProvince(province);
            addOrEditClinicParameters.setCountry(country);
            addOrEditClinicParameters.setPhone(phone);
            addOrEditClinicParameters.setFax(fax);
            addOrEditClinicParameters.setSupportedLangs(supportedLangs);
            String jsonString = "";
            if (file == null) {
                jsonString = post(API_CLINICS_ADD, addOrEditClinicParameters.toJson());
            } else {
                jsonString = postWithFile(API_CLINICS_ADD, addOrEditClinicParameters.toJson(), file, addOrEditClinicParameters.PARAMETER_AVATAR);
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

    public static Integer postEditClinic(Integer userId, String name, String speciality, Float rateNum, HashMap<String, String> ratePercentage, String address, String streetName, String houseNumber, String zipCode, String city, String province, String country, String phone, String fax, ArrayList<SupportedLang> supportedLangs, File file, Context context) {
        Integer result = -1;
        try {
            AddOrEditClinicParameters addOrEditClinicParameters = new AddOrEditClinicParameters();
            addOrEditClinicParameters.setUserId(userId);
            addOrEditClinicParameters.setName(name);
            addOrEditClinicParameters.setSpeciality(speciality);
            addOrEditClinicParameters.setRateNum(rateNum);
            addOrEditClinicParameters.setRatePercentage(ratePercentage);
            addOrEditClinicParameters.setAddress(address);
            addOrEditClinicParameters.setStreetName(streetName);
            addOrEditClinicParameters.setHouseNumber(houseNumber);
            addOrEditClinicParameters.setZipCode(zipCode);
            addOrEditClinicParameters.setCity(city);
            addOrEditClinicParameters.setProvince(province);
            addOrEditClinicParameters.setCountry(country);
            addOrEditClinicParameters.setPhone(phone);
            addOrEditClinicParameters.setFax(fax);
            addOrEditClinicParameters.setSupportedLangs(supportedLangs);

            String jsonString = "";
            if (file == null) {
                jsonString = post(API_CLINICS_EDIT, addOrEditClinicParameters.toJson());
            } else {
                jsonString = postWithFile(API_CLINICS_EDIT, addOrEditClinicParameters.toJson(), file, addOrEditClinicParameters.PARAMETER_AVATAR);
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
    public static Message sendMessage(int UserID, int toID, String textMessage, String type, File media, Context context) {
        Message message = null;
        try {
            MessageSendParameters messageSendParamaters = new MessageSendParameters();
            messageSendParamaters.setFromID(UserID);
            messageSendParamaters.setToID(toID);
            messageSendParamaters.setMessage(textMessage);
            messageSendParamaters.setTypeMessage(type);
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
        ArrayList<Speciality> specialityArrayList = null;
        try {
            String jsonString = post(API_SPECIALITIES_LIST, EMPTY_JSON);
            Gson gson = new Gson();
            SpecialityResponse specialityResponse = gson.fromJson(jsonString, SpecialityResponse.class);
            if (specialityResponse.getStatus()) {
                specialityArrayList = new ArrayList<>();
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
        ArrayList<Language> languageArrayList = null;
        try {
            String jsonString = post(API_LANGUAGES_LIST, EMPTY_JSON);
            Gson gson = new Gson();
            LanguageResponse languageResponse = gson.fromJson(jsonString, LanguageResponse.class);
            if (languageResponse.getStatus()) {
                languageArrayList = new ArrayList<>();
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

    public static Document postAddDocument(Integer userId, String type, String document, File file, Context context) {
        Document result = null;
        try {
            AddDocumentParameters addDocumentParameters = new AddDocumentParameters();
            addDocumentParameters.setUserId(userId);
            addDocumentParameters.setDocument(document);
            addDocumentParameters.setType(type);

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

    public static void postDocumentPrivacy(Integer documentId, Integer privacy, Context context) {

        try {
            DocumentPrivacyParameters documentPrivacyParameters = new DocumentPrivacyParameters();
            documentPrivacyParameters.setDocumentId(documentId);
            documentPrivacyParameters.setPrivacy(privacy);

            String jsonString = "";

            jsonString = post(API_DOCUMENT_PRIVACY, documentPrivacyParameters.toJson());

            Gson gson = new Gson();
            DocumentPrivacyResponse documentPrivacyResponse = gson.fromJson(jsonString, DocumentPrivacyResponse.class);
            Toast.makeText(context, documentPrivacyResponse.getMsg(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Helper.handleError(TAG, "postDocumentPrivacy", e, -1, context);
        } finally {

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
    public static boolean editDoctor(Context context, int userID, String password, String title, String firstName, String lastName, String birthday, String gender, File avatar, String email, String address) {
        boolean result = false;
        try {
            EditDoctorParameter editDoctorParamater = new EditDoctorParameter();
            editDoctorParamater.setUserID(userID);
            editDoctorParamater.setPassword(password);
            editDoctorParamater.setTitle(title);
            editDoctorParamater.setFirstName(firstName);
            editDoctorParamater.setLastName(lastName);
            editDoctorParamater.setBirthday(birthday);
            editDoctorParamater.setGender(gender);
            editDoctorParamater.setAddress(address);
            editDoctorParamater.setEmail(email);
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
    public static boolean editPatient(Context context, int userID, String title, String firstName, String lastName, String birthday, String gender, File avatar, String countryCode, String phone) {
        boolean result = false;
        try {
            EditPatientParameter editDoctorParamater = new EditPatientParameter();
            editDoctorParamater.setUserID(userID);
            editDoctorParamater.setFirstName(firstName);
            editDoctorParamater.setLastName(lastName);
            editDoctorParamater.setTitle(title);
            editDoctorParamater.setCountryCode(countryCode);
            editDoctorParamater.setPhone(phone);
            editDoctorParamater.setGender(gender);
            editDoctorParamater.setBirthday(birthday);
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

    public static UserInfo getUserInfo(Context context, int userID) {
        UserInfo userInfo = null;
        try {
            UserInfoParameter userInfoParameter = new UserInfoParameter();
            userInfoParameter.setUserID(userID);
            String jsonString = post(API_USERS_ME, userInfoParameter.toJson());
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

    public static boolean openSession(Context context, int userID, int doctorID) {
        boolean result = false;
        try {
            OpenSessionParameters openSessionParameters = new OpenSessionParameters();
            openSessionParameters.setUserID(userID);
            openSessionParameters.setDoctorID(doctorID);
            String jsonString = post(API_REQUESTS_OPEN, openSessionParameters.toJson());
            Gson gson = new Gson();
            ParentResponse parentResponse = gson.fromJson(jsonString, ParentResponse.class);
            if (parentResponse.getStatus()) {
                result = true;
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

    public static Message sendForward(Context context, String userID, String doctorID, String messageID) {
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

    private static boolean MessageOperation(Context context, String user_id, String msg_id, int operationType) {
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

    public static boolean deleteMessgae(Context context, String user_id, String msg_id) {
        return MessageOperation(context, user_id, msg_id, MessageOperationParameter.DELETE);
    }

    public static boolean seenMessgae(Context context, String user_id, String msg_id) {
        return MessageOperation(context, user_id, msg_id, MessageOperationParameter.SEEN);
    }

    public static boolean deliveredMessgae(Context context, String user_id, String msg_id) {
        return MessageOperation(context, user_id, msg_id, MessageOperationParameter.DELIVER);
    }

    private static ArrayList<ChatModel> getChatMessages(Context context, int userID, int chatType) {
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

    public static ArrayList<ChatModel> getChatDoctor(Context context, int userID) {
        return getChatMessages(context, userID, UserInfoParameter.CHATDOCTOR);
    }

    public static ArrayList<ChatModel> getChatClinic(Context context, int userID) {
        return getChatMessages(context, userID, UserInfoParameter.CHATCLINIC);
    }

    public static ArrayList<ChatModel> getChatUser(Context context, int userID) {
        return getChatMessages(context, userID, UserInfoParameter.CHATUSER);
    }

    public static ArrayList<ChatModel> getChatAnother(Context context, int userID) {
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
    //endregion
}
