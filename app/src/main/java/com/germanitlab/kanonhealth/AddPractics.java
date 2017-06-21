package com.germanitlab.kanonhealth;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.adapters.SpecilaitiesAdapter;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.callback.Message;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.initialProfile.DialogPickerCallBacks;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.Table;
import com.germanitlab.kanonhealth.models.user.Info;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

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
    int PLACE_PICKER_REQUEST = 22;


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
            user.setName(etName.getText().toString());
            user.setAddress(etLocation.getText().toString());
            info.setHouseNumber(etHouseNumber.getText().toString());
            info.setZipCode(etZipCode.getText().toString());
            info.setProvinz(etProvince.getText().toString());
            info.setCountry(etCountry.getText().toString());
            user.setInfo(info);
            user.setPhone(etTelephone.getText().toString());
            user.setId(AppController.getInstance().getClientInfo().getUser_id());
            user.setPassword(AppController.getInstance().getClientInfo().getPassword());
            new HttpCall(this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    Toast.makeText(AddPractics.this, "Save Practics", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onFailed(String error) {
                    Toast.makeText(AddPractics.this, error, Toast.LENGTH_LONG).show();
                }
            }).addClinic(user);
        }catch (Exception e){
            Crashlytics.logException(e);
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
        }catch (Exception e){
            Crashlytics.logException(e);
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
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.locationonmap)
    public void locationonmap() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(AddPractics.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.member_text)
    public void edit_member_list() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("Constants", Constants.DoctorAll);
            bundle.putSerializable(Constants.CHOSED_LIST, (Serializable) user.getMembers_at());
            showDialogFragment(bundle);
        }catch (Exception e){
            Crashlytics.logException(e);
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
        }catch (Exception e){
            Crashlytics.logException(e);
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
        }catch (Exception e){
            Crashlytics.logException(e);
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
                    for (ChooseModel speciality : user.getSpecialities()) {
                        tvSpecilities.append(speciality.getSpeciality_title() + " ");
                    }

                    setRecyclerView(templist, R.id.speciality_recycleview, LinearLayoutManager.HORIZONTAL, Constants.SPECIALITIES);
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
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
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
                        Glide.with(this).load(selectedImageUri).into(civImageAvatar);

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
                        }).uploadImage(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                                , AppController.getInstance().getClientInfo().getPassword(), getPathFromURI(selectedImageUri));
                        pickerDialog.dismiss();

                        break;
                    case TAKE_PICTURE:
                        util.showProgressDialog();
                        Log.e("ImageUri", selectedImageUri != null ? selectedImageUri.toString() : "Empty Uri");

                        prefManager.put(PrefManager.PROFILE_IMAGE, selectedImageUri.toString());
                        Glide.with(this).load(selectedImageUri).into(civImageAvatar);
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
                                Log.e("upload image failed :", error);                            }
                        }).uploadImage(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                                , AppController.getInstance().getClientInfo().getPassword(), ImageFilePath.
                                        .(selectedImageUri));
                        pickerDialog.dismiss();
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
                    Place place = PlacePicker.getPlace(data, this);
                    user.setLocation_lat(place.getLatLng().latitude);
                    user.setLocation_long(place.getLatLng().longitude);
                }
            }

        }catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }

    }

    private void getTimaTableData(List<Table> list) {
        if (user.getOpen_Type() == 3)
            tvNoTime.setText("permenant_closed");
        else
            tvNoTime.setText("Always Open");

        if (list.size() > 0) {
            llNo.setVisibility(View.GONE);
            tablelayout.removeAllViews();
            com.germanitlab.kanonhealth.helpers.TimeTable timeTable=new com.germanitlab.kanonhealth.helpers.TimeTable();
            timeTable.creatTimeTable(list,this,tablelayout);
        }
        else
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
            Helper.setImage(this, Constants.CHAT_SERVER_URL
                    + "/" + user.getAvatar(), civImageAvatar, R.drawable.placeholder);
            prefManager.put(PrefManager.PROFILE_IMAGE, "");
            pickerDialog.dismiss();
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void takeImageWithCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "New Picture");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
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
            editText.setError("Please Fill Data");
            return false;
        } else
            return true;
    }
}
