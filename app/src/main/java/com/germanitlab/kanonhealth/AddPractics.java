package com.germanitlab.kanonhealth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.germanitlab.kanonhealth.Crop.PickerBuilder;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Clinic;
import com.germanitlab.kanonhealth.api.models.Language;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.callback.Message;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.initialProfile.DialogPickerCallBacks;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.Table;
import com.germanitlab.kanonhealth.models.user.Info;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.mukesh.countrypicker.Country;
import com.nex3z.flowlayout.FlowLayout;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddPractics extends ParentActivity implements Message , DialogPickerCallBacks {

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
    @BindView(R.id.fl_language)
    FlowLayout flLanguages;


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
    FlowLayout flSpecilities;
    @BindView(R.id.flowlayout_invite)
    FlowLayout flInvite;

    // additional data
    Info info;
    RecyclerView recyclerView;
    PickerDialog pickerDialog;
    PrefManager prefManager;
    private PrefManager mPrefManager;
    //    private Uri selectedImageUri;
    Util util;
    UserInfo user;
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_PIC = 55;
    int PLACE_PICKER_REQUEST = 22;
    String practics_id = "";
    Clinic clinic;
    String specialityIds;
    String langIds;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_practics);
        ButterKnife.bind(this);
        initTB();
        mPrefManager = new PrefManager(this);
        prefManager = new PrefManager(this);
        try {
            user = new Gson().fromJson(mPrefManager.getData(PrefManager.USER_KEY), UserInfo.class);
        } catch (Exception e) {
        }

        clinic = new Clinic();
        pickerDialog = new PickerDialog(true);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                clinic = ApiHelper.postGetClinic(Integer.valueOf(practics_id), getApplicationContext());
                if (clinic != null) {
                    if (clinic.getAvatar() != null && !clinic.getAvatar().isEmpty()) {
                        ImageHelper.setImage(civImageAvatar, ApiHelper.SERVER_IMAGE_URL + "/" + clinic.getAvatar());
                    }
                    etName.setText(clinic.getName());
                    etLocation.setText(clinic.getAddress());
                    etHouseNumber.setText(clinic.getHouseNumber());
                    etZipCode.setText(clinic.getZipCode());
                    etProvince.setText(clinic.getProvince());
                    etCountry.setText(clinic.getCountry());
                    etTelephone.setText(clinic.getPhone());
                    //--------------------------------------------------------------------------location------------------------------------------------------//
//                    if (clinic.get != null && !user.getLocation_img().isEmpty()) {
//                        ImageHelper.setImage(location_img, Constants.CHAT_SERVER_URL_IMAGE + "/" + user.getLocation_img());
//                        location_img.setVisibility(View.VISIBLE);
//                    }
                } else {
                    Toast.makeText(AddPractics.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressDialog.dismiss();
            }
        }).start();


//
//        new HttpCall(this, new ApiResponse() {
//            @Override
//            public void onSuccess(Object response) {
//                try {
//                    UserInfoResponse userInfoResponse = (UserInfoResponse) response;
//                    user = userInfoResponse.getUser();
//                    if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
//                        ImageHelper.setImage(civImageAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + user.getAvatar());
//                    }
//                    etName.setText(user.getFullName());
//                    etLocation.setText(user.getAddress());
//                    etHouseNumber.setText(user.getInfo().getHouseNumber());
//                    etZipCode.setText(user.getInfo().getZip_code());
//                    etProvince.setText(user.getInfo().getProvinz());
//                    etCountry.setText(user.getInfo().getCountry());
//                    etTelephone.setText(user.getPhone());
//                    if (user.getLocation_img() != null && !user.getLocation_img().isEmpty()) {
//                        ImageHelper.setImage(location_img, Constants.CHAT_SERVER_URL_IMAGE + "/" + user.getLocation_img());
//                        location_img.setVisibility(View.VISIBLE);
//                    }
//                    progressDialog.dismiss();
//                } catch (Exception e) {
//                    onFailed(e.getLocalizedMessage());
//                }
//            }
//
//            @Override
//            public void onFailed(String error) {
//                Crashlytics.log(error);
//                Toast.makeText(AddPractics.this, R.string.error_message, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//                finish();
//            }
//        }).getDoctorId(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), practics_id);
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
        // try {
        if (!isvalid(etName) || !isvalid(etLocation) || !isvalid(etHouseNumber) || !isvalid(etZipCode) || !isvalid(etProvince) || !isvalid(etCountry))
            return;

        if (practics_id != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Integer result = ApiHelper.postEditClinic(Integer.valueOf(practics_id), etName.getText().toString(), specialityIds, etStreetName.getText().toString(), etHouseNumber.getText().toString(), etZipCode.getText().toString(), etCity.getText().toString(), etProvince.getText().toString(), etCountry.getText().toString(), etTelephone.getText().toString(), langIds, file, getApplicationContext());
                    if (result != -1) {
                        Toast.makeText(AddPractics.this, R.string.edit_practics, Toast.LENGTH_LONG).show();
                        finish();
                    }

                }
            }).start();
        } else {
            Clinic clinic = ApiHelper.postAddClinic(user.getUserID(), etName.getText().toString(), specialityIds, etStreetName.getText().toString(), etHouseNumber.getText().toString(), etZipCode.getText().toString(), etCity.getText().toString(), etProvince.getText().toString(), etCountry.getText().toString(), etTelephone.getText().toString(), langIds, file, getApplicationContext());

            if (clinic != null) {
                Toast.makeText(AddPractics.this, R.string.save_practics, Toast.LENGTH_LONG).show();
                finish();
            }
        }

