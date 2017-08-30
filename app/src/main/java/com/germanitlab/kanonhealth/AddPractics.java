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
import com.germanitlab.kanonhealth.api.models.ClinicEdit;
import com.germanitlab.kanonhealth.api.models.Language;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.Times;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.models.WorkingHours;
import com.germanitlab.kanonhealth.callback.Message;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.initialProfile.DialogPickerCallBacks;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.models.user.Info;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.mukesh.countrypicker.Country;
import com.nex3z.flowlayout.FlowLayout;
import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddPractics extends ParentActivity implements Message , DialogPickerCallBacks {

    //Edit text
    @BindView(R.id.ed_phone)
    EditText etPhone;
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
    //    private Uri selectedImageUri;
    Util util;
    UserInfo user;
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_PIC = 55;
    int PLACE_PICKER_REQUEST = 22;
    String practics_id = "";
    UserInfo clinic;
    String specialityIds="";
    String langIds="";
    String doctorIds="";
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_practics);
        ButterKnife.bind(this);
        initTB();

        try {
            user = new Gson().fromJson(PrefHelper.get(getBaseContext() , PrefHelper.KEY_USER_KEY , ""), UserInfo.class);
        } catch (Exception e) {
        }

        clinic = new UserInfo();
        clinic.setOpenType(0);
        WorkingHours workingHours= new WorkingHours();
        workingHours.setSunday(new ArrayList<Times>());
        workingHours.setMonday(new ArrayList<Times>());
        workingHours.setTuesday(new ArrayList<Times>());
        workingHours.setWednesday(new ArrayList<Times>());
        workingHours.setFriday(new ArrayList<Times>());
        workingHours.setSaturday(new ArrayList<Times>());
        ArrayList <WorkingHours> workingHoursArrayList= new ArrayList<>();
        workingHoursArrayList.add(workingHours);
        clinic.setTimeTable(workingHoursArrayList);

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
            etPhone.setClickable(false);
            etPhone.setFocusable(false);
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (clinic.getAvatar() != null && !clinic.getAvatar().isEmpty()) {
                                ImageHelper.setImage(civImageAvatar, ApiHelper.SERVER_IMAGE_URL + "/" + clinic.getAvatar());
                            }
                            etName.setText(clinic.getName());
                            etPhone.setText(clinic.getPhone());
                            etHouseNumber.setText(clinic.getHouseNumber());
                            etStreetName.setText(clinic.getStreetName());
                            etZipCode.setText(clinic.getZipCode());
                            etProvince.setText(clinic.getProvidence());
                            etCity.setText(clinic.getCity());
                            etCountry.setText(clinic.getCountry());
                            etTelephone.setText(clinic.getPhone());
                            setSpecialities();
                            setLanguages();
                            setMemberDoctors();
                            getTimaTableData();

                        }
                    });

                    //--------------------------------------------------------------------------location------------------------------------------------------//

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddPractics.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
                progressDialog.dismiss();
            }
        }).start();

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
        if (!isvalid(etName) || !isvalid(etPhone) || !isvalid(etHouseNumber) || !isvalid(etZipCode) || !isvalid(etProvince) || !isvalid(etCountry))
            return;

        if (practics_id != null) {
            AddPractics.this.showProgressBar();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ClinicEdit result = ApiHelper.postEditClinic(Integer.valueOf(practics_id), etName.getText().toString(), specialityIds, etStreetName.getText().toString(), etHouseNumber.getText().toString(), etZipCode.getText().toString(), etCity.getText().toString(), etProvince.getText().toString(), etCountry.getText().toString(), etPhone.getText().toString(), doctorIds, String.valueOf(clinic.getOpenType()), langIds, file, getApplicationContext());

                    if (result != null) {
                        if(clinic.getOpenType()!=0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AddPractics.this.hideProgressBar();
                                    Toast.makeText(AddPractics.this, R.string.edit_practics, Toast.LENGTH_LONG).show();
                                    finish();

                                }
                            });
                        }else
                        {
                            sendWorkingHours();
                        }

                    }else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddPractics.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                                AddPractics.this.hideProgressBar();

                            }
                        });

                    }

                }
            }).start();
        } else {
            AddPractics.this.showProgressBar();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ClinicEdit result = ApiHelper.postAddClinic(user.getUserID(), etName.getText().toString(), specialityIds, etStreetName.getText().toString(), etHouseNumber.getText().toString(), etZipCode.getText().toString(), etCity.getText().toString(), etProvince.getText().toString(), etCountry.getText().toString(), etPhone.getText().toString(), doctorIds, String.valueOf(AddPractics.this.clinic.getOpenType()), langIds, file, getApplicationContext());
                    if (result != null) {
                        if(clinic.getOpenType()!=0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AddPractics.this.hideProgressBar();
                                    Toast.makeText(AddPractics.this, R.string.save_practics, Toast.LENGTH_LONG).show();
                                    finish();

                                }
                            });
                        }else
                        {
                            sendWorkingHours();
                        }

                    }else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddPractics.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                                AddPractics.this.hideProgressBar();

                            }
                        });

                    }
                }

            }).start();
        }
    }

