package com.germanitlab.kanonhealth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.adapters.SpecilaitiesAdapter;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.callback.Message;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.initialProfile.DialogPickerCallBacks;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.Table;
import com.germanitlab.kanonhealth.models.user.Info;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.nex3z.flowlayout.FlowLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddPractics extends AppCompatActivity implements Message<ChooseModel>, DialogPickerCallBacks {

    //Edit text
    @BindView(R.id.ed_location)
    EditText etLocation;
    @BindView(R.id.ed_street_name)
    EditText etStreetName;
    @BindView(R.id.ed_house_number)
    EditText etHouseNumber;
    @BindView(R.id.ed_zip_code)
    EditText etZipCode;
    @BindView(R.id.ed_city)
    EditText etCity;
    @BindView(R.id.ed_province)
    EditText etProvince;
    @BindView(R.id.ed_country)
    EditText etCountry;
    @BindView(R.id.ed_name)
    EditText etName;


    @BindView(R.id.et_telephone)
    EditText etTelephone;
    //Image view

    @BindView(R.id.img_edit_avatar)
    CircleImageView civImageAvatar;
    @BindView(R.id.edit_image)
    CircleImageView civEditImage;
    @BindView(R.id.edit_time_table)
    ImageView ivEditTimeTable;

    // Time table
    @BindView(R.id.no_time)
    LinearLayout llNo;
    @BindView(R.id.tv_no_time)
    TextView tvNoTime;
    @BindView(R.id.tablelayout)
    TableLayout tablelayout;
    @BindView(R.id.tv_specilities)
    TextView tvSpecilities;

    @BindView(R.id.tv_languages)
    TextView tvLanguages;
    @BindView(R.id.location_img)
    ImageView location_img;
    @BindView(R.id.flowlayout_speciality)
    FlowLayout flSpecilities ;

    // additional data
    User user;
    Info info;
    RecyclerView recyclerView;
    PickerDialog pickerDialog;
    PrefManager prefManager;
    private Uri selectedImageUri;
    Util util;
    UploadImageResponse uploadImageResponse;
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_PIC = 55;
    int PLACE_PICKER_REQUEST = 22;
    String practics_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_practics);
        ButterKnife.bind(this);
        initTB();
        user = new User();
        info = new Info();
        pickerDialog = new PickerDialog(true);
        prefManager = new PrefManager(this);
        util = Util.getInstance(this);
        handleImoAction();
        try {
            practics_id = getIntent().getExtras().getString("PRACTICS_ID");
        } catch (Exception e) {
            practics_id = "";
            Crashlytics.logException(e);
            Log.e("AddPractics", "onCreate: ", e);
            Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
        }
        if (practics_id != null) {
            bindData();
        }
    }

    private void bindData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.waiting_text);
        progressDialog.setCancelable(false);
        progressDialog.show();
        //Integer.parseInt(prefManager.getData(PrefManager.USER_ID))
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                try {
                    UserInfoResponse userInfoResponse = (UserInfoResponse) response;
                    user = userInfoResponse.getUser();
                    if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                        ImageHelper.setImage(civImageAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + user.getAvatar(), AddPractics.this);
                    }
                    etName.setText(user.getFullName());
                    etLocation.setText(user.getAddress());
                    etHouseNumber.setText(user.getInfo().getHouseNumber());
                    etZipCode.setText(user.getInfo().getZip_code());
                    etProvince.setText(user.getInfo().getProvinz());
                    etCountry.setText(user.getInfo().getCountry());
                    etTelephone.setText(user.getPhone());
                    if (user.getLocation_img() != null && !user.getLocation_img().isEmpty()) {
                        ImageHelper.setImage(location_img, Constants.CHAT_SERVER_URL_IMAGE + "/" + user.getLocation_img(), AddPractics.this);
                        location_img.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    onFailed(e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailed(String error) {
                Crashlytics.log(error);
                Toast.makeText(AddPractics.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }
        }).getDoctorId(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), practics_id);
    }

    private void initTB() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.new_toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_practice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.mi_save:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleImoAction() {
        etTelephone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                save();
                return true;
            }
        });
    }

    public void save() {
        try {
            if (!isvalid(etName) || !isvalid(etLocation) || !isvalid(etHouseNumber) || !isvalid(etZipCode) || !isvalid(etProvince) || !isvalid(etCountry))
                return;
            user.setFirst_name(etName.getText().toString());
            user.setAddress(etLocation.getText().toString());
            info.setHouseNumber(etHouseNumber.getText().toString());
            info.setZip_code(etZipCode.getText().toString());
            info.setProvinz(etProvince.getText().toString());
            info.setCountry(etCountry.getText().toString());
            info.setStreetname(etStreetName.getText().toString());
            info.setCity(etCity.getText().toString());
            user.setInfo(info);
            user.setPhone(etTelephone.getText().toString());
            user.setPassword(prefManager.getData(PrefManager.USER_PASSWORD));
            if (practics_id == null) {
                user.setUserID_request(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));
                user.setId(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));
                new HttpCall(this, new ApiResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        Toast.makeText(AddPractics.this, R.string.save_practics, Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailed(String error) {
                        Log.e("Add Practics Add", error);
                        Toast.makeText(AddPractics.this, error, Toast.LENGTH_LONG).show();
                    }
                }).addClinic(user);
            } else {
                user.setUserID_request(Integer.valueOf(practics_id));
                user.setId(Integer.valueOf(practics_id));
                new HttpCall(this, new ApiResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        Toast.makeText(AddPractics.this, R.string.edit_practics, Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailed(String error) {
                        Log.e("Add Practics Edit", error);
                        Toast.makeText(AddPractics.this, error, Toast.LENGTH_LONG).show();
                    }
                }).editClinic(user);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.edit_speciality_list)
    public void editSpecialityList(View view) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Constants", Constants.SPECIALITIES);
            bundle.putSerializable(Constants.CHOSED_LIST, (Serializable) user.getSpecialities());
            showDialogFragment(bundle);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.edit_languages_list)
    public void edit_languages_list() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Constants", Constants.LANGUAUGE);
            bundle.putSerializable(Constants.CHOSED_LIST, (Serializable) user.getSupported_lang());
            showDialogFragment(bundle);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.locationonmap)
    public void locationonmap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            askForLocationPermission();
            return;
        }
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(AddPractics.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void askForLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, Constants.LAST_LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //resume tasks needing this
                switch (requestCode) {
                    case Constants.LAST_LOCATION_PERMISSION_CODE:
                        locationonmap();
                        break;
                }
            }
        }
    }

    @OnClick(R.id.member_text)
    public void edit_member_list() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Constants", Constants.DoctorAll);
            bundle.putSerializable(Constants.CHOSED_LIST, (Serializable) user.getMembers_at());
            showDialogFragment(bundle);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.edit_image)
    public void onEditProfileImageClicked() {
        try {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {


                pickerDialog.show(getFragmentManager(), "imagePickerDialog");
            } else {
                askForPermission(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA},
                        Constants.GALLERY_PERMISSION_CODE);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    private void askForPermission(String[] permission, Integer requestCode) {
        ActivityCompat.requestPermissions(this, permission, requestCode);
    }

    @OnClick(R.id.edit_time_table)
    public void editTimeTable(View view) {
        try {
            Intent intent = new Intent(this, TimeTable.class);
            intent.putExtra(Constants.DATA, (Serializable) user.getOpen_time());
            startActivityForResult(intent, Constants.HOURS_CODE);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void showDialogFragment(Bundle bundle) {
        MultiChoiseListFragment dialogFragment = new MultiChoiseListFragment();
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "list");
    }

    @Override
    public void Response(ArrayList<ChooseModel> specialitiesArrayList, int type) {
        try {
            ArrayList<ChooseModel> templist = new ArrayList<>();
            switch (type) {
                case Constants.SPECIALITIES:
                    user.getSpecialities().clear();
                    for (ChooseModel item : specialitiesArrayList) {
                        if (item.getIsMyChoise())
                            templist.add(item);
                    }
                    user.setSpecialities(templist);

                    tvSpecilities.setText("");
                    int size = 0;
                    for (ChooseModel speciality : user.getSpecialities()) {
                        tvSpecilities.append(speciality.getSpeciality_title());
                        size++;
                        if (size < user.getSpecialities().size())
                            tvSpecilities.append(", ");
                    }

              //      setRecyclerView(templist, R.id.speciality_recycleview, LinearLayoutManager.HORIZONTAL, Constants.SPECIALITIES);
                    setSpecilites(user.getSpecialities());

                    break;
                case Constants.LANGUAUGE:
                    user.getSupported_lang().clear();
                    for (ChooseModel item : specialitiesArrayList) {
                        if (item.getIsMyChoise())
                            templist.add(item);
                    }
                    user.setSupported_lang(templist);
                    tvLanguages.setText("");
                    for (ChooseModel lang : user.getSupported_lang()) {
                        tvLanguages.append(lang.getLang_title() + " ");
                    }
                    setRecyclerView(templist, R.id.language_recycleview, LinearLayoutManager.HORIZONTAL, Constants.LANGUAUGE);
                    break;
                case Constants.MEMBERAT:
                case Constants.DoctorAll:
                    user.getMembers_at().clear();
                    for (ChooseModel item : specialitiesArrayList) {
                        if (item.getIsMyChoise())
                            templist.add(item);
                    }
                    user.setMembers_at(templist);
                    setRecyclerView(templist, R.id.member_recycleview, LinearLayoutManager.VERTICAL, Constants.MEMBERAT);

                    break;
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void setSpecilites(List<ChooseModel> specialities) {
        flSpecilities.removeAllViews();
        for (ChooseModel chooseModel: specialities
             ) {
            flSpecilities.addView(ImageHelper.setImageCircle(chooseModel.getSpeciality_icon() , getApplicationContext()));

        }
    }



    public void setRecyclerView(List<ChooseModel> list, int id, int linearLayoutManager, int type) {
        if (recyclerView == null)
            new RecyclerView(getApplicationContext());
        SpecilaitiesAdapter adapter = new SpecilaitiesAdapter(list, getApplicationContext(), type);
        recyclerView = (RecyclerView) findViewById(id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, linearLayoutManager, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case Constants.IMAGE_REQUEST:
                        selectedImageUri = data.getData();
                        prefManager.put(PrefManager.PROFILE_IMAGE, selectedImageUri.toString());
                        util.showProgressDialog();
                        Log.e("ImageUri", selectedImageUri != null ? selectedImageUri.toString() : "Empty Uri");
                        ImageHelper.setImage(civImageAvatar, selectedImageUri, AddPractics.this);

                        new HttpCall(this, new ApiResponse() {
                            @Override
                            public void onSuccess(Object response) {
                                util.dismissProgressDialog();
                                uploadImageResponse = (UploadImageResponse) response;
                                user.setAvatar(uploadImageResponse.getFile_url());
                                Log.e("After Casting", uploadImageResponse.getFile_url());
                                prefManager.put(PrefManager.PROFILE_IMAGE, uploadImageResponse.getFile_url());
                            }

                            @Override
                            public void onFailed(String error) {
                                util.dismissProgressDialog();
                                Toast.makeText(AddPractics.this, getResources().getText(R.string.error_saving_data), Toast.LENGTH_SHORT).show();
                                Log.e("upload image failed :", error);
                            }
                        }).uploadImage(prefManager.getData(PrefManager.USER_ID)
                                , prefManager.getData(PrefManager.USER_PASSWORD), ImageFilePath.getPath(this, selectedImageUri));
                        pickerDialog.dismiss();

                        break;
                    case CROP_PIC :
                        afterCropFinish();
                        break;
                    case TAKE_PICTURE:
                        util.showProgressDialog();
                        Log.e("ImageUri", selectedImageUri != null ? selectedImageUri.toString() : "Empty Uri");
                        performCrop();
                        break;
                    case Constants.HOURS_CODE:
                        user.setOpen_time((List<Table>) data.getSerializableExtra(Constants.DATA));
                        user.setOpen_Type(data.getIntExtra("type", 0));
                        getTimaTableData(user.getOpen_time());
                        break;
                    case Constants.HOURS_TYPE_CODE:
                        user.setOpen_Type(data.getIntExtra("type", 0));
                        break;
                }
            } else if (requestCode == PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    user.setLocation_lat(place.getLatLng().latitude);
                    user.setLocation_long(place.getLatLng().longitude);
                }
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }

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

    private void afterCropFinish() {
        prefManager.put(PrefManager.PROFILE_IMAGE, selectedImageUri.toString());
        ImageHelper.setImage(civImageAvatar, selectedImageUri, AddPractics.this);
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                util.dismissProgressDialog();
                uploadImageResponse = (UploadImageResponse) response;
                user.setAvatar(uploadImageResponse.getFile_url());
                Log.e("After Casting", uploadImageResponse.getFile_url());
            }

            @Override
            public void onFailed(String error) {
                util.dismissProgressDialog();
                Toast.makeText(AddPractics.this, getResources().getText(R.string.error_saving_data), Toast.LENGTH_SHORT).show();
                Log.e("upload image failed :", error);
            }
        }).uploadImage(prefManager.getData(PrefManager.USER_ID)
                , prefManager.getData(PrefManager.USER_PASSWORD), ImageFilePath.getPath(this, selectedImageUri));
        pickerDialog.dismiss();
    }

    private void getTimaTableData(List<Table> list) {
        if (user.getOpen_Type() == 3)
            tvNoTime.setText(R.string.permenant_closed);
        else
            tvNoTime.setText(R.string.always_open);

        if (list.size() > 0) {
            llNo.setVisibility(View.GONE);
            tablelayout.removeAllViews();
            com.germanitlab.kanonhealth.helpers.TimeTable timeTable = new com.germanitlab.kanonhealth.helpers.TimeTable();
            timeTable.creatTimeTable(list, this, tablelayout);
        } else
            llNo.setVisibility(View.VISIBLE);

    }

    @Override
    public void onGalleryClicked(Intent intent) {
        startActivityForResult(intent, Constants.IMAGE_REQUEST);

    }

    @Override
    public void onCameraClicked() {
        takeImageWithCamera();
    }

    @Override
    public void deleteMyImage() {
        try {
            user.setAvatar("");
            ImageHelper.setImage(civImageAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + user.getAvatar(), R.drawable.placeholder, this);
            prefManager.put(PrefManager.PROFILE_IMAGE, "");
            pickerDialog.dismiss();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
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

    /* Get the real path from the URI
    public String getPathFromURI(Uri contentUri) {
        String path;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            path = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            if(idx==-1)
                path = contentUri.getPath();
            else
                path = cursor.getString(idx);
            cursor.close();
        }
        return path;
    }*/

    private boolean isvalid(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError(AddPractics.this.getString(R.string.please_fill_data));
            return false;
        } else
            return true;
    }

    @OnClick(R.id.location_img)
    public void openMap() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("long", user.getLocation_long());
        intent.putExtra("lat", user.getLocation_lat());
        startActivity(intent);
    }
}
