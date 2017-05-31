package com.germanitlab.kanonhealth.async;

import android.app.Activity;
import android.util.Log;

import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.interfaces.ApiInterface;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.Forward;
import com.germanitlab.kanonhealth.models.Login;
import com.germanitlab.kanonhealth.models.Payment;
import com.germanitlab.kanonhealth.models.Questions.Answers;
import com.germanitlab.kanonhealth.models.Questions.InqueryRequest;
import com.germanitlab.kanonhealth.models.Questions.SubmitQuestionRequest;
import com.germanitlab.kanonhealth.models.RequsetToken;
import com.germanitlab.kanonhealth.models.SettingResponse;
import com.germanitlab.kanonhealth.models.Speciality;
import com.germanitlab.kanonhealth.models.UpdatePrivacy;
import com.germanitlab.kanonhealth.models.doctors.DoctorRequest;
import com.germanitlab.kanonhealth.models.messages.DeleteMessage;
import com.germanitlab.kanonhealth.models.user.ActivateAccountRequest;
import com.germanitlab.kanonhealth.models.user.BasicRequest;
import com.germanitlab.kanonhealth.models.user.LocationRequest;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.models.user.UserRegisterRequest;
import com.germanitlab.kanonhealth.models.user.UserRegisterResponse;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eslam on 1/10/17.
 */

public class HttpCall {

    private Activity activity;
    private ApiResponse apiResponse;

    public HttpCall(Activity activity, ApiResponse apiResponse) {

        this.activity = activity;
        this.apiResponse = apiResponse;
    }

    public HttpCall(ApiResponse apiResponse) {

        this.apiResponse = apiResponse;
    }

    public void updateToken(RequsetToken requsetToken) {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<com.google.gson.JsonObject> connection = service.updateToken(requsetToken);

        connection.enqueue(new Callback<com.google.gson.JsonObject>() {
            @Override
            public void onResponse(Call<com.google.gson.JsonObject> call, Response<com.google.gson.JsonObject> response) {
                String reponseStr = response.toString().replaceAll("\\\\", "");
                Log.e("new String", reponseStr);
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<com.google.gson.JsonObject> call, Throwable t) {

                Log.e("QR ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }



    public void activateUser(int userID, String password, String code) {

        ActivateAccountRequest request = new ActivateAccountRequest(userID, password, code);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> connection = service.activateUser(request);

        connection.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.d("activateUser ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e("activateUser ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });

    }

    public void registerUser(String phone, String countryCode) {

        UserRegisterRequest request = new UserRegisterRequest(phone, countryCode, "2");
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<UserRegisterResponse> connection = service.registerUser(request);

        connection.enqueue(new Callback<UserRegisterResponse>() {
            @Override
            public void onResponse(Call<UserRegisterResponse> call, Response<UserRegisterResponse> response) {

                Log.d("activateUser ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<UserRegisterResponse> call, Throwable t) {

                Log.e("activateUser ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });

    }

    public void getAllDoctor(String userID, String password) {

        BasicRequest request = new BasicRequest(userID, password, 1);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<User>> connection = service.getAllDoctor(request);

        connection.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                Log.d("DoctorResponse ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

                Log.e("activateUser ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });

    }


    public void getChatDoctors(String userID, String password) {

        BasicRequest request = new BasicRequest(userID, password, 1);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<User>> connection = service.getMyDoctor(request);

        connection.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                Log.d("My User ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

                Log.e("My User ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });

    }
    public void getChatClinics(String userID, String password) {

        BasicRequest request = new BasicRequest(userID, password, 1);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<User>> connection = service.getMyClinics(request);

        connection.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                Log.d("My User ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

                Log.e("My User ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });

    }
    public void getSpecialities(String userID , String password)
    {
        BasicRequest basicRequest = new BasicRequest(userID , password );
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<Speciality>> call = service.getSpecialities(basicRequest);
        call.enqueue(new Callback<ArrayList<Speciality>>() {
            @Override
            public void onResponse(Call<ArrayList<Speciality>> call, Response<ArrayList<Speciality>> response) {
                apiResponse.onSuccess((response.body()));
            }

            @Override
            public void onFailure(Call<ArrayList<Speciality>> call, Throwable t) {
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }

    public void login(String userID, String password, String qr) {

        Login request = new Login(userID, password, qr);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> connection = service.login(request);

        connection.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.d("QR ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e("QR ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //-- get profile
    public void getProfile(UserRegisterResponse user) {

        BasicRequest request = new BasicRequest(String.valueOf(user.getUser_id()), user.getPassword());
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<UserInfoResponse> connection = service.getProfile(request);

        connection.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {

                Log.d("QR ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {

                Log.e("QR ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });

    }



    public void sendPopUpResult (int userID, String password, String doctorID , ArrayList<HashMap<String, String>> data){
        final InqueryRequest popUpRequest = new InqueryRequest(userID, password, doctorID , data);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<com.google.gson.JsonObject> call = apiInterface.sendPopUpResult(popUpRequest);
        call.enqueue(new Callback<com.google.gson.JsonObject>() {
            @Override
            public void onResponse(Call<com.google.gson.JsonObject> call, Response<com.google.gson.JsonObject> response) {
                Log.d("PopUp Request", "Success");
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<com.google.gson.JsonObject> call, Throwable t) {
                Log.e("PopUp Error", t.getMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }

    public void getDoctor (String userID, String password, String doctorID , int entity_type){

        DoctorRequest doctorRequest = new DoctorRequest(userID, password, doctorID , entity_type);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UserInfoResponse> call = apiInterface.getDoctor(doctorRequest);
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                Log.d("DoctorResponse ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.e("DoctorResponse ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }

    public void getDoctorId (String userID, String password ,  String doctorID){

        DoctorRequest doctorRequest = new DoctorRequest(userID, password, "0", doctorID);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UserInfoResponse> call = apiInterface.getDoctorId(doctorRequest);
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                Log.d("DoctorResponse ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.e("DoctorResponse ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }

    public void getrating(String userID, String password, String doctorID){

        DoctorRequest doctorRequest = new DoctorRequest(userID, password, "0", doctorID);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<User>> call = apiInterface.getrating(doctorRequest);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("DoctorResponse ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("DoctorResponse ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }

    public void uploadImage(String userID, String password, String imagePath) {


        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        //File creating from selected path
        File file = new File(imagePath);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        RequestBody id =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, userID);

        RequestBody pass =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, password);

        Call<UploadImageResponse> connection = service.uploadProfileImage(id, pass , body);

        connection.enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                Log.e("image response success:", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                Log.e("imageUploadFailed", t.toString());
            }
        });

    }


    //-- Edit
    public void editProfile(User request) {

        // UserInfoResponse request = new UserInfoResponse();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> connection = service.editProfile(request);

        connection.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.d("QR ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e("QR ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });

    }

    //--- getQuestions
    public void getDocQuestions(String userID, String password) {


        BasicRequest request = new BasicRequest(userID, password);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<String>> connection = service.getQuestions(request);

        connection.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                Log.d("Qestion ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

                Log.e("Qestion ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });

    }


    //--- delete Message
    public void deleteMessage(int userID, String password, String messageId) {


        DeleteMessage request = new DeleteMessage(userID, password, messageId);

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> connection = service.deleteMessage(request);

        connection.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("delete message call", call.toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e("delete message fail", call.toString());

                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }


    //-- submit question answers.
    public void submitAnswers(String userID, String password, String doctorID, List<Answers> answersList) {


        SubmitQuestionRequest request = new SubmitQuestionRequest(userID, password, doctorID, answersList);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> connection = service.submitAnswers(request);

        connection.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.d("Answers ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e("Answers ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });

    }

    public void getlocations(String userID , String password , int id , int type)
    {
        LocationRequest locationRequest = new LocationRequest(userID , password  , id , type );
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<List<User>> connection = service.getLocations(locationRequest);
        connection.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("Answers ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

                Log.e("Answers ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }

    public void sendPayment(String userID , String password , String id , String type)
    {
        Payment payment = new Payment(userID , password  , id , type );
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> connection = service.sendPayment(payment);
        connection.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("Answers ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e("Answers ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }


    public void forward(String userID , String password , List id , List doctors)
    {
        Forward forward = new Forward(userID , password  , id , doctors );
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> connection = service.forward(forward);
        connection.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("Answers ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e("Answers ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }

    //-- get Setting.
    public void getSetting() {


        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<SettingResponse> connection = service.getSetting();

        connection.enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {

                Log.d("Setting ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {

                Log.e("Setting ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });

    }

    public void updatePrivacy(String userID , String password , int id , int privacy)
    {
        //the same request model but htis is for uppdate the privacy
        UpdatePrivacy locationRequest = new UpdatePrivacy(userID , password  , id , privacy );
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> connection = service.updatePrivacy(locationRequest);
        connection.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("Answers ", response.body().toString());
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e("Answers ", " " + t.getLocalizedMessage());
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }
    public void addToMyDoctor(String doc_id){
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        DoctorRequest mDoctorRequest =new DoctorRequest(String.valueOf(AppController.getInstance().getClientInfo().getUser_id()),AppController.getInstance().getClientInfo().getPassword(),"",doc_id);
        Call<JsonObject> connection=service.addToMyDoctor(mDoctorRequest);
        connection.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }
    public void removeFromMyDoctor(String doc_id){
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        DoctorRequest mDoctorRequest =new DoctorRequest(String.valueOf(AppController.getInstance().getClientInfo().getUser_id()),AppController.getInstance().getClientInfo().getPassword(),"",doc_id);
        Call<JsonObject> connection=service.removeFromMyDoctor(mDoctorRequest);
        connection.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                apiResponse.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                apiResponse.onFailed(t.getLocalizedMessage());
            }
        });
    }
}
