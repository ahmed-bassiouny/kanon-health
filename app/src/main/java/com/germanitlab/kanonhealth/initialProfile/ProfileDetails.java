package com.germanitlab.kanonhealth.initialProfile;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.Crop.PickerBuilder;
import com.germanitlab.kanonhealth.PasscodeActivty;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.custom.FixedHoloDatePickerDialog;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.DateUtil;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.models.user.UserRegisterResponse;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mo on 3/12/17.
 */

public class ProfileDetails extends ParentActivity implements DialogPickerCallBacks {

    private static final int TAKE_PICTURE = 1;
    private static final int CROP_PIC = 5;

    @BindView(R.id.rggender)
    RadioGroup rgGender;
    @BindView(R.id.edgender)
    EditText edGender;
    @BindView(R.id.image_profile)
    ImageView imageProfile;
    @BindView(R.id.edit_first_name)
    EditText editFirstName;
    @BindView(R.id.edit_last_name)
    EditText editLastName;
    @BindView(R.id.edit_birthday)
    TextView textBirthday;
    PrefManager mPrefManager;
    @BindView(R.id.et_title)
    EditText et_title;
    UploadImageResponse uploadImageResponse;
    ProgressDialog progressDialog;
    PickerDialog pickerDialog;
    String birthdate="";
    String gender_other = "Male";
    int gender = 1;
    PrefManager prefManager;
    Util util;
    Helper helper ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mPrefManager = new PrefManager(this);
            pickerDialog = new PickerDialog();
            util = Util.getInstance(this);
            setContentView(R.layout.profile_details_activity);
            ButterKnife.bind(this);
            prefManager = new PrefManager(this);
//            if (savedInstanceState != null) {
//                textBirthday.setText(savedInstanceState.getString("birthdate"));
//                selectedImageUri = Uri.parse(savedInstanceState.getString("imageURI"));
//                ImageHelper.setImage(imageProfile, selectedImageUri, this);
//            }
            editLastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    textBirthday.performClick();
                    return true;
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
        rgGender.check(R.id.rbmale);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rbmale:
                        gender_other = "Male";
                        gender = 1;
                        edGender.setVisibility(View.GONE);
                        break;
                    case R.id.rbfemal:
                        gender_other = "Femal";
                        gender = 2;
                        edGender.setVisibility(View.GONE);
                        break;
                    case R.id.rbother:
                        gender_other = "";
                        gender = 3;
                        edGender.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }

    @OnClick(R.id.image_profile_edit)
    public void onAddProfileImageClicked() {
        if (!Helper.isNetworkAvailable(this)) {
            Toast.makeText(this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {


                pickerDialog.show(getFragmentManager(), "imagePickerDialog");
            } else {
                askForPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        Constants.GALLERY_PERMISSION_CODE);
            }
        } catch (RuntimeException e) {
            Toast.makeText(this, R.string.please_access_storage, Toast.LENGTH_SHORT).show();
        }

    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        if (selectedImageUri != null)
//            outState.putString("imageURI", selectedImageUri.toString());
//        outState.putString("birthdate", textBirthday.getText().toString());
//        super.onSaveInstanceState(outState);
//    }



    private void askForPermission(String[] permission, Integer requestCode) {
        ActivityCompat.requestPermissions(this, permission, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //resume tasks needing this
                switch (requestCode) {
                    case Constants.GALLERY_PERMISSION_CODE:
                        pickerDialog.show(getFragmentManager(), "imagePickerDialog");
                        break;
                }
            }
        }
    }

    @OnClick(R.id.edit_birthday)
    public void onEditBirthdayClicked() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        // old code about dateoicker
        /*
        Calendar calender = Calendar.getInstance();
        final Context themedContext = new ContextThemeWrapper(
                ProfileDetails.this,
                android.R.style.Theme_Holo_Light_Dialog
        );

        final DatePickerDialog mDialog = new FixedHoloDatePickerDialog(
                themedContext,
                mDateSetListener,
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH));

        mDialog.show();*/

        /*
        Calendar calender = Calendar.getInstance();
        if(birthdate.isEmpty())
        birthdate=calender.get(Calendar.YEAR) + "-" + calender.get(Calendar.MONTH) + "-" +calender.get(Calendar.DAY_OF_MONTH);
        DatePickerPopWin pickerPopWin=new DatePickerPopWin.Builder(ProfileDetails.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                birthdate = dateDesc;
                Date parseDate = null;
                try {
                    parseDate = DateUtil.getAnotherFormat().parse(birthdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String newformatDate = (DateUtil.formatBirthday(parseDate.getTime()));
                textBirthday.setText(newformatDate);
            }
        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .minYear(1900) //min year in loop
                .maxYear(calender.get(Calendar.YEAR)+1) // max year in loop
                .showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                .dateChose(birthdate) // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(this);*/
        if(helper==null)
            helper=new Helper(this);
        helper.showDatePicker(textBirthday);
    }

    @OnClick(R.id.button_submit)
    public void onSubmitClicked() {

        if (!Helper.isNetworkAvailable(this)) {
            Toast.makeText(this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            return;
        }
        String firstName = editFirstName.getText().toString();
        String lastName = editLastName.getText().toString();
        String birthDate = textBirthday.getText().toString();
        gender_other = edGender.getText().toString();
        if (gender == 3 && gender_other.trim().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.please_fill_data), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!firstName.trim().isEmpty() && !lastName.trim().isEmpty() && !birthDate.trim().isEmpty()) {
            util.showProgressDialog();
            final User user = new User();
            user.setId(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));
            user.setPassword(prefManager.getData(PrefManager.USER_PASSWORD));
            user.setFirst_name(firstName);
            user.setLast_name(lastName);
            user.setSubTitle(et_title.getText().toString());
            user.setBirthDate(birthdate);
            user.setGender(gender);
            user.setGender_other(gender_other);
            if (uploadImageResponse != null) {
                user.setAvatar(uploadImageResponse.getFile_url());
            }

            new HttpCall(this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    Log.e("Update user response :", user != null ? response.toString() : "no response found");


                    mPrefManager.put(mPrefManager.USER_KEY, response.toString());
                    Gson gson = new Gson();
                    UserInfoResponse userInfoResponse = gson.fromJson(response.toString(), UserInfoResponse.class);
                    Log.e("my qr link ", userInfoResponse.getUser().getQr_url());
                    mPrefManager.put(mPrefManager.IS_DOCTOR, userInfoResponse.getUser().getIsDoc() == 1);
                    mPrefManager.put(mPrefManager.PROFILE_QR, userInfoResponse.getUser().getQr_url());
                    loadData();

                }

                @Override
                public void onFailed(String error) {
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_saving_data), Toast.LENGTH_SHORT).show();
                    util.dismissProgressDialog();

                }
            }).editProfile(user);
        } else {
            Toast.makeText(this, getResources().getString(R.string.please_fill_data), Toast.LENGTH_SHORT).show();
        }

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            arg2 = arg2 + 1;

            birthdate = arg1 + "-" + arg2 + "-" + arg3;
            Date parseDate = null;
            try {
                parseDate = DateUtil.getAnotherFormat().parse(birthdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String s = (DateUtil.formatBirthday(parseDate.getTime()));
            textBirthday.setText(s);
        }
    };





    @Override
    public void onGalleryClicked(Intent intent) {
        Helper.getCroppedImageFromCamera(this, PickerBuilder.SELECT_FROM_GALLERY);
    }

    @Override
    public void onCameraClicked() {
        Helper.getCroppedImageFromCamera(this, PickerBuilder.SELECT_FROM_CAMERA);
    }

    @Override
    public void deleteMyImage() {

    }

    private void loadData() {
        try {
            UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
            userRegisterResponse.setUser_id(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));
            userRegisterResponse.setPassword(prefManager.getData(PrefManager.USER_PASSWORD));
            if (!Helper.isNetworkAvailable(this)) {
                Toast.makeText(this, R.string.error_connection, Toast.LENGTH_SHORT).show();
                return;
            }
            new HttpCall(this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    if (response != null) {
                        Gson gson = new Gson();
                        new PrefManager(ProfileDetails.this).put(PrefManager.USER_KEY, gson.toJson(response));
                        util.dismissProgressDialog();
                        Intent intent = new Intent(getApplicationContext(), PasscodeActivty.class);
                        intent.putExtra("checkPassword", false);
                        intent.putExtra("finish", false);
                        intent.putExtra("has_back", true);
                        startActivity(intent);
                    } else {
                        onFailed("response is null");

                    }
                }

                @Override
                public void onFailed(String error) {
                    Log.e("ProfileDetails", error);
                    util.dismissProgressDialog();
                    finish();
                }
            }).getProfile(userRegisterResponse);


        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.sorry_missing_data_please_contact_support), Toast.LENGTH_SHORT).show();
            util.dismissProgressDialog();
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void ImagePickerCallBack(Uri uri) {
        util.showProgressDialog();
        Log.e("ImageUri", uri != null ? uri.toString() : "Empty Uri");
        ImageHelper.setImage(imageProfile, uri);

        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                util.dismissProgressDialog();
                uploadImageResponse = (UploadImageResponse) response;
                Toast.makeText(ProfileDetails.this, R.string.upload_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getApplicationContext(), R.string.image_not_save_error_while_uploading, Toast.LENGTH_SHORT).show();
                util.dismissProgressDialog();
                imageProfile.setImageResource(R.drawable.profile_place_holder);
            }
        }).uploadImage(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), ImageFilePath.getPath(this, uri));

    }
}
