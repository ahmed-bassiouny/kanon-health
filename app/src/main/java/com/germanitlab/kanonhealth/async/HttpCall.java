package com.germanitlab.kanonhealth.async;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.httpchat.MessageRequest;
import com.germanitlab.kanonhealth.httpchat.MessageResponse;
import com.germanitlab.kanonhealth.interfaces.ApiInterface;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.Forward;
import com.germanitlab.kanonhealth.models.Login;
import com.germanitlab.kanonhealth.models.Payment;
import com.germanitlab.kanonhealth.models.Questions.Answers;
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

    private Context context;
    private ApiResponse apiResponse;
    PrefManager prefManager;

    public HttpCall(Context actcontextvity, ApiResponse apiResponse) {

        this.context = actcontextvity;
        this.apiResponse = apiResponse;
        prefManager = new PrefManager(actcontextvity);
    }


    public void updateToken(RequsetToken requsetToken) {
        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }


    public void activateUser(int userID, String password, String code) {
        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    public void registerUser(String phone, String countryCode) {
        try {

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

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllDoctor(String userID, String password) {
        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }


    public void getChatDoctors(String userID, String password) {
        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    public void getChatClinics(String userID, String password) {
        try {
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

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void getChatClient(String userID, String password) {
        try {
            BasicRequest request = new BasicRequest(userID, password, 1);
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<List<User>> connection = service.getMyClients(request);

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

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void getChatDoctorAndClinics(String userID, String password) {
        try {
            BasicRequest request = new BasicRequest(userID, password, 1);
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<List<User>> connection = service.getMyDoctorsAndClients(request);

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

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void getSpecialities(String userID, String password) {
        try {
            BasicRequest basicRequest = new BasicRequest(userID, password);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void login(String userID, String password, String qr) {
        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    //-- get profile
    public void getProfile(UserRegisterResponse user) {
        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }


    public void sendPopUpResult(int userID, String password, String doctorID, ArrayList<HashMap<String, String>> data) {
        try {
            final InqueryRequest popUpRequest = new InqueryRequest(userID, password, doctorID, data);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void getLocation() {
        try {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<com.google.gson.JsonObject> call = apiInterface.getLocation();
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void getDoctor(String userID, String password, String doctorID, int entity_type) {
        try {
            DoctorRequest doctorRequest = new DoctorRequest(userID, password, doctorID, entity_type);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }


    public void getDoctorId(String userID, String password, String doctorID) {
        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void getrating(String doctorID) {
        try {
            Comment comment = new Comment(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)), prefManager.getData(PrefManager.USER_ID), "0", doctorID);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<List<Comment>> call = apiInterface.getrating(comment);
            call.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    Log.d("DoctorResponse ", response.body().toString());
                    apiResponse.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    Log.e("DoctorResponse ", " " + t.getLocalizedMessage());
                    apiResponse.onFailed(t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }


    public void goOnline(String userID, String password, String isAvailable) {
        try {
            StatusRequestModel request = new StatusRequestModel(userID, password, isAvailable);
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<StatusResponse> connection = service.goOnline(request);

            connection.enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                    Log.d("Setting ", response.body().toString());
                    apiResponse.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {

                    Log.e("Setting ", " " + t.getLocalizedMessage());
                    apiResponse.onFailed(t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }


    public void uploadImage(String userID, String password, String imagePath) {
        try {
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

            Call<UploadImageResponse> connection = service.uploadProfileImage(id, pass, body);

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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }


    //-- Edit
    public void editProfile(User request) {
        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }


    public void addPractice(User request) {
        try {
            // UserInfoResponse request = new UserInfoResponse();
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> connection = service.addPractice(request);

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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    //--- getQuestions
    public void getDocQuestions(String userID, String password) {

        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }


    //--- delete Message
    public void deleteMessage(int userID, String password, String messageId) {
        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }


    //-- submit question answers.
    public void submitAnswers(String userID, String password, String doctorID, List<Answers> answersList) {
        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    public void getlocations(String userID, String password, int id, int type) {
        try {
            LocationRequest locationRequest = new LocationRequest(userID, password, id, type);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void sendSessionRequest(String userID, String password, String id, String type) {
        try {
            Payment payment = new Payment(userID, password, id, type);
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> connection = service.sendSessionRequest(payment);
            connection.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.d("Answers ", response.body().toString());
                    apiResponse.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                    Log.e("Answers ", " " + t.getMessage());
                    apiResponse.onFailed(t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }


    public void forward(String userID, String password, List id, List doctors) {
        try {
            Forward forward = new Forward(userID, password, id, doctors);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    //-- get Setting.
    public void getSetting() {

        try {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    public void updatePrivacy(String userID, String password, int id, int privacy) {
        try {
            //the same request model but htis is for uppdate the privacy
            UpdatePrivacy locationRequest = new UpdatePrivacy(userID, password, id, privacy);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void addToMyDoctor(String doc_id) {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            DoctorRequest mDoctorRequest = new DoctorRequest(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), "", doc_id);
            Call<JsonObject> connection = service.addToMyDoctor(mDoctorRequest);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void removeFromMyDoctor(String doc_id) {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            DoctorRequest mDoctorRequest = new DoctorRequest(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), "", doc_id);
            Call<JsonObject> connection = service.removeFromMyDoctor(mDoctorRequest);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void rateDoctor(String doc_id, String txtcomment, String rate,String requestId) {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Comment comment = new Comment(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)), prefManager.getData(PrefManager.USER_PASSWORD), "0", doc_id, txtcomment, rate,requestId);
            Call<JsonObject> connection = service.rateDoctor(comment);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void closeSession(String id) {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            DoctorRequest mDoctorRequest = new DoctorRequest(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), "","0", id);
            Call<JsonObject> connection = service.closeSession(mDoctorRequest);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void getAllSpecilaities() {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<List<ChooseModel>> connection = service.getAllSpecilaities();
            connection.enqueue(new Callback<List<ChooseModel>>() {
                @Override
                public void onResponse(Call<List<ChooseModel>> call, Response<List<ChooseModel>> response) {
                    apiResponse.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<List<ChooseModel>> call, Throwable t) {
                    apiResponse.onFailed(t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void getAllLanguage() {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<List<ChooseModel>> connection = service.getAllLanguage();
            connection.enqueue(new Callback<List<ChooseModel>>() {
                @Override
                public void onResponse(Call<List<ChooseModel>> call, Response<List<ChooseModel>> response) {
                    apiResponse.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<List<ChooseModel>> call, Throwable t) {
                    apiResponse.onFailed(t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void getAllMemberAt() {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<List<ChooseModel>> connection = service.getAllMemberAt();
            connection.enqueue(new Callback<List<ChooseModel>>() {
                @Override
                public void onResponse(Call<List<ChooseModel>> call, Response<List<ChooseModel>> response) {
                    apiResponse.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<List<ChooseModel>> call, Throwable t) {
                    apiResponse.onFailed(t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void getDoctorAll() {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<List<ChooseModel>> connection = service.getDoctorAll();
            connection.enqueue(new Callback<List<ChooseModel>>() {
                @Override
                public void onResponse(Call<List<ChooseModel>> call, Response<List<ChooseModel>> response) {
                    apiResponse.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<List<ChooseModel>> call, Throwable t) {
                    apiResponse.onFailed(t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void addClinic(User user) {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> connection = service.addClinic(user);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void closeSessionAndOpenNewSession(final String userID, final String password, final String id, final String type) {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            DoctorRequest mDoctorRequest = new DoctorRequest(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), "", id);
            final Call<JsonObject> connection = service.closeSession(mDoctorRequest);
            connection.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Payment payment = new Payment(userID, password, id, type);
                    ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonObject> connection2 = service.sendSessionRequest(payment);
                    connection2.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.d("Answers ", response.body().toString());
                            apiResponse.onSuccess(response.body());
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                            Log.e("Answers ", " " + t.getMessage());
                            apiResponse.onFailed(t.getLocalizedMessage());
                        }
                    });
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    apiResponse.onFailed(t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void editClinic(User user) {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> connection = service.editClinic(user);
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void loadChat(MessageRequest messageRequest) {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<MessageResponse> connection = service.loadChat(messageRequest);
            connection.enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                    if (response.body().getStatus() == 1)
                        apiResponse.onSuccess(response.body().getMessage());
                    else
                        onFailure(call, new Exception(context.getString(R.string.error_loading_data)));
                }

                @Override
                public void onFailure(Call<MessageResponse> call, Throwable t) {
                    Log.e("Httpcall", "loadChat: ", t);
                    Crashlytics.logException(t);
                    Toast.makeText(context, R.string.error_loading_data, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Httpcall", "loadChat: ", e);
            Crashlytics.logException(e);
            Toast.makeText(context, R.string.error_loading_data, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessage(Message message) {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<MessageResponse> connection = service.sendMessage(message);
            connection.enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {


                    if (response.body().getStatus() == 1)

                    if (response.body().getStatus()!=null &&response.body().getStatus() == 1)

                        apiResponse.onSuccess(response.body().getMsg());
                    else
                        onFailure(call, new Exception());
                }

                @Override
                public void onFailure(Call<MessageResponse> call, Throwable t) {
                    t.printStackTrace();

                }
            });
        } catch (Exception e) {
            Log.e("Httpcall", "sendMessage: ", e);
            Crashlytics.logException(e);
        }
    }

    public void uploadMedia(String imagePath) {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            //File creating from selected path
            File file = new File(imagePath);

            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);


            Call<UploadImageResponse> connection = service.uploadMedia(body);

            connection.enqueue(new Callback<UploadImageResponse>() {
                @Override
                public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                    if (response.body().getStatus() == 1)
                        apiResponse.onSuccess(response.body().getFile_url());
                    else
                        onFailure(call, new Exception("status not equal 1"));

                }

                @Override
                public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                    Crashlytics.logException(t);
                    Log.e("httpcall", "uploadMedia: ", t);
                    Toast.makeText(context, context.getResources().getText(R.string.cantupload), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("httpcall", "uploadMedia: ", e);
            Toast.makeText(context, context.getResources().getText(R.string.cantupload), Toast.LENGTH_SHORT).show();
        }


    }


    public void sendQrCode(WebLogin webLogin) {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> connection = service.webLoginQrCode(webLogin);
            connection.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    apiResponse.onSuccess(response);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(context, R.string.send_fail, Toast.LENGTH_LONG).show();
                    apiResponse.onFailed(t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("Httpcall", "sendMessage: ", e);
            Crashlytics.logException(e);
        }
    }
    public void messagesSeen(MessageRequest messageRequest){
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> connection = service.messagesSeen(messageRequest);
            connection.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    apiResponse.onSuccess(response);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(context, R.string.send_fail, Toast.LENGTH_LONG).show();
                    apiResponse.onFailed(t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("Httpcall", "sendMessage: ", e);
            Crashlytics.logException(e);
        }
    }
    public void messagesDeliver(MessageRequest messageRequest){
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> connection = service.messagesDeliver(messageRequest);
            connection.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    apiResponse.onSuccess(response);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(context, R.string.send_fail, Toast.LENGTH_LONG).show();
                    apiResponse.onFailed(t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("Httpcall", "sendMessage: ", e);
            Crashlytics.logException(e);
        }
    }
}
