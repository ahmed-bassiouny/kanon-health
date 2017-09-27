package com.germanitlab.kanonhealth.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.germanitlab.kanonhealth.api.models.ClinicEdit;
import com.germanitlab.kanonhealth.api.models.Document;
import com.germanitlab.kanonhealth.api.models.Language;
import com.germanitlab.kanonhealth.api.models.Message;
import com.germanitlab.kanonhealth.api.models.Register;
import com.germanitlab.kanonhealth.api.models.Review;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.WorkingHours;
import com.germanitlab.kanonhealth.api.parameters.AddClinicParameters;
import com.germanitlab.kanonhealth.api.parameters.AddDocumentParameters;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.parameters.ClinicWorkingHoursParameters;
import com.germanitlab.kanonhealth.api.parameters.CloseSessionParameters;
import com.germanitlab.kanonhealth.api.parameters.ChangeStatusParameters;
import com.germanitlab.kanonhealth.api.parameters.DoctorWorkingHoursParameters;
import com.germanitlab.kanonhealth.api.parameters.DocumentPrivacyParameters;
import com.germanitlab.kanonhealth.api.parameters.EditClinicParameters;
import com.germanitlab.kanonhealth.api.parameters.EditDoctorParameter;
import com.germanitlab.kanonhealth.api.parameters.EditPatientParameter;
import com.germanitlab.kanonhealth.api.parameters.FavouriteParameters;
import com.germanitlab.kanonhealth.api.parameters.ForwardParameter;
import com.germanitlab.kanonhealth.api.parameters.GetClinicParameters;
import com.germanitlab.kanonhealth.api.parameters.GetDocumentListParameters;
import com.germanitlab.kanonhealth.api.parameters.HaveRateParameters;
import com.germanitlab.kanonhealth.api.parameters.IsOpenParameters;
import com.germanitlab.kanonhealth.api.parameters.MessageOperationParameter;
import com.germanitlab.kanonhealth.api.parameters.MessageSendParameters;
import com.germanitlab.kanonhealth.api.parameters.MessagesParameter;
import com.germanitlab.kanonhealth.api.parameters.OpenSessionParameters;
import com.germanitlab.kanonhealth.api.parameters.RateDoctorParameter;
import com.germanitlab.kanonhealth.api.parameters.RateReviewsParameter;
import com.germanitlab.kanonhealth.api.parameters.RegisterParameters;
import com.germanitlab.kanonhealth.api.parameters.TokenAddParameter;
import com.germanitlab.kanonhealth.api.parameters.UserAddParameter;
import com.germanitlab.kanonhealth.api.parameters.UserInfoParameter;
import com.germanitlab.kanonhealth.api.responses.AddClinicResponse;
import com.germanitlab.kanonhealth.api.responses.AddDocumentResponse;
import com.germanitlab.kanonhealth.api.responses.ChangeStatusResponse;
import com.germanitlab.kanonhealth.api.responses.ChatResponse;
import com.germanitlab.kanonhealth.api.responses.EditClinicResponse;
import com.germanitlab.kanonhealth.api.responses.GetClinicListResponse;
import com.germanitlab.kanonhealth.api.responses.GetClinicResponse;
import com.germanitlab.kanonhealth.api.responses.GetDoctorListResponse;
import com.germanitlab.kanonhealth.api.responses.GetDocumentListResponse;
import com.germanitlab.kanonhealth.api.responses.HaveRateResponse;
import com.germanitlab.kanonhealth.api.responses.IsOpenResponse;
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
import java.util.concurrent.TimeUnit;

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
    private static final String API_CLINICS_RATE = "clinics/rate";
    private static final String API_DOCUMENTS_ADD = "document/add";
    private static final String API_DOCUMENTS_LIST = "document/list";
    private static final String API_DOCUMENT_PRIVACY = "document/privacy";
    private static final String API_LANGUAGES_LIST = "langs/list";
    /***************  Message with Doctor or user   ******************/
    //region
    private static final String API_MESSAGES_SEND_DOC = "messages/send";
    private static final String API_MESSAGES_FORWARD_DOC = "messages/forward";
    private static final String API_MESSAGES_DELETE_DOC = "messages/delete";
    private static final String API_MESSAGES_DELIVER_DOC = "messages/deliver";
    private static final String API_MESSAGES_SEEN_DOC = "messages/seen";
    private static final String API_MESSAGES_LIST_DOC = "messages/messages";
    //endregion
    /***************  End Message with Doctor or user   ******************/
    /***************  Message with Clinic   ******************/
    // region
    private static final String API_MESSAGES_SEND_CLINIC = "messages-clinic/send";
    private static final String API_MESSAGES_FORWARD_CLINIC = "messages-clinic/forward";
    private static final String API_MESSAGES_DELETE_CLINIC = "messages-clinic/delete";
    private static final String API_MESSAGES_DELIVER_CLINIC = "messages-clinic/deliver";
    private static final String API_MESSAGES_SEEN_CLINIC = "messages-clinic/seen";
    private static final String API_MESSAGES_LIST_CLINIC = "messages-clinic/messages";
    // endregion
    /***************  end Message with Clinic   ******************/
    private static final String API_MESSAGES_CHAT_ANOTHER = "messages/chat/another";
    private static final String API_MESSAGES_CHAT_CLINIC = "messages/chat/clinic";
    private static final String API_MESSAGES_CHAT_DOCTOR = "messages/chat/doctor";
    private static final String API_MESSAGES_CHAT_USER = "messages/chat/users";
    private static final String API_MESSAGES_CHAT_ALL = "messages/chat/all";

    /***************  Request with Doctor   ******************/
    // region
    private static final String API_REQUESTS_OPEN_DOC = "requests/open";
    private static final String API_REQUESTS_CLOSE_DOC = "requests/close";
    // endregion
    /***************  end Request with Doctor******************/

    /***************  Request with Clinic   ******************/
    // region
    private static final String API_REQUESTS_OPEN_CLINIC = "requests-clinic/open";
    private static final String API_REQUESTS_CLOSE_CLINIC = "requests-clinic/close";
    // endregion
    /***************  end Request with Doctor******************/


    private static final String API_SPECIALITIES_LIST = "speciality/list";
    private static final String API_USERS_ME = "users/me";
    private static final String API_USERS_ADD = "users/add";
    private static final String API_USERS_EDIT = "users/edit";
    private static final String API_USERS_REGISTER = "users/register";
    private static final String API_RATE_REVIEW = "users/rate_reviews";
    private static final String API_FAVOURITE = "favourite";
    private static final String API_TOKEN_REGISTER = "token/add";
    private static final String API_UPLOAD = "upload";
    private static final String API_IS_OPEN_DOC = "flag/is_open";
    private static final String API_FLAG_HAVE_RATE = "flag/have_rate";
    private static final String API_FLAG_HAVE_RATE_CLINIC = "flag/have_rate_clinics";
    private static final String API_IS_OPEN_CLINIC = "flag/is_open_clinics";
    private static final String API_DOCTOR_WORKING_HOURS = "users/doctor_working_hours";
    private static final String API_CLINIC_WORKING_HOURS = "users/clinic_working_hours";
    private static final String API_NETWORK_LOCATION = "http://ip-api.com/json";

    //endregion

    //region Configuration

