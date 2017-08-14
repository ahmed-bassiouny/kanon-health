package com.germanitlab.kanonhealth.profile;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.Crop.PickerBuilder;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.adapters.EditQuestionAdapter;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.custom.FixedHoloDatePickerDialog;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.DateHelper;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.initialProfile.DialogPickerCallBacks;
import com.germanitlab.kanonhealth.initialProfile.ExifUtils;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.models.user.Info;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Geram IT Lab on 21/02/2017.
 */

public class EditUserProfileActivity extends ParentActivity implements Serializable, DialogPickerCallBacks {

    private static final int CROP_PIC = 5;

    private EditQuestionAdapter mAdapter;

    @BindView(R.id.first_name)
    EditText etFirstName;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.last_name)
    EditText etLastName;
    @BindView(R.id.et_edit_mobile_number)
    EditText etPhone;
    @BindView(R.id.et_edit_country_code)
    EditText etCountryCode;
    @BindView(R.id.et_edit_birthday)
    TextView etBirthday;
    @BindView(R.id.et_edit_streat)
    EditText etStreet;
    @BindView(R.id.et_edit_house_num)
    EditText etHousePhone;
    @BindView(R.id.et_edit_zip_code)
    EditText etZip;
    @BindView(R.id.et_edit_proviz)
    EditText etProvinz;
    @BindView(R.id.et_edit_country)
    EditText etCountry;
    @BindView(R.id.img_edit_avatar)
    ImageView imgAvatar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    UserInfo userInfo;
    Info info;
    PrefManager prefManager;
    Uri imageUri;
    PickerDialog pickerDialog;
    private static final int TAKE_PICTURE = 1;
    Util util;
    Helper helper;


    LinkedHashMap<String, String> questionAnswer;

    Calendar myCalendar = Calendar.getInstance();
    File avatar=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_edit_profile);
        try {
            prefManager = new PrefManager(this);
            pickerDialog = new PickerDialog(true);
            util = Util.getInstance(this);
            ButterKnife.bind(this);
            etFirstName.setSelected(false);
            Intent i = getIntent();
            userInfo = (UserInfo) i.getSerializableExtra("userInfoResponse");
            bindData();
            createAdapter();

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }

    }

    public void createAdapter() {
        mAdapter = new EditQuestionAdapter(questionAnswer, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    public ArrayList<String> iterateOverRecyclerView() {
        int count = recyclerView.getChildCount();
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (recyclerView.findViewHolderForLayoutPosition(i) instanceof EditQuestionAdapter.MyViewHolder) {
                EditQuestionAdapter.MyViewHolder myViewHolder = (EditQuestionAdapter.MyViewHolder) recyclerView.findViewHolderForLayoutPosition(i);
                strings.add(myViewHolder.tv_answer.getText().toString());
                Log.e("Answer", myViewHolder.tv_answer.getText().toString());
            }
        }
        return strings;
    }

    private void bindData() {
        if (userInfo.getAvatar() != null && !userInfo.getAvatar().isEmpty())
            ImageHelper.setImage(imgAvatar, ApiHelper.SERVER_IMAGE_URL + "/" + userInfo.getAvatar(),R.drawable.profile_place_holder);

        etLastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                etBirthday.performClick();
                return true;
            }
        });

        etFirstName.setText(userInfo.getFirstName());
        etLastName.setText(userInfo.getLastName());
        etTitle.setText(userInfo.getTitle());
        etCountryCode.setText(userInfo.getCountry_code());
        etPhone.setText(userInfo.getPhone());
        etBirthday.setText(userInfo.getBirthday().toString());
        etStreet.setText(userInfo.getStreetName());
        etHousePhone.setText(userInfo.getHouseNumber());
        etZip.setText(userInfo.getZipCode());
        etProvinz.setText(userInfo.getProvidence());
        questionAnswer = userInfo.getQuestionsAnswers();
        etCountry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                submit();
                return true;
            }
        });
    }

    private void askForPermission(String[] permission, Integer requestCode) {
        ActivityCompat.requestPermissions(this, permission, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        try {
//            if (imageUri != null)
//                outState.putString("imageURI", imageUri.toString());
//            setUserObject();
//            outState.putSerializable("userdata", userInfo);
//            super.onSaveInstanceState(outState);
//        } catch (Exception e) {
//            Crashlytics.logException(e);
//            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
//        }
//
//    }

    @Override
    public void onBackPressed() {
        try {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("userInfoResponse", userInfo);
            i.putExtra("from", false);
            startActivity(i);
            finish();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick(R.id.et_edit_birthday)
    public void viewBirthdate(View v) {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (helper == null)
            helper = new Helper(this);
        helper.showDatePicker(etBirthday);

    }

    @OnClick(R.id.img_edit)
    public void onEditProfileImageClicked() {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    @OnClick(R.id.btn_edit_save)
    public void submit() {
      setUserObject();

    }

    private void setUserObject() {
        userInfo.setFirstName(etFirstName.getText().toString());
        userInfo.setLastName(etLastName.getText().toString());
        userInfo.setTitle(etTitle.getText().toString());
        userInfo.setPhone(etPhone.getText().toString());
        userInfo.setBirthday(etBirthday.getText().toString());
        userInfo.setStreetName(etStreet.getText().toString());
        userInfo.setZipCode(etZip.getText().toString());
        userInfo.setHouseNumber(etHousePhone.getText().toString());
        userInfo.setProvidence(etProvinz.getText().toString());
        //userInfo.setCountry(etCountry.getText().toString());
        ArrayList<String> answers = iterateOverRecyclerView();
        Set<String> questions = questionAnswer.keySet();
        ArrayList<String> questionsArray = new ArrayList<>(questions);
        for (int i = 0; i < answers.size(); i++) {
            questionAnswer.put(questionsArray.get(i), answers.get(i));
        }
        userInfo.setQuestionsAnswers(questionAnswer);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result =ApiHelper.editPatient(EditUserProfileActivity.this,userInfo,avatar);
                if(result){
                    EditUserProfileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EditUserProfileActivity.this, R.string.upload_success, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(EditUserProfileActivity.this, ProfileActivity.class);
                            i.putExtra("userInfoResponse", userInfo);
                            i.putExtra("from", false);
                            startActivity(i);
                            finish();
                        }
                    });
                }else{
                    EditUserProfileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EditUserProfileActivity.this, R.string.upload_failed, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }).start();
    }


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
            userInfo.setAvatar("");
            ImageHelper.setImage(imgAvatar, ApiHelper.SERVER_IMAGE_URL+ "/" + "", R.drawable.profile_place_holder);
            prefManager.put(PrefManager.PROFILE_IMAGE, "");
            avatar=null;
            pickerDialog.dismiss();
    }

    @Override
    public void ImagePickerCallBack(Uri uri) {
        prefManager.put(PrefManager.PROFILE_IMAGE, uri.toString());
        ImageHelper.setImage(imgAvatar, uri);
        avatar = new File(ImageFilePath.getPath(EditUserProfileActivity.this, uri));
        pickerDialog.dismiss();
    }
}