//
//
//            user.setFirst_name(etName.getText().toString());
//            user.setAddress(etLocation.getText().toString());
//            info.setHouseNumber(etHouseNumber.getText().toString());
//            info.setZip_code(etZipCode.getText().toString());
//            info.setProvinz(etProvince.getText().toString());
//            info.setCountry(etCountry.getText().toString());
//            info.setStreetname(etStreetName.getText().toString());
//            info.setCity(etCity.getText().toString());
//            user.setInfo(info);
//            user.setPhone(etTelephone.getText().toString());
//            user.setPassword(prefManager.getData(PrefManager.USER_PASSWORD));
//            if (practics_id == null) {
//                user.setUserID_request(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));
//                user.setId(Integer.parseInt(prefManager.getData(PrefManager.USER_ID)));
//                new HttpCall(this, new ApiResponse() {
//                    @Override
//                    public void onSuccess(Object response) {
//                        Toast.makeText(AddPractics.this, R.string.save_practics, Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailed(String error) {
//                        Log.e("Add Practics Add", error);
//                        Toast.makeText(AddPractics.this, error, Toast.LENGTH_LONG).show();
//                    }
//                }).addClinic(user);
//            } else {
//                user.setUserID_request(Integer.valueOf(practics_id));
//                user.setId(Integer.valueOf(practics_id));
//                new HttpCall(this, new ApiResponse() {
//                    @Override
//                    public void onSuccess(Object response) {
//                        Toast.makeText(AddPractics.this, R.string.edit_practics, Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailed(String error) {
//                        Log.e("Add Practics Edit", error);
//                        Toast.makeText(AddPractics.this, error, Toast.LENGTH_LONG).show();
//                    }
//                }).editClinic(user);
//            }
//        } catch (Exception e) {
//            Crashlytics.logException(e);
//            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
//            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
//        }


    }

    @OnClick(R.id.edit_speciality_list)
    public void editSpecialityList(View view) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Constants", Constants.SPECIALITIES);
            bundle.putSerializable(Constants.CHOSED_LIST, clinic.getSpeciality());
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
            bundle.putSerializable(Constants.CHOSED_LIST, clinic.getSupportedLangs());
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

    @OnClick(R.id.edit_invite_doctor)
    public void edit_member_list() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Constants", Constants.DoctorAll);
            bundle.putSerializable(Constants.CHOSED_LIST, clinic.getDoctors());
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
    ////----------------------------------------------------------------------------working hours----------------------------------------------///

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

