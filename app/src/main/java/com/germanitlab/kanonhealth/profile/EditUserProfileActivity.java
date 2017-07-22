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
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.adapters.EditQuestionAdapter;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.custom.FixedHoloDatePickerDialog;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.DateUtil;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.initialProfile.DialogPickerCallBacks;
import com.germanitlab.kanonhealth.initialProfile.ExifUtils;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.Info;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;

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

public class EditUserProfileActivity extends AppCompatActivity implements Serializable, ApiResponse, DialogPickerCallBacks {

    private static final int CROP_PIC = 5;
    private UserInfoResponse userInfoResponse;

    private EditQuestionAdapter mAdapter;

    @BindView(R.id.first_name)
    EditText etFirstName;
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
    User user;
    Info info;
    PrefManager prefManager;
    Uri imageUri;
    PickerDialog pickerDialog;
    private String birthdate;
    private Uri selectedImageUri;
    private static final int TAKE_PICTURE = 1;
    Util util ;


    UploadImageResponse uploadImageResponse;

    LinkedHashMap<String, String> questionAnswer;

    Calendar myCalendar = Calendar.getInstance();


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
            userInfoResponse = (UserInfoResponse) i.getSerializableExtra("userInfoResponse");

            user = userInfoResponse.getUser();
            Log.e("My user ", String.valueOf(user.get_Id()));
            info = user.getInfo();
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
        if(userInfoResponse.getUser().getAvatar()!=null &&!userInfoResponse.getUser().getAvatar().isEmpty())
        ImageHelper.setImage(imgAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + userInfoResponse.getUser().getAvatar(), this);

        etLastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                etBirthday.performClick();
                return true;
            }
        });

        etFirstName.setText(userInfoResponse.getUser().getFirst_name());
        etLastName.setText(userInfoResponse.getUser().getLast_name());
        etCountryCode.setText(userInfoResponse.getUser().getCountryCOde());
        birthdate = userInfoResponse.getUser().getBirth_date();
        etPhone.setText(userInfoResponse.getUser().getPhone());
        try {
            Date parseDate = DateUtil.getAnotherFormat().parse(userInfoResponse.getUser().getBirth_date().toString());
            String s = (DateUtil.formatBirthday(parseDate.getTime()));
            Log.d("my converted date", s);
            etBirthday.setText(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        etStreet.setText(userInfoResponse.getUser().getInfo().getStreetname());
        etHousePhone.setText(userInfoResponse.getUser().getInfo().getHouseNumber());
        etZip.setText(userInfoResponse.getUser().getInfo().getZip_code());
        etProvinz.setText(userInfoResponse.getUser().getInfo().getProvinz());
        etCountry.setText(userInfoResponse.getUser().getInfo().getCountry());
        questionAnswer = userInfoResponse.getUser().getQuestions();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            if (imageUri != null)
                outState.putString("imageURI", imageUri.toString());
            setUserObject();
            outState.putSerializable("userdata", userInfoResponse);
            super.onSaveInstanceState(outState);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            pickerDialog.dismiss();
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case Constants.IMAGE_REQUEST:
                        selectedImageUri = data.getData();
                        prefManager.put(PrefManager.PROFILE_IMAGE, selectedImageUri.toString());
                        util.showProgressDialog();
                        ImageHelper.setImage(imgAvatar, selectedImageUri, this);
                        new HttpCall(this, new ApiResponse() {
                            @Override
                            public void onSuccess(Object response) {
                                try {
                                    util.dismissProgressDialog();
                                    uploadImageResponse = (UploadImageResponse) response;
                                    user.setAvatar(uploadImageResponse.getFile_url());
                                } catch (Exception e) {
                                    Crashlytics.logException(e);
                                    util.dismissProgressDialog();
                                    Toast.makeText(getApplicationContext(), "image not save error while uploading", Toast.LENGTH_SHORT).show();
                                    imgAvatar.setImageResource(R.drawable.profile_place_holder);

                                }

                            }

                            @Override
                            public void onFailed(String error) {
                                util.dismissProgressDialog();
                                Toast.makeText(getApplicationContext(), "image not save error while uploading", Toast.LENGTH_SHORT).show();
                                imgAvatar.setImageResource(R.drawable.profile_place_holder);

                            }
                        }).uploadImage(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), ImageFilePath.getPath(this, selectedImageUri));

                        break;
                    /*case CROP_PIC :
                        afterCropFinish();
                        break;*/
                    case TAKE_PICTURE:
                        util.showProgressDialog();
                        prefManager.put(PrefManager.PROFILE_IMAGE, selectedImageUri.toString());
                        ImageHelper.setImage(imgAvatar, selectedImageUri, this);
                        new HttpCall(this, new ApiResponse() {
                            @Override
                            public void onSuccess(Object response) {
                                try {
                                    util.dismissProgressDialog();
                                    uploadImageResponse = (UploadImageResponse) response;
                                    user.setAvatar(uploadImageResponse.getFile_url());
                                } catch (Exception e) {
                                    Crashlytics.logException(e);
                                    util.dismissProgressDialog();
                                    Toast.makeText(getApplicationContext(), "image not save error while uploading", Toast.LENGTH_SHORT).show();
                                    imgAvatar.setImageResource(R.drawable.profile_place_holder);

                                }

                            }

                            @Override
                            public void onFailed(String error) {
                                util.dismissProgressDialog();
                                Toast.makeText(getApplicationContext(), "image not save error while uploading", Toast.LENGTH_SHORT).show();
                                imgAvatar.setImageResource(R.drawable.profile_place_holder);
                            }
                        }).uploadImage(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), ImageFilePath.getPath(this, selectedImageUri));

                        break;
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }
    }

    private void afterCropFinish() {
        prefManager.put(PrefManager.PROFILE_IMAGE, selectedImageUri.toString());
        ImageHelper.setImage(imgAvatar, imageUri, this);
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                try {
                    util.dismissProgressDialog();
                    uploadImageResponse = (UploadImageResponse) response;
                    user.setAvatar(uploadImageResponse.getFile_url());
                    Log.e("After Casting", uploadImageResponse.getFile_url());
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        }).uploadImage(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), ImageFilePath.getPath(this, selectedImageUri));

    }


    @Override
    public void onSuccess(Object response) {
        try {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("userInfoResponse", userInfoResponse);
            i.putExtra("from", false);
            startActivity(i);
            finish();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onFailed(String error) {
        Log.d("Update User1 failes", "on Failed");
        Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_saving_data), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        try {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("userInfoResponse", userInfoResponse);
            i.putExtra("from", false);
            startActivity(i);
            finish();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
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
            etBirthday.setText(s);
        }
    };

    @OnClick(R.id.et_edit_birthday)
    public void viewBirthdate(View v) {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            Calendar calender = Calendar.getInstance();
       /* Dialog mDialog = new DatePickerDialog(EditUserProfileActivity.this,
                android.R.style.Theme_Holo_Light_Dialog,
                mDateSetListener, calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH), calender
                .get(Calendar.DAY_OF_MONTH));

        mDialog.show();*/
            final Context themedContext = new ContextThemeWrapper(
                    EditUserProfileActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog
            );

            final DatePickerDialog mDialog = new FixedHoloDatePickerDialog(
                    themedContext,
                    mDateSetListener,
                    calender.get(Calendar.YEAR),
                    calender.get(Calendar.MONTH),
                    calender.get(Calendar.DAY_OF_MONTH));

            mDialog.show();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


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
/*        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image*//*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image*//*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, Constants.IMAGE_REQUEST);
        } else {
            askForPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.GALLERY_PERMISSION_CODE);
        }*/
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    public void takeImageWithCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, getString(R.string.new_picture));
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, getString(R.string.from_your_camera));
        selectedImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        startActivityForResult(intent, TAKE_PICTURE);
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
        try {
            setUserObject();
            new HttpCall(this, this).editProfile(user);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void setUserObject() {
        user.setFirst_name(etFirstName.getText().toString());
        user.setLast_name(etLastName.getText().toString());
        user.setPlatform("3");
        user.setPhone(etPhone.getText().toString());
        user.setBirthDate(birthdate);
        Log.d("my birthdate format", etBirthday.getText().toString());
        info.setStreetname(etStreet.getText().toString());
        info.setZip_code(etZip.getText().toString());
        info.setHouseNumber(etHousePhone.getText().toString());
        info.setProvinz(etProvinz.getText().toString());
        info.setCountry(etCountry.getText().toString());
        ArrayList<String> answers = iterateOverRecyclerView();
        Set<String> questions = questionAnswer.keySet();
        ArrayList<String> questionsArray = new ArrayList<>(questions);
        for (int i = 0; i < answers.size(); i++) {
            questionAnswer.put(questionsArray.get(i), answers.get(i));
        }

        user.setInfo(info);
    }

    public void decodeFile(String filePath) {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap b1 = BitmapFactory.decodeFile(filePath, o2);
        Bitmap b = ExifUtils.rotateBitmap(filePath, b1);

        // image.setImageBitmap(bitmap);
    }


    @Override
    public void onGalleryClicked(Intent intent) {
        startActivityForResult(intent, Constants.IMAGE_REQUEST);
    }

    @Override
    public void onCameraClicked() {
        takeImageWithCamera();
    }
    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(selectedImageUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, R.string.this_device_doesnot_support_the_crop_action, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void deleteMyImage() {
        try {
            user.setAvatar("");
            ImageHelper.setImage(imgAvatar, Constants.CHAT_SERVER_URL + "/" + userInfoResponse.getUser().getAvatar(), R.drawable.profile_place_holder, this);
            prefManager.put(PrefManager.PROFILE_IMAGE, "");
            pickerDialog.dismiss();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }
}