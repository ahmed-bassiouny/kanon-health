package com.germanitlab.kanonhealth;

import android.*;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class AddPractics extends AppCompatActivity implements Message<ChooseModel>,DialogPickerCallBacks {

    //Edit text
    @BindView(R.id.ed_location)
    EditText ed_location;
    @BindView(R.id.ed_street_name)
    EditText ed_street_name;
    @BindView(R.id.ed_house_number)
    EditText ed_house_number;
    @BindView(R.id.ed_zip_code)
    EditText ed_zip_code;
    @BindView(R.id.ed_city)
    EditText ed_city;
    @BindView(R.id.ed_province)
    EditText ed_province;
    @BindView(R.id.ed_country)
    EditText ed_country;
    @BindView(R.id.ed_name)
    EditText ed_name;


    @BindView(R.id.et_telephone)
    EditText et_telephone;
    //Image view

    @BindView(R.id.img_edit_avatar)
    CircleImageView imageAvatar;
    @BindView(R.id.edit_image)
    CircleImageView edit_image;
    @BindView(R.id.edit_time_table)
    ImageView edit_time_table;

    // Time table
    @BindView(R.id.tv_monday)
    TextView monday;
    @BindView(R.id.tv_tuesday)
    TextView tuesday;
    @BindView(R.id.tv_wednesday)
    TextView wednesday;
    @BindView(R.id.tv_thursday)
    TextView thursday;
    @BindView(R.id.tv_friday)
    TextView friday;
    @BindView(R.id.tv_saturday)
    TextView saturday;
    @BindView(R.id.tv_sunday)
    TextView sunday;
    @BindView(R.id.no_time)
    LinearLayout ll_no;
    @BindView(R.id.monday_time)
    LinearLayout ll_monday;
    @BindView(R.id.tuesday_time)
    LinearLayout ll_tuesday;
    @BindView(R.id.wednesday_time)
    LinearLayout ll_wednesday;
    @BindView(R.id.thursday_time)
    LinearLayout ll_thursday;
    @BindView(R.id.friday_time)
    LinearLayout ll_friday;
    @BindView(R.id.saturday_time)
    LinearLayout ll_saturday;
    @BindView(R.id.sunday_time)
    LinearLayout ll_sunday;
    @BindView(R.id.tv_no_time)
    TextView tv_no_time;

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
        user = new User();
        info=new Info();
        pickerDialog = new PickerDialog(true);
        prefManager = new PrefManager(this);
        util = Util.getInstance(this);
    }

    @OnClick(R.id.save)
    public void save(View view) {
        if(!isvalid(ed_name)||!isvalid(ed_location)||!isvalid(ed_house_number)||!isvalid(ed_zip_code)||!isvalid(ed_province)||!isvalid(ed_country)||!isvalid(et_telephone))
            return;
        user.setName(ed_name.getText().toString());
        user.setAddress(ed_location.getText().toString());
        info.setHouseNumber(ed_house_number.getText().toString());
        info.setZipCode(ed_zip_code.getText().toString());
        info.setProvinz(ed_province.getText().toString());
        info.setCountry(ed_country.getText().toString());
        user.setInfo(info);
        user.setPhone(et_telephone.getText().toString());
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
    }

    @OnClick(R.id.edit_speciality_list)
    public void editSpecialityList(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("Constants", Constants.SPECIALITIES);
        bundle.putSerializable(Constants.CHOSED_LIST,(Serializable) user.getSpecialities());
        showDialogFragment(bundle);
    }

    @OnClick(R.id.edit_languages_list)
    public void edit_languages_list() {
        Bundle bundle = new Bundle();
        bundle.putInt("Constants", Constants.LANGUAUGE);
        bundle.putSerializable(Constants.CHOSED_LIST, (Serializable)user.getSupported_lang());
        showDialogFragment(bundle);
    }
    @OnClick(R.id.locationonmap)
    public void locationonmap(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddPractics.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    @OnClick(R.id.member_text)
    public void edit_member_list() {
        Bundle bundle = new Bundle();
        bundle.putInt("Constants", Constants.DoctorAll);
        bundle.putSerializable(Constants.CHOSED_LIST, (Serializable)user.getMembers_at());
        showDialogFragment(bundle);
    }
    @OnClick(R.id.edit_image)
    public void onEditProfileImageClicked() {

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

    }

    private void askForPermission(String[] permission, Integer requestCode) {
        ActivityCompat.requestPermissions(this, permission, requestCode);
    }

    @OnClick(R.id.edit_time_table)
    public void editTimeTable(View view) {
            Intent intent = new Intent(this, TimeTable.class);
            intent.putExtra(Constants.DATA, (Serializable) user.getOpen_time());
            startActivityForResult(intent, Constants.HOURS_CODE);
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
        ArrayList<ChooseModel> templist = new ArrayList<>();
        switch (type) {
            case Constants.SPECIALITIES:
                user.getSpecialities().clear();
                for (ChooseModel item : specialitiesArrayList) {
                    if (item.getIsMyChoise())
                        templist.add(item);
                }
                user.setSpecialities(templist);
                setRecyclerView(templist,R.id.speciality_recycleview,LinearLayoutManager.HORIZONTAL,Constants.SPECIALITIES);
                break;
            case Constants.LANGUAUGE:
                user.getSupported_lang().clear();
                for (ChooseModel item : specialitiesArrayList) {
                    if (item.getIsMyChoise())
                        templist.add(item);
                }
                user.setSupported_lang(templist);
                setRecyclerView(templist,R.id.language_recycleview,LinearLayoutManager.HORIZONTAL,Constants.LANGUAUGE);
                break;
            case Constants.MEMBERAT:
            case Constants.DoctorAll:
                user.getMembers_at().clear();
                for (ChooseModel item : specialitiesArrayList) {
                    if (item.getIsMyChoise())
                        templist.add(item);
                }
                user.setMembers_at(templist);
                setRecyclerView(templist,R.id.member_recycleview, LinearLayoutManager.VERTICAL, Constants.MEMBERAT);

                break;
        }
    }
    public void setRecyclerView( List<ChooseModel> list,int id,int linearLayoutManager, int type) {
        if(recyclerView==null)
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.IMAGE_REQUEST:
                    selectedImageUri = data.getData();
                    prefManager.put(PrefManager.PROFILE_IMAGE, selectedImageUri.toString());
                    util.showProgressDialog();
                    Log.e("ImageUri", selectedImageUri != null ? selectedImageUri.toString() : "Empty Uri");
                    Glide.with(this).load(selectedImageUri).into(imageAvatar);

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
                    Glide.with(this).load(selectedImageUri).into(imageAvatar);
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
                            Log.e("upload image failed :", error);
                        }
                    }).uploadImage(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                            , AppController.getInstance().getClientInfo().getPassword(), getPathFromURI(selectedImageUri));
                    pickerDialog.dismiss();
                    break;
                case Constants.HOURS_CODE:
                    user.setOpen_time((List<Table>) data.getSerializableExtra(Constants.DATA));
                    user.setOpen_Type(data.getIntExtra("type" , 0));
                    getTimaTableData(user.getOpen_time());
                    break;
                case Constants.HOURS_TYPE_CODE :
                    user.setOpen_Type(data.getIntExtra("type" ,0));
                    break;
            }
        }else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                user.setLocation_lat(place.getLatLng().latitude);
                user.setLocation_long(place.getLatLng().longitude);
            }
        }

    }
    private void getTimaTableData(List<Table> list) {
        if(user.getOpen_Type() == 3)
            tv_no_time.setText("permenant_closed");
       else
            tv_no_time.setText("Always Open");

        if (list.size() > 0)
            ll_no.setVisibility(View.GONE);
        else
            ll_no.setVisibility(View.VISIBLE);
        passData(list);
    }

    private void passData(List<Table> list) {
        clearTexts();
        for (int s= 0 ; s < list.size() ; s++) {
            Table table = list.get(s);
            if (table.getDayweek() != null) {
                if (table.getDayweek().equals("1")) {
                    setViewText(monday, table);
                    ll_monday.setVisibility(View.VISIBLE);
                } else if (table.getDayweek().equals("2")) {
                    setViewText(tuesday, table);
                    ll_tuesday.setVisibility(View.VISIBLE);
                } else if (table.getDayweek().equals("3")) {
                    setViewText(wednesday, table);
                    ll_wednesday.setVisibility(View.VISIBLE);
                } else if (table.getDayweek().equals("4")) {
                    setViewText(thursday, table);
                    ll_thursday.setVisibility(View.VISIBLE);
                } else if (table.getDayweek().equals("5")) {
                    setViewText(friday, table);
                    ll_friday.setVisibility(View.VISIBLE);
                } else if (table.getDayweek().equals("6")) {
                    setViewText(saturday, table);
                    ll_saturday.setVisibility(View.VISIBLE);
                } else if (table.getDayweek().equals("7")) {
                    setViewText(sunday, table);
                    ll_sunday.setVisibility(View.VISIBLE);
                }
            }
        }
        return;
    }

    private void clearTexts() {
        monday.setText("");
        tuesday.setText("");
        wednesday.setText("");
        thursday.setText("");
        friday.setText("");
        saturday.setText("");
        sunday.setText("");
        ll_monday.setVisibility(View.GONE);
        ll_thursday.setVisibility(View.GONE);
        ll_friday.setVisibility(View.GONE);
        ll_saturday.setVisibility(View.GONE);
        ll_wednesday.setVisibility(View.GONE);
        ll_sunday.setVisibility(View.GONE);
        ll_tuesday.setVisibility(View.GONE);
    }
    private void setViewText(TextView textView, Table table) {
        textView.append(table.getFrom() + " - " + table.getTo());
        textView.append(System.getProperty("line.separator"));
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
        user.setAvatar("");
        Helper.setImage(this, Constants.CHAT_SERVER_URL
                + "/" + user.getAvatar(), imageAvatar, R.drawable.placeholder);
        prefManager.put(PrefManager.PROFILE_IMAGE, "");
        pickerDialog.dismiss();
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
    private boolean isvalid(EditText editText){
        if(editText.getText().toString().trim().isEmpty()){
            editText.setError("Please Fill Data");
            return false;
        }else
            return true;
    }
}