//    @Override
//    public void Response(ArrayList<ChooseModel> specialitiesArrayList, int type) {
//        try {
//            ArrayList<ChooseModel> templist = new ArrayList<>();
//            switch (type) {
//                case Constants.SPECIALITIES:
//                    user.getSpecialities().clear();
//                    for (ChooseModel item : specialitiesArrayList) {
//                        if (item.getIsMyChoise())
//                            templist.add(item);
//                    }
//                    user.setSpecialities(templist);
//
//                    tvSpecilities.setText("");
//                    int size = 0;
//                    for (ChooseModel speciality : user.getSpecialities()) {
//                        tvSpecilities.append(speciality.getSpeciality_title());
//                        size++;
//                        if (size < user.getSpecialities().size())
//                            tvSpecilities.append(", ");
//                    }
//
//                    //      setRecyclerView(templist, R.id.speciality_recycleview, LinearLayoutManager.HORIZONTAL, Constants.SPECIALITIES);
//                    setCircles(user.getSpecialities(), flSpecilities, 1);
//
//                    break;
//                case Constants.LANGUAUGE:
//                    user.getSupported_lang().clear();
//                    for (ChooseModel item : specialitiesArrayList) {
//                        if (item.getIsMyChoise())
//                            templist.add(item);
//                    }
//                    user.setSupported_lang(templist);
//                    tvLanguages.setText("");
//                    setImage(user.getSupported_lang(), flLanguages, 0);
//                    //  setRecyclerView(templist, R.id.language_recycleview, LinearLayoutManager.HORIZONTAL, Constants.LANGUAUGE);
//                    break;
//                case Constants.MEMBERAT:
//                case Constants.DoctorAll:
//                    user.getMembers_at().clear();
//                    for (ChooseModel item : specialitiesArrayList) {
//                        if (item.getIsMyChoise())
//                            templist.add(item);
//                    }
//                    user.setMembers_at(templist);
//                    //     setRecyclerView(templist, R.id.member_recycleview, LinearLayoutManager.VERTICAL, Constants.MEMBERAT);
//                    setCircles(templist, flInvite, 2);
//                    break;
//            }
//        } catch (Exception e) {
//            Crashlytics.logException(e);
//            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
//            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
//        }
//
//    }

    private void setImage(List<ChooseModel> supported_lang, FlowLayout flowLayout, int i) {
        flowLayout.removeAllViews();
        tvLanguages.setText("");
        int size = 0;
        for (ChooseModel chooseModel : supported_lang) {
            String country_code = chooseModel.getCountry_code();
            if (!TextUtils.isEmpty(country_code)) {
                tvLanguages.append(chooseModel.getLang_title());
                if (supported_lang.size() > size + 1) {
                    tvLanguages.append(" , ");
                    size++;
                }
                Country country = Country.getCountryByISO(country_code);
                if (country != null) {
                    flowLayout.addView(ImageHelper.setImageHeart(country.getFlag(), getApplicationContext()));
                } else {
                    Log.d("Country " + chooseModel.getLang_title().toString(), "code " + chooseModel.getCountry_code().toString());
                }
            }
        }
    }


    private void setCircles(List<ChooseModel> specialities, FlowLayout flowLayout, int type) {
        // type = 1 for specialities , type = 2 for members
        flowLayout.removeAllViews();
        for (ChooseModel chooseModel : specialities
                ) {
            if (type == 1)
                flowLayout.addView(ImageHelper.setImageCircle(chooseModel.getSpeciality_icon(), this));
            else if (type == 2)
                flowLayout.addView(ImageHelper.setImageCircle(chooseModel.getAvatarMember(), this));

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
//                    case Constants.IMAGE_REQUEST:
//                        selectedImageUri = data.getData();
//                        prefManager.put(PrefManager.PROFILE_IMAGE, selectedImageUri.toString());
//                        util.showProgressDialog();
//                        Log.e("ImageUri", selectedImageUri != null ? selectedImageUri.toString() : "Empty Uri");
//                        ImageHelper.setImage(civImageAvatar, selectedImageUri, AddPractics.this);
//
//                        new HttpCall(this, new ApiResponse() {
//                            @Override
//                            public void onSuccess(Object response) {
//                                util.dismissProgressDialog();
//                                uploadImageResponse = (UploadImageResponse) response;
//                                user.setAvatar(uploadImageResponse.getFile_url());
//                                Log.e("After Casting", uploadImageResponse.getFile_url());
//                                prefManager.put(PrefManager.PROFILE_IMAGE, uploadImageResponse.getFile_url());
//                            }
//
//                            @Override
//                            public void onFailed(String error) {
//                                util.dismissProgressDialog();
//                                Toast.makeText(AddPractics.this, getResources().getText(R.string.error_saving_data), Toast.LENGTH_SHORT).show();
//                                Log.e("upload image failed :", error);
//                            }
//                        }).uploadImage(prefManager.getData(PrefManager.USER_ID)
//                                , prefManager.getData(PrefManager.USER_PASSWORD), ImageFilePath.getPath(this, selectedImageUri));
//                        pickerDialog.dismiss();
//
//                        break;
                    case Constants.HOURS_CODE:
                        user.setOpen_time((Table) data.getSerializableExtra(Constants.DATA));
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


    private void getTimaTableData(Table list) {
        if (user.getOpen_Type() == 3)
            tvNoTime.setText(R.string.permenant_closed);
        else
            tvNoTime.setText(R.string.always_open);

        if (list != null) {
            llNo.setVisibility(View.GONE);
            tablelayout.removeAllViews();
            com.germanitlab.kanonhealth.helpers.TimeTable timeTable = new com.germanitlab.kanonhealth.helpers.TimeTable();
            timeTable.creatTimeTable(list, this, tablelayout);
        } else
            llNo.setVisibility(View.VISIBLE);

    }

    @Override
    public void onGalleryClicked(Intent intent) {
        Helper.getCroppedImageFromCamera(this, PickerBuilder.SELECT_FROM_GALLERY);

    }

    @Override
    public void onCameraClicked() {
//        takeImageWithCamera();
        Helper.getCroppedImageFromCamera(this, PickerBuilder.SELECT_FROM_CAMERA);
    }

    @Override
    public void deleteMyImage() {
        try {
            user.setAvatar("");
            ImageHelper.setImage(civImageAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + user.getAvatar(), R.drawable.placeholder);
            prefManager.put(PrefManager.PROFILE_IMAGE, "");
            pickerDialog.dismiss();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

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

    @Override
    public void ImagePickerCallBack(Uri uri) {

        file= new File(ImageFilePath.getPath(this, uri));
        ImageHelper.setImage(civImageAvatar, uri);
        pickerDialog.dismiss();
    }


    @Override
    public void returnChoseSpecialityList(ArrayList<Speciality> specialitiesArrayList) {
        user.getSpecialities().clear();

    }

    @Override
    public void returnChoseLanguageList(ArrayList<Language> languageArrayList) {

    }

    @Override
    public void returnChoseDoctorList(ArrayList<UserInfo> doctorArrayList) {

    }
}
