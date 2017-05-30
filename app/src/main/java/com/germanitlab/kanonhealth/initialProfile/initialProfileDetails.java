package com.germanitlab.kanonhealth.initialProfile;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.germanitlab.kanonhealth.PasscodeActivty;
import com.germanitlab.kanonhealth.models.user.User1;
import com.google.gson.Gson;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.InternetFilesOperations;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;

/**
 * Created by Mo on 3/12/17.
 */

public class initialProfileDetails extends AppCompatActivity {

    @BindView(R.id.image_profile)ImageView imageProfile;
    @BindView(R.id.edit_first_name)EditText editFirstName;
    @BindView(R.id.edit_last_name)EditText editLastName;
    @BindView(R.id.edit_birthday)TextView textBirthday;
    PrefManager mPrefManager;
    UploadImageResponse uploadImageResponse;
    private InternetFilesOperations internetFilesOperations;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mPrefManager = new PrefManager(this);
        setContentView(R.layout.profile_details_activity);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.image_profile)
    public void onAddProfileImageClicked (){
        dispatchOpenGalleryIntent();
    }

    public  void dispatchOpenGalleryIntent () {
        if (ContextCompat.checkSelfPermission(this , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, Constants.IMAGE_REQUEST);
        } else {
            askForPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.GALLERY_PERMISSION_CODE );
        }
    }

    private void askForPermission(String[] permission, Integer requestCode) {
        ActivityCompat.requestPermissions(this, permission, requestCode);
    }

    @OnClick(R.id.edit_birthday)
    public void onEditBirthdayClicked (){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Calendar calender = Calendar.getInstance();
        Dialog mDialog = new DatePickerDialog(initialProfileDetails.this,
                android.R.style.Theme_Holo,
                mDateSetListener, calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH), calender
                .get(Calendar.DAY_OF_MONTH));

        mDialog.show();
    }

    @OnClick(R.id.button_submit)
    public void onSubmitClicked (){

        String firstName = editLastName.getText().toString();
        String lastName = editFirstName.getText().toString();
        String birthDate = textBirthday.getText().toString();


        if (!firstName.equals("") && !lastName.equals("") && !birthDate.equals("")){
            showProgressDialog();
            final User1 user = new User1();
            user.setId(AppController.getInstance().getClientInfo().getUser_id());
            user.setPassword(AppController.getInstance().getClientInfo().getPassword());
            user.setName(firstName + " " +lastName);
            user.setBirthDate(birthDate);
            if (uploadImageResponse != null){
                user.setAvatar(uploadImageResponse.getFile_url());
            }

            new HttpCall(this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    Log.e("Update user response :", user != null ? response.toString() : "no response found" );


                    mPrefManager.put(mPrefManager.USER_KEY, response.toString());
                    Gson gson = new Gson();
                    UserInfoResponse userInfoResponse = gson.fromJson(response.toString() , UserInfoResponse.class);
                    Log.e("my qr link " ,  userInfoResponse.getUser().getQr_url());
                    mPrefManager.put(mPrefManager.IS_DOCTOR ,userInfoResponse.getUser().getIsDoc() == 1 );

                    mPrefManager.put(mPrefManager.Image_data , userInfoResponse.getUser().getQr_url());
                    dismissProgressDialog();
                    Intent intent = new Intent(getApplicationContext() , PasscodeActivty.class);
                    intent.putExtra("status", 1);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailed(String error) {

                }
            }).editProfile(user);
        } else {
            Toast.makeText(this, getResources().getString(R.string.answer), Toast.LENGTH_SHORT).show();
        }

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            arg2 = arg2 + 1;

            String my_date = arg1 + "-" + arg2 + "-" + arg3;
            textBirthday.setText(my_date);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ){
            switch (requestCode) {
                case Constants.IMAGE_REQUEST:


                    Uri imageUri = data.getData();
                    showProgressDialog();
                    Log.e("ImageUri", imageUri != null ? imageUri.toString() : "Empty Uri");
                    Glide.with(this).load(imageUri).into(imageProfile);

                    new HttpCall(this, new ApiResponse() {
                        @Override
                        public void onSuccess(Object response) {
                            dismissProgressDialog();
                            uploadImageResponse = (UploadImageResponse) response;
                            Log.e("After Casting", uploadImageResponse.getFile_url());
                        }

                        @Override
                        public void onFailed(String error) {
                            Log.e("upload image failed :", error);
                        }
                    }).uploadImage(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                            , AppController.getInstance().getClientInfo().getPassword(), getPathFromURI(imageUri));

                    break;

            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String path;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            path = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path = cursor.getString(idx);
            cursor.close();
        }
        return path;
    }


    public void showProgressDialog(){progressDialog = ProgressDialog.show(this, "", "Bitte, Warten Sie...", true);}

    public void dismissProgressDialog() {progressDialog.dismiss();}

}