private void sendWorkingHours()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean result = ApiHelper.ClinicWorkingHours(clinic.getTimeTable().get(0), String.valueOf(clinic.getOpenType()),practics_id);
                AddPractics.this.hideProgressBar();
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddPractics.this, R.string.save_practics, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });

                }else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddPractics.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }

        }).start();
    }


    @OnClick(R.id.edit_speciality_list)
    public void editSpecialityList(View view) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Constants", Constants.SPECIALITIES);
            bundle.putSerializable(Constants.CHOSED_LIST, clinic.getSpecialities());
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
            bundle.putSerializable(Constants.CHOSED_LIST, clinic.getClinics());
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

 //======================================================================>//
   @OnClick(R.id.edit_time_table)
    public void editTimeTable(View view) {
        try {
            Intent intent = new Intent(this, TimeTable.class);
            intent.putExtra(Constants.DATA, clinic.getTimeTable());
            intent.putExtra("type", clinic.getOpenType());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    //=================================================================================================>//
                    case Constants.HOURS_CODE:
                        clinic.setTimeTable((ArrayList<WorkingHours>) data.getSerializableExtra("list"));
                        clinic.setOpenType(data.getIntExtra("type", 0));
                        getTimaTableData();
                        break;
                    case Constants.HOURS_TYPE_CODE:
                        clinic.setOpenType(data.getIntExtra("type", 0));
                        break;
                }
//            } else if (requestCode == PLACE_PICKER_REQUEST) {
//                if (resultCode == RESULT_OK) {
//                    Place place = PlacePicker.getPlace(this, data);
//                    user.setLocation_lat(place.getLatLng().latitude);
//                    user.setLocation_long(place.getLatLng().longitude);
               }


        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }

    }

//====================================================================================================================>//
    private void getTimaTableData() {
        tablelayout.removeAllViews();
        llNo.setVisibility(View.VISIBLE);
        if (clinic.getOpenType() == 3) {
            tvNoTime.setText(R.string.permenant_closed);
        }
        else if(clinic.getOpenType() == 1) {
            tvNoTime.setText(R.string.always_open);
        }
        else if(clinic.getOpenType()==2) {
            tvNoTime.setText(R.string.no_hours_available);
        }else {

            if (clinic.getTimeTable() != null && clinic.getTimeTable().size() > 0) {
                llNo.setVisibility(View.GONE);
                tablelayout.removeAllViews();
                com.germanitlab.kanonhealth.helpers.TimeTable timeTable = new com.germanitlab.kanonhealth.helpers.TimeTable();
                timeTable.creatTimeTable(clinic.getTimeTable().get(0), this, tablelayout);
            } else
                llNo.setVisibility(View.VISIBLE);
                tvNoTime.setText(R.string.no_time_has_set);
        }
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
            clinic.setAvatar("");
            file=null;
            ImageHelper.setImage(civImageAvatar, "", R.drawable.placeholder);
            PrefHelper.put(getBaseContext() , PrefHelper.KEY_PROFILE_IMAGE , "");
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

    //======================================================================================================================================>

//    @OnClick(R.id.location_img)
//    public void openMap() {
//        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//        intent.putExtra("long", user.getLocation_long());
//        intent.putExtra("lat", user.getLocation_lat());
//        startActivity(intent);
//    }




    private void setSpecialities()
    {
        tvSpecilities.setText("");
        specialityIds="";
        flSpecilities.removeAllViews();
                    int size = 0;
                    for (Speciality speciality : clinic.getSpecialities()) {
                        flSpecilities.addView(ImageHelper.setImageCircle(speciality.getImage(), this));
                        tvSpecilities.append(speciality.getTitle());
                        specialityIds= specialityIds.concat(String.valueOf(speciality.getSpecialityID()));
                        size++;
                        if (size <  clinic.getSpecialities().size()) {
                            tvSpecilities.append(", ");
                            specialityIds = specialityIds.concat(",");
                        }
                    }

    }

    private void setLanguages()
    {
        tvLanguages.setText("");
        langIds="";
        flLanguages.removeAllViews();
        int size = 0;
        for (Language  language : clinic.getSupportedLangs()) {
            if (!TextUtils.isEmpty(language.getLanguageCountryCode())) {
                Country country = Country.getCountryByISO(language.getLanguageCountryCode());
                if (country != null) {
                    flLanguages.addView(ImageHelper.setImageHeart(country.getFlag(), getApplicationContext()));
                }
                tvLanguages.append(language.getLanguageTitle());
                langIds = langIds.concat(String.valueOf(language.getLanguageID()));
                if (clinic.getSupportedLangs().size() > size + 1) {
                    tvLanguages.append(" , ");
                    langIds = langIds.concat(",");
                    size++;
                }
            }
        }

    }

    private void setMemberDoctors()
    {
        doctorIds="";
        flInvite.removeAllViews();
        int size = 0;
        for (UserInfo doctor : clinic.getClinics()) {
            flInvite.addView(ImageHelper.setImageCircle(doctor.getAvatar(), this));
            doctorIds= doctorIds.concat(String.valueOf(doctor.getUserID()));
            size++;
            if (size <  clinic.getClinics().size()) {
                doctorIds= doctorIds.concat(",");
            }
        }

    }

    @Override
    public void ImagePickerCallBack(Uri uri) {

        file= new File(ImageFilePath.getPath(this, uri));
        ImageHelper.setImage(civImageAvatar, uri);
        pickerDialog.dismiss();
    }


    @Override
    public void returnChoseSpecialityList(ArrayList<Speciality> specialitiesArrayList) {
     clinic.setSpecialities(specialitiesArrayList);
        setSpecialities();
    }

    @Override
    public void returnChoseLanguageList(ArrayList<Language> languageArrayList) {
        clinic.setSupportedLangs(languageArrayList);
        setLanguages();

    }

    @Override
    public void returnChoseDoctorList(ArrayList<UserInfo> doctorArrayList) {
        clinic.setClinics(doctorArrayList);
        setMemberDoctors();

    }
}
