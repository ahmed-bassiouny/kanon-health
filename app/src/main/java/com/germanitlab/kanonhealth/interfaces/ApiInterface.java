package com.germanitlab.kanonhealth.interfaces;


import com.germanitlab.kanonhealth.httpchat.MessageRequest;
import com.germanitlab.kanonhealth.httpchat.MessageRequestSeen;
import com.germanitlab.kanonhealth.httpchat.MessageResponse;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.Forward;
import com.germanitlab.kanonhealth.models.Login;
import com.germanitlab.kanonhealth.models.Payment;
import com.germanitlab.kanonhealth.models.Questions.InqueryRequest;
import com.germanitlab.kanonhealth.models.Questions.SubmitQuestionRequest;
import com.germanitlab.kanonhealth.models.RequsetToken;
import com.germanitlab.kanonhealth.models.SettingResponse;
import com.germanitlab.kanonhealth.models.Speciality;
import com.germanitlab.kanonhealth.models.StatusRequestModel;
import com.germanitlab.kanonhealth.models.StatusResponse;
import com.germanitlab.kanonhealth.models.UpdatePrivacy;
import com.germanitlab.kanonhealth.models.WebLogin;
import com.germanitlab.kanonhealth.models.doctors.Comment;
import com.germanitlab.kanonhealth.models.doctors.DoctorRequest;
import com.germanitlab.kanonhealth.models.messages.DeleteMessage;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.ActivateAccountRequest;
import com.germanitlab.kanonhealth.models.user.BasicRequest;
import com.germanitlab.kanonhealth.models.user.LocationRequest;
import com.germanitlab.kanonhealth.models.user.UpdateDocumentPrivacyRequest;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.models.user.UserRegisterRequest;
import com.germanitlab.kanonhealth.models.user.UserRegisterResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by eslam on 8/28/16.
 */
public interface ApiInterface {

    @POST("/users/activate")
    Call<JsonObject> activateUser(@Body ActivateAccountRequest request);

    @POST("/users/add")
    Call<UserRegisterResponse> registerUser(@Body UserRegisterRequest request);

    @POST("/doctors/all")
    Call<List<User>> getAllDoctor(@Body BasicRequest request);

    @POST("/users/all")
    Call<List<User>> getMyDoctor(@Body BasicRequest request);

    @POST("/doctors/login")
    Call<JsonObject> login(@Body Login request);

    @POST("/specialties")
    Call<ArrayList<Speciality>> getSpecialities(@Body BasicRequest request);

    @POST("questions/answer")
    Call<JsonObject> sendPopUpResult(@Body InqueryRequest popUpRequest);

    @POST("/json")
    Call<JsonObject> getLocation();

    @POST("/users/get_myuser")
    Call<UserInfoResponse> getProfile(@Body BasicRequest request);


    @POST("/users/get_user")
    Call<UserInfoResponse> getDoctorId(@Body DoctorRequest request);

    @POST("/users/update")
    Call<JsonObject> editProfile(@Body User request);

    @POST("/users/update")
    Call<JsonObject> addPractice(@Body User request);

    @POST("/messages/update_privacy")
    Call<JSONObject> updateDocuments(@Body UpdateDocumentPrivacyRequest updateDocumentPrivacyRequest);

    @POST("/questions")
    Call<List<String>> getQuestions(@Body BasicRequest request);

    @POST("/questions/answer")
    Call<JsonObject> submitAnswers(@Body SubmitQuestionRequest request);

    @POST("/doctors/filter")
    Call<List<User>> getLocations(@Body LocationRequest request);

    @POST("/app/settings")
    Call<SettingResponse> getSetting();

    @POST("/messages/delete")
    Call<JsonObject> deleteMessage(@Body DeleteMessage request);

    @POST("/qr/get_user")
    Call<UserInfoResponse> getDoctor(@Body DoctorRequest doctorRequest);

    @POST("/clinics/all")
    Call<List<User>> getMyClinics(@Body BasicRequest request);

    @POST("/clients/all")
    Call<List<User>> getMyClients(@Body BasicRequest request);

    @POST("/another/all")
    Call<List<User>> getMyDoctorsAndClients(@Body BasicRequest request);

    @POST("/rate/list")
    Call<List<Comment>> getrating(@Body Comment comment);

    @POST("/doctors/change_status")
    Call<StatusResponse> goOnline(@Body StatusRequestModel request);

    @POST("/users/update_token")
    Call<JsonObject> updateToken(@Body RequsetToken request);

    @POST("/messages/update_privacy")
    Call<JsonObject> updatePrivacy(@Body UpdatePrivacy locationRequest);

    @POST("/request/session")
    Call<JsonObject> sendSessionRequest(@Body Payment payment);

    @POST("/messages/forward")
    Call<JsonObject> forward(@Body Forward forward);

    @Multipart
    @POST("/upload")
    Call<UploadImageResponse> uploadProfileImage(@Part("user_id") RequestBody id, @Part("password") RequestBody password, @Part MultipartBody.Part image);

    // Edit by Ahmed 29-5-2017
    @POST("/doctors/add_to_my_doctors")
    Call<JsonObject> addToMyDoctor(@Body DoctorRequest request);

    @POST("/doctors/remove_from_my_doctors")
    Call<JsonObject> removeFromMyDoctor(@Body DoctorRequest request);

    //Edit by ahmed 6-6-2017
    @POST("/rate/update")
    Call<JsonObject> rateDoctor(@Body Comment comment);

    @POST("request/close_session")
    Call<JsonObject> closeSession(@Body DoctorRequest request);

    //Edit by ahmed 8-6-2017
    @POST("/specialties/all")
    Call<List<ChooseModel>> getAllSpecilaities();

    @POST("/langs/all")
    Call<List<ChooseModel>> getAllLanguage();

    @POST("/clinics/list")
    Call<List<ChooseModel>> getAllMemberAt();

    @POST("/doctors/all")
    Call<List<ChooseModel>> getDoctorAll();

    @POST("/clinics/add")
    Call<JsonObject> addClinic(@Body User user);

    @POST("/clinics/edit")
    Call<JsonObject> editClinic(@Body User user);

    @POST("/messages")
    Call<MessageResponse> loadChat(@Body MessageRequest messageRequest);

    @POST("/messages/send")
    Call<MessageResponse> sendMessage(@Body Message message);

    @POST("/qr/join_web")
    Call<JsonObject> webLoginQrCode(@Body WebLogin code);

    @Multipart
    @POST("/upload")
    Call<UploadImageResponse> uploadMedia( @Part MultipartBody.Part image);

    @POST("/messages/seen")
    Call<JsonObject> messagesSeen(@Body MessageRequestSeen messageRequest);

    @POST("/messages/deliver")
    Call<JsonObject> messagesDeliver(@Body MessageRequestSeen messageRequest);

}