//    public static final String SERVER_API_URL = "http://api.gagabay.com/V1/";
//    public static final String SERVER_IMAGE_UPLOADS = "http://api.gagabay.com/";

    public static final String SERVER_API_URL = "http://cleverclan.com/kanon/V1/";
    public static final String SERVER_IMAGE_URL = "http://cleverclan.com/kanon/public/uploads/";
    public static final String SERVER_IMAGE_SPECIAL_URL = "http://cleverclan.com/kanon/";
    private static final String TAG = "ApiHelper";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String EMPTY_JSON = "{}";


    private static OkHttpClient getClient() {
        // set time out connection 20SEC
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    private static String post(String url, String parameters) throws IOException {

        RequestBody body = RequestBody.create(JSON, parameters);
        Request request = new Request.Builder()
                .url(SERVER_API_URL + url)
                .post(body)
                .build();
        Log.i(TAG, request.toString());
        Response response = getClient().newCall(request).execute();
        return response.body().string();
        //ApiUtils.removeNulls(response.body().string());
    }

    private static String postWithoutServerPath(String url, String parameters) throws IOException {
        RequestBody body = RequestBody.create(JSON, parameters);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Log.i(TAG, request.toString());
        Response response = getClient().newCall(request).execute();
        return response.body().string();
        //ApiUtils.removeNulls(response.body().string());
    }


    private static String postWithFile(String url, HashMap<String, Object> parameters, File file, String fileParameterName) throws IOException {
        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileParameterName, file.getName(), RequestBody.create(MediaType.parse(MimeUtils.getType(file.getName())), file));

        for (String s : parameters.keySet()) {
            requestBody.addFormDataPart(s, parameters.get(s).toString());
        }


        Request request = new Request.Builder()
                .url(SERVER_API_URL + url)
                .post(requestBody.build())
                .build();
        Response response = getClient().newCall(request).execute();
        return response.body().string();
        //ApiUtils.removeNulls(response.body().string());
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

    public static ArrayList<UserInfo> postGetClinicList(Context context) {
        ArrayList<UserInfo> result = new ArrayList<>();
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

    public static ClinicEdit postAddClinic(Integer userId, String name, String speciality, String streetName, String houseNumber, String zipCode, String city, String province, String country, String phone, String memberDoctors, String openType, String languages, File file, WorkingHours timeTable, Context context, String locationLat, String locationLong) {
        ClinicEdit result = null;
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
            addClinicParameters.setMemberDoctors(memberDoctors);
            addClinicParameters.setOpenType(openType);
            addClinicParameters.setSupportedLangs(languages);
            addClinicParameters.setTimeTable(timeTable);
            addClinicParameters.setLocationLat(locationLat);
            addClinicParameters.setLocationLong(locationLong);
            String jsonString = "";
            if (file == null) {
                jsonString = post(API_CLINICS_ADD, addClinicParameters.toJson());
            } else {
                jsonString = postWithFile(API_CLINICS_ADD, addClinicParameters.toHashMap(), file, addClinicParameters.PARAMETER_AVATAR);
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

    public static UserInfo postGetClinic(Integer clinicId, Context context) {
        UserInfo result = null;
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

    public static ClinicEdit postEditClinic(Integer clinicId, String name, String speciality, String streetName, String houseNumber, String zipCode, String city, String province, String country, String phone, String memberDoctors, String openType, String languages, File file, Context context, String avatar, WorkingHours timeTable, String locationLat, String locationLong) {
        ClinicEdit result = null;
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
            EditClinicParameters.setMemberDoctors(memberDoctors);
            EditClinicParameters.setOpenType(openType);
            EditClinicParameters.setSupportedLangs(languages);
            EditClinicParameters.setTimeTable(timeTable);
            EditClinicParameters.setLocationLong(locationLong);
            EditClinicParameters.setLocationLat(locationLat);

            String jsonString = "";
            if (file == null) {
                EditClinicParameters.setAvatar(avatar);
                jsonString = post(API_CLINICS_EDIT, EditClinicParameters.toJson());
            } else {
                jsonString = postWithFile(API_CLINICS_EDIT, EditClinicParameters.toHashMap(), file, EditClinicParameters.PARAMETER_AVATAR);
            }
            System.out.println("edit clinic");
            System.out.println(EditClinicParameters.toJson());
            System.out.println(jsonString);
            Gson gson = new Gson();
            EditClinicResponse editClinicResponse = gson.fromJson(jsonString, EditClinicResponse.class);

            if (editClinicResponse.getStatus()) {
                result = editClinicResponse.getData();
            }

        } catch (Exception e) {
            Helper.handleError(TAG, "postEditClinic", e, -1, context);
        } finally {
            return result;
        }
    }

    // get all message in chat
    public static ArrayList<Message> getMessages(int UserID, int toID, Context context, int userType) {
        ArrayList<Message> result = null;
        try {
            MessagesParameter messagesParamater = new MessagesParameter();
            messagesParamater.setUserID(UserID);
            messagesParamater.setToID(toID);
            String jsonString;
            System.out.println(messagesParamater.toJson());
            if (userType == UserInfo.CLINIC) {
                jsonString = post(API_MESSAGES_LIST_CLINIC, messagesParamater.toJson());
                System.out.println(messagesParamater.toJson());
            } else {
                jsonString = post(API_MESSAGES_LIST_DOC, messagesParamater.toJson());
            }
            System.out.println("jjjj+" + jsonString);
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
    public static Message sendMessage(Message msg, File media, Context context, int userType) {
        Message message = null;
        try {
            MessageSendParameters messageSendParamaters = new MessageSendParameters();
            messageSendParamaters.setFromID(msg.getFromID());
            messageSendParamaters.setToID(msg.getToID());
            messageSendParamaters.setMessage(msg.getMessage());
            messageSendParamaters.setTypeMessage(msg.getType());
            messageSendParamaters.setIsForward(0);
            String jsonString = "";
            if (userType == UserInfo.CLINIC) {
                if (media != null) {
                    jsonString = postWithFile(API_MESSAGES_SEND_CLINIC, messageSendParamaters.toHashMap(), media, MessageSendParameters.PARAMATER_MEDIA);
                } else {
                    jsonString = post(API_MESSAGES_SEND_CLINIC, messageSendParamaters.toJson());
                }
            } else {
                if (media != null) {
                    jsonString = postWithFile(API_MESSAGES_SEND_DOC, messageSendParamaters.toHashMap(), media, MessageSendParameters.PARAMATER_MEDIA);
                } else {
                    jsonString = post(API_MESSAGES_SEND_DOC, messageSendParamaters.toJson());
                }
            }
            System.out.println(messageSendParamaters.toJson());
            System.out.println(jsonString);
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

    public static ArrayList<UserInfo> postGetDoctorList(Context context, String userId) {
        ArrayList<UserInfo> result = new ArrayList<>();
        try {

            UserInfoParameter userInfoParameter = new UserInfoParameter();
            userInfoParameter.setUserID(userId);
            String jsonString = post(API_DOCTORS_LIST, userInfoParameter.toJson());
            Gson gson = new Gson();
            GetDoctorListResponse getDoctorListResponse = gson.fromJson(jsonString, GetDoctorListResponse.class);
            System.out.println("liiiist");
            System.out.println(jsonString);
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
            Log.i("jsson:", jsonString);

            Gson gson = new Gson();
            ChangeStatusResponse changeStatusResponse = gson.fromJson(jsonString, ChangeStatusResponse.class);
            // Toast.makeText(context, changeStatusResponse.getMsg(), Toast.LENGTH_LONG).show();
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
                jsonString = postWithFile(API_DOCUMENTS_ADD, addDocumentParameters.toHashMap(), file, addDocumentParameters.PARAMETER_IMAGE);
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
            ParentResponse parentResponse = gson.fromJson(jsonString, ParentResponse.class);
            if (parentResponse.getStatus()) {
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
            Log.i("UserIdValue:", userID + "");
            userAddParamater.setPassword(password);
            userAddParamater.setTitle(title);
            userAddParamater.setFirstName(firstName);
            userAddParamater.setLastName(lastName);
            userAddParamater.setBirthday(birthday);
            userAddParamater.setGender(gender);
            String jsonString;
            if (avatar != null) {
                jsonString = postWithFile(API_USERS_ADD, userAddParamater.toHashMap(), avatar, UserAddParameter.PARAMETER_AVATAR);
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
    public static boolean editDoctor(Context context, UserInfo userInfo, File avatar, String langIds, String specialityIds) {
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
            editDoctorParamater.setCountryCode(userInfo.getCountry_code());
            editDoctorParamater.setSpeciality(specialityIds);
            editDoctorParamater.setLangSub(langIds);

            System.out.println(editDoctorParamater.toHashMap());
            String jsonString;
            if (avatar != null) {
                editDoctorParamater.setAvatar(userInfo.getAvatar());
                jsonString = postWithFile(API_USERS_EDIT, editDoctorParamater.toHashMap(), avatar, UserAddParameter.PARAMETER_AVATAR);
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
            editDoctorParamater.setGender(userInfo.getGender());
            editDoctorParamater.setBirthday(userInfo.getBirthday());
            editDoctorParamater.setProvidence(userInfo.getProvidence());
            editDoctorParamater.setStreetName(userInfo.getStreetName());
            editDoctorParamater.setHouseNumber(userInfo.getHouseNumber());
            editDoctorParamater.setZipCode(userInfo.getZipCode());
            editDoctorParamater.setWeight_value(userInfo.getWeight_value());
            editDoctorParamater.setWeight_unit(userInfo.getWeight_unit());
            editDoctorParamater.setHeight_value(userInfo.getHeight_value());
            editDoctorParamater.setHeight_unit(userInfo.getHeight_unit());
            editDoctorParamater.setBlood(userInfo.getBlood_type());
            editDoctorParamater.setAllergies(userInfo.getKeyAllergies());
            editDoctorParamater.setAnamnese(userInfo.getKeyAnamnese());
            editDoctorParamater.setDiagnosis(userInfo.getKeyDiagnosis());
            editDoctorParamater.setDruges(userInfo.getKeyDrugs());
            editDoctorParamater.setMedical(userInfo.getKeyMedicalFindingsValue());
            editDoctorParamater.setProcedure(userInfo.getKeyProcedureValue());


            String jsonString;
            if (avatar != null) {
                jsonString = postWithFile(API_USERS_EDIT, editDoctorParamater.toHashMap(), avatar, UserAddParameter.PARAMETER_AVATAR);

            } else {
                editDoctorParamater.setAvatar(userInfo.getAvatar());
                jsonString = post(API_USERS_EDIT, editDoctorParamater.toJson());
                System.out.println(editDoctorParamater.toJson());
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
            String jsonString = post(API_USERS_ME, userInfoParameter.toJson());
            Gson gson = new Gson();
            System.out.println(jsonString);
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

    public static int openSession(Context context, String userID, String doctorID, HashMap<String, String> questionsAnswers, int userType) {
        int result = -1;
        try {
            OpenSessionParameters openSessionParameters = new OpenSessionParameters();
            openSessionParameters.setUserID(userID);
            openSessionParameters.setQuestionsAnswers(questionsAnswers);
            String jsonString;
            if (userType == UserInfo.CLINIC) {
                openSessionParameters.setClinicID(doctorID);
                jsonString = post(API_REQUESTS_OPEN_CLINIC, openSessionParameters.toJson());
            } else {
                openSessionParameters.setDoctorID(doctorID);
                jsonString = post(API_REQUESTS_OPEN_DOC, openSessionParameters.toJson());
            }
            System.out.println(openSessionParameters.toJson());
            System.out.println(jsonString);
            Gson gson = new Gson();
            OpenSessionResponse parentResponse = gson.fromJson(jsonString, OpenSessionResponse.class);
            if (parentResponse.getStatus()) {
                result = parentResponse.getData().getRequestId();
            }
        } catch (Exception e) {
            Helper.handleError(TAG, "openSession", e, -1, context);
        } finally {
            return result;
        }
    }

    public static boolean closeSession(Context context, int requestID, int userType) {
        boolean result = false;
        try {
            CloseSessionParameters closeSessionParameters = new CloseSessionParameters();
            closeSessionParameters.setRequestID(requestID);
            String jsonString;
            if (userType == UserInfo.CLINIC) {
                jsonString = post(API_REQUESTS_CLOSE_CLINIC, closeSessionParameters.toJson());
            } else {
                jsonString = post(API_REQUESTS_CLOSE_DOC, closeSessionParameters.toJson());
            }

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

    public static boolean sendForward(Context context, int userID, HashMap<String, Integer> doctorID, HashMap<String, Integer> messageID) {
        boolean result = false;
        try {
            ForwardParameter forwardParameter = new ForwardParameter();
            forwardParameter.setUserID(userID);
            forwardParameter.setMessagesID(messageID);
            forwardParameter.setToID(doctorID);

            String jsonString;
            jsonString = post(API_MESSAGES_FORWARD_DOC, forwardParameter.toJson());

            Gson gson = new Gson();
            ParentResponse parentResponse = gson.fromJson(jsonString, ParentResponse.class);
            result = parentResponse.getStatus();
        } catch (Exception e) {
            Helper.handleError(TAG, "sendForward", e, -1, context);
        } finally {
            return result;
        }
    }

    private static boolean MessageOperation(Context context, int user_id, String msg_id, String toId, int operationType, int userType) {
        boolean result = false;
        try {
            MessageOperationParameter messageOperationParameter = new MessageOperationParameter();
            messageOperationParameter.setUserID(user_id);
            messageOperationParameter.setMessagesID(msg_id);
            messageOperationParameter.setToID(toId);
            String url;
            if (userType == UserInfo.CLINIC) {
                if (operationType == MessageOperationParameter.SEEN) {
                    url = API_MESSAGES_SEEN_CLINIC;
                } else if (operationType == MessageOperationParameter.DELIVER) {
                    url = API_MESSAGES_DELIVER_CLINIC;
                } else if (operationType == MessageOperationParameter.DELETE) {
                    url = API_MESSAGES_DELETE_CLINIC;
                } else {
                    return false;
                }
            } else {
                if (operationType == MessageOperationParameter.SEEN) {
                    url = API_MESSAGES_SEEN_DOC;
                } else if (operationType == MessageOperationParameter.DELIVER) {
                    url = API_MESSAGES_DELIVER_DOC;
                } else if (operationType == MessageOperationParameter.DELETE) {
                    url = API_MESSAGES_DELETE_DOC;
                } else {
                    return false;
                }
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

    public static boolean deleteMessgae(Context context, int user_id, String toId, String msg_id, int userType) {
        return MessageOperation(context, user_id, msg_id, toId, MessageOperationParameter.DELETE, userType);
    }

    public static boolean seenMessage(Context context, int user_id, String toId, String msg_id, int userType) {
        return MessageOperation(context, user_id, msg_id, toId, MessageOperationParameter.SEEN, userType);
    }

    public static boolean deliveredMessgae(Context context, int user_id, String toId, String msg_id, int userType) {
        return MessageOperation(context, user_id, msg_id, toId, MessageOperationParameter.DELIVER, userType);
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
            } else if (chatType == UserInfoParameter.CHATALL) {
                url = API_MESSAGES_CHAT_ALL;
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

    public static ArrayList<ChatModel> getChatAll(Context context, String userID) {
        return getChatMessages(context, userID, UserInfoParameter.CHATALL);
    }

    // Add or Update Push MyNotification Token
    public static void addToken(Context context, String userId, String token) {
        try {
            TokenAddParameter parameter = new TokenAddParameter();
            parameter.setUserId(userId);
            parameter.setToken(token);
            String jsonString = post(API_TOKEN_REGISTER, parameter.toJson());
            Gson gson = new Gson();
            ParentResponse response = gson.fromJson(jsonString, ParentResponse.class);
            Log.d(TAG, response.getMsg());
        } catch (Exception e) {
            Helper.handleError(TAG, "addToken", e, -1, context);
        }
    }

    public static RateDoctorResponse rateDoctor(Context context, String userId, String id, String requestId, String comment, String rate, int type) {
        RateDoctorResponse result = null;
        try {
            RateDoctorParameter rateDoctorParameter = new RateDoctorParameter();
            rateDoctorParameter.setUserId(userId);
            rateDoctorParameter.setRequestId(requestId);
            rateDoctorParameter.setComment(comment);
            rateDoctorParameter.setRate(rate);
            String jsonString = "";
            if (type == UserInfo.DOCTOR) {
                rateDoctorParameter.setDoctorId(id);
                jsonString = post(API_DOCTORS_RATE, rateDoctorParameter.toJson());
            } else {
                rateDoctorParameter.setClinicId(id);
                jsonString = post(API_CLINICS_RATE, rateDoctorParameter.toJson());
            }

            System.out.println(rateDoctorParameter.toJson());
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


    public static Review getReview(String id, Context context, int type) {
        Review review = null;
        try {
            RateReviewsParameter rateReviewsParameter = new RateReviewsParameter();
            if (type == UserInfo.DOCTOR) {
                rateReviewsParameter.setDoctorID(id);
            } else {
                rateReviewsParameter.setClinicID(id);
            }

            String jsonString = post(API_RATE_REVIEW, rateReviewsParameter.toJson());
            System.out.println(rateReviewsParameter.toJson());
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

    public static boolean setFavouriteOperation(String fromID, String toID, int toType, boolean type) {
        boolean result = false;
        try {
            FavouriteParameters favouriteParameters = new FavouriteParameters();
            favouriteParameters.setUserId(fromID);
            favouriteParameters.setToId(toID);
            favouriteParameters.setToType(toType);
            favouriteParameters.setType(type);
            String jsonString = post(API_FAVOURITE, favouriteParameters.toJson());
            Gson gson = new Gson();
            ParentResponse parentResponse = gson.fromJson(jsonString, ParentResponse.class);
            result = parentResponse.getStatus();
        } catch (Exception e) {
            Helper.handleError(TAG, "setFavouriteOperation", e, -1, null);
        } finally {
            return result;
        }
    }


    public static boolean ClinicWorkingHours(WorkingHours workingHours, String openType, String clinicId) {
        boolean result = false;
        try {
            ClinicWorkingHoursParameters clinicWorkingHoursParameters = new ClinicWorkingHoursParameters();
            clinicWorkingHoursParameters.setUserId(clinicId);
            clinicWorkingHoursParameters.setOpenType(openType);
            clinicWorkingHoursParameters.setSaturday((new Gson()).toJson(workingHours.getSaturday()));
            clinicWorkingHoursParameters.setSunday((new Gson()).toJson(workingHours.getSunday()));
            clinicWorkingHoursParameters.setMonday((new Gson()).toJson(workingHours.getMonday()));
            clinicWorkingHoursParameters.setTuesday((new Gson()).toJson(workingHours.getTuesday()));
            clinicWorkingHoursParameters.setWednesday((new Gson()).toJson(workingHours.getWednesday()));
            clinicWorkingHoursParameters.setThursday((new Gson()).toJson(workingHours.getThursday()));
            clinicWorkingHoursParameters.setFriday((new Gson()).toJson(workingHours.getFriday()));
            String jsonString = post(API_CLINIC_WORKING_HOURS, clinicWorkingHoursParameters.toJson());
            System.out.println(clinicWorkingHoursParameters.toJson());
            Gson gson = new Gson();
            ParentResponse parentResponse = gson.fromJson(jsonString, ParentResponse.class);
            result = parentResponse.getStatus();
        } catch (Exception e) {
            Helper.handleError(TAG, "ClinicWorkingHours", e, -1, null);
        } finally {
            return result;
        }
    }

    public static boolean DoctorWorkingHours(WorkingHours workingHours, String openType, String doctorId) {
        boolean result = false;
        try {
            DoctorWorkingHoursParameters doctorWorkingHoursParameters = new DoctorWorkingHoursParameters();
            doctorWorkingHoursParameters.setUserId(doctorId);
            doctorWorkingHoursParameters.setOpenType(openType);
            doctorWorkingHoursParameters.setSaturday((new Gson()).toJson(workingHours.getSaturday()));
            doctorWorkingHoursParameters.setSunday((new Gson()).toJson(workingHours.getSunday()));
            doctorWorkingHoursParameters.setMonday((new Gson()).toJson(workingHours.getMonday()));
            doctorWorkingHoursParameters.setTuesday((new Gson()).toJson(workingHours.getTuesday()));
            doctorWorkingHoursParameters.setWednesday((new Gson()).toJson(workingHours.getWednesday()));
            doctorWorkingHoursParameters.setThursday((new Gson()).toJson(workingHours.getThursday()));
            doctorWorkingHoursParameters.setFriday((new Gson()).toJson(workingHours.getFriday()));
            String jsonString = post(API_DOCTOR_WORKING_HOURS, doctorWorkingHoursParameters.toJson());
            System.out.println(doctorWorkingHoursParameters.toJson());
            Gson gson = new Gson();
            ParentResponse parentResponse = gson.fromJson(jsonString, ParentResponse.class);
            result = parentResponse.getStatus();
        } catch (Exception e) {
            Helper.handleError(TAG, "DoctorWorkingHours", e, -1, null);
        } finally {
            return result;
        }
    }

    public static IsOpenResponse getIsOpen(int userId, int docId, int userType) {
        // this method must return status number 0 or 1 and return request id
        // so return IsOpenResponse
        IsOpenResponse isOpenResponse = new IsOpenResponse();
        try {
            IsOpenParameters openParameters = new IsOpenParameters();
            openParameters.setUserId(userId);
            String jsonString;
            if (userType == UserInfo.CLINIC) {
                openParameters.setClinicId(docId);
                jsonString = post(API_IS_OPEN_CLINIC, openParameters.toJson());
            } else {
                openParameters.setDocId(docId);
                jsonString = post(API_IS_OPEN_DOC, openParameters.toJson());
            }

            Gson gson = new Gson();
            isOpenResponse = gson.fromJson(jsonString, IsOpenResponse.class);

        } catch (Exception e) {
            Helper.handleError(TAG, "getIsOpen", e, -1, null);
        } finally {
            return isOpenResponse;
        }
    }

    public static HaveRateResponse getHaveRate(int userId, int id, int userType) {
        // this method must return status number 0 or 1 and return request id
        // so return IsOpenResponse
        HaveRateResponse haveRateResponse = null;
        try {
            HaveRateParameters haveRateParameters = new HaveRateParameters();
            haveRateParameters.setUserId(userId);
            String jsonString;
            if (userType == UserInfo.CLINIC) {
                haveRateParameters.setClinicId(id);
                jsonString = post(API_FLAG_HAVE_RATE_CLINIC, haveRateParameters.toJson());
            } else {
                haveRateParameters.setDocId(id);
                jsonString = post(API_FLAG_HAVE_RATE, haveRateParameters.toJson());
            }
            System.out.println("nour");
            System.out.println(haveRateParameters.toJson());
            System.out.println(jsonString);
            Gson gson = new Gson();
            haveRateResponse = gson.fromJson(jsonString, HaveRateResponse.class);

        } catch (Exception e) {
            Helper.handleError(TAG, "getHaveRate", e, -1, null);
        } finally {
            return haveRateResponse;
        }
    }

    //endregion
}
