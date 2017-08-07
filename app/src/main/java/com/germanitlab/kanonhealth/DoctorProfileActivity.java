package com.germanitlab.kanonhealth;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.Crop.PickerBuilder;
import com.germanitlab.kanonhealth.adapters.SpecilaitiesAdapter;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.SupportedLang;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.callback.Message;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.ProgressHelper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.httpchat.HttpChatActivity;
import com.germanitlab.kanonhealth.initialProfile.DialogPickerCallBacks;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.inquiry.InquiryActivity;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.Table;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.mukesh.countrypicker.Country;
import com.nex3z.flowlayout.FlowLayout;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileActivity extends ParentActivity implements Message<ChooseModel>, Serializable, DialogPickerCallBacks {

    //--------------------------------------------


    @BindView(R.id.toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.avatar)
    CircleImageView circleImageViewAvatar;
    @BindView(R.id.tv_online)
    EditText tvOnline;
    @BindView(R.id.tv_contact)
    EditText tvContact;
    @BindView(R.id.ed_add_to_favourite)
    EditText edAddToFavourite;
    @BindView(R.id.image_star)
    ImageView ivStar;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.speciality_recycleview)
    FlowLayout flSpeciliaty;
    @BindView(R.id.edit_speciality_list)
    ImageView ivSpecialityList;
    @BindView(R.id.tv_specilities)
    TextView tvSpecilities;
    @BindView(R.id.img_location)
    ImageView imageViewLocation;
    @BindView(R.id.tv_location_value)
    TextView tvLocation;
    @BindView(R.id.ed_street_name)
    EditText etStreetName;
    @BindView(R.id.ed_house_number)
    EditText etHouseNumber;
    @BindView(R.id.ed_zip_code)
    EditText etZipCode;
    @BindView(R.id.ed_province)
    EditText etProvince;
    @BindView(R.id.tv_phone_value)
    TextView textViewPhone;
    @BindView(R.id.fl_language)
    FlowLayout flLanguages;
    @BindView(R.id.edit_languages_list)
    ImageView ivLanguagesList;
    @BindView(R.id.edit_time_table)
    ImageView ivTimeTable;
    @BindView(R.id.ll_time)
    LinearLayout linearLayoutTime;
    @BindView(R.id.nsv_soctor_profile_scroll)
    NestedScrollView nestedScrollView;
    @BindView(R.id.tv_rating)
    TextView textViewRating;

    UserInfo userInfo;

    //-------------------------
    private static final int CROP_PIC = 5;
    SpecilaitiesAdapter adapter;


    UploadImageResponse uploadImageResponse;
    Util util;


    Boolean is_me = false;

    private DoctorDocumentAdapter doctorDocumentAdapter;
    PrefManager prefManager;
    PickerDialog pickerDialog;
    private static final int TAKE_PICTURE = 1;
    int PLACE_PICKER_REQUEST = 7;
    private Menu menu;

    boolean is_doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile_view);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initToolbar();
        handleImeActions();

        try {
            is_me = userInfo.getUserID() == Integer.parseInt(prefManager.getData(PrefManager.USER_ID));
            userInfo = new UserInfo();
            userInfo = (UserInfo) getIntent().getSerializableExtra("doctor_data");
            prefManager = new PrefManager(this);
            pickerDialog = new PickerDialog(true);
            bindData();


            //is_doc = new PrefManager(this).get(PrefManager.IS_DOCTOR);

            //util = Util.getInstance(this);


        } catch (Exception e) {
            Toast.makeText(this, getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            Log.i("Doctor Profile  ", " Activity ", e);
            finish();
        }

    }

    private void handleImeActions() {
        /*et_location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                handleNewData();
                bindData();
                return true;
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (menu != null) {
            try {
                getMenuInflater().inflate(R.menu.menu_doctor_profile, menu);
                menu.findItem(R.id.mi_save).setVisible(false);
                menu.findItem(R.id.mi_edit).setVisible(true);


                if (is_me) {
                    menu.findItem(R.id.mi_save).setVisible(false);
                    menu.findItem(R.id.mi_edit).setVisible(true);

                } else {
                    menu.findItem(R.id.mi_save).setVisible(false);
                    menu.findItem(R.id.mi_edit).setVisible(false);

                }
                this.menu = menu;
            } catch (Exception e) {
            }

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
//                onBackPressed();
                finish();
                break;
            case R.id.mi_edit:
                setVisiblitiy(View.GONE);
                menu.findItem(R.id.mi_save).setVisible(true);
                menu.findItem(R.id.mi_edit).setVisible(false);
                tvOnline.setText(user.getTitle());
                edAddToFavourite.setText(user.getFirstName());
                tvContact.setText(user.getLastName());
                changeGravity(tvOnline, true);
                changeGravity(edAddToFavourite, true);
                changeGravity(tvContact, true);
                break;
            case R.id.mi_save:
                changeGravity(tvOnline, false);
                changeGravity(edAddToFavourite, false);
                changeGravity(tvContact, false);
                handleNewData();
                bindData();
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void chechEditPermission() {

        if (user.getUserID() == prefManager.getInt(PrefManager.USER_ID))
            is_me = true;
        else
            is_me = false;

    }


    @OnClick(R.id.tv_contact)
    public void contactClick(View v) {
        try {
            if (is_me)
                return;
            Gson gson = new Gson();
            if (user.getUserType() == UserInfo.CLINIC) {
                Intent intent = new Intent(this, InquiryActivity.class);
                //UserInfoResponse userInfoResponse = new UserInfoResponse();
                //userInfoResponse.setUser(user);
                //intent.putExtra("doctor_data", gson.toJson(userInfoResponse));
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, HttpChatActivity.class);
                intent.putExtra("doctorID", user.getUserID());
                startActivity(intent);
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Doctor Profile Activity", e.toString());
            Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
        }
    }


    private void handleNewData() {
        if (tvContact.getText().toString().trim().isEmpty() || edAddToFavourite.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.please_fill_data, Toast.LENGTH_SHORT).show();
            return;
        }
        user.setTitle(tvOnline.getText().toString());
        user.setLastName(tvContact.getText().toString());
        user.setFirstName(edAddToFavourite.getText().toString());
        tvLocation.setText(et_location.getText().toString());
        tvTelephone.setText(etTelephone.getText().toString());
        user.setPhone(etTelephone.getText().toString());
        user.setPassword(prefManager.getData(PrefManager.USER_PASSWORD));
        user.setUserID_request(user.get_Id());

        user.setStreetName(etStreetName.getText().toString());
        user.setHouseNumber(etHouseNumber.getText().toString());
        user.setZipCode(etZipCode.getText().toString());
        user.setProvidence(etProvince.getText().toString());

        sendDataToserver();
    }

    private void sendDataToserver() {
        util.showProgressDialog();
        new HttpCall(this, this).editProfile(user);

    }

    private void setVisiblitiy(int visiblitiy) {
        int notvisibility = (visiblitiy == View.VISIBLE) ? View.GONE : View.VISIBLE;
        et_location.setVisibility(notvisibility);
        tvLocation.setVisibility(visiblitiy);
        etTelephone.setVisibility(notvisibility);
        tvTelephone.setVisibility(visiblitiy);
        ivSpecialityList.setVisibility(notvisibility);
        ivLanguagesList.setVisibility(notvisibility);
        ivTimeTable.setVisibility(notvisibility);
        ivEdit.setVisibility(visiblitiy);
        iSave.setVisibility(notvisibility);
        civEditImage.setVisibility(notvisibility);
        //Edit ahmed 12-6-2017
        boolean editable = (visiblitiy == View.GONE) ? true : false;
        etLocation.setEnabled(editable);
        etStreetName.setEnabled(editable);
        etHouseNumber.setEnabled(editable);
        etZipCode.setEnabled(editable);
        etCity.setEnabled(editable);
        etProvince.setEnabled(editable);
        if (editable) {
            tvOnline.setFocusableInTouchMode(true);
            edAddToFavourite.setFocusableInTouchMode(true);
            tvContact.setFocusableInTouchMode(true);
        } else {
            tvOnline.setFocusable(false);
            edAddToFavourite.setFocusable(false);
            tvContact.setFocusable(false);
        }
    }

    @OnClick(R.id.edit_time_table)
    public void editTimeTable(View view) {
        if (user.getOpen_Type() == 0) {
            Intent intent = new Intent(this, TimeTable.class);
            intent.putExtra(Constants.DATA, (Serializable) user.getOpen_time());
            startActivityForResult(intent, Constants.HOURS_CODE);
        } else {
            Intent intent = new Intent(this, OpeningHoursActivity.class);
            intent.putExtra("type", user.getOpen_Type());
            intent.putExtra(Constants.DATA, (Serializable) user.getOpen_time());
            startActivityForResult(intent, Constants.HOURS_TYPE_CODE);
        }
    }

    private void setAdapters() {
        RecyclerView recyclerView = new RecyclerView(getApplicationContext());
        if (user.getSpecialities() != null)
            setImage(user.getSpecialities(), flSpeciliaty, 1);
        if (user.getClinics() != null)
            if (user.getClinics().size() > 0) {
                set(adapter, user.getClinics(), recyclerView, R.id.member_recycleview, LinearLayoutManager.VERTICAL, Constants.MEMBERAT);
            }
        if (user.getDocuments() != null) {
            if (!is_me) { // handle the document if the profile is not my profile
                doctorDocumentAdapter = new DoctorDocumentAdapter(user.getDocuments(), this);
                recyclerViewDocument.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                recyclerViewDocument.setAdapter(doctorDocumentAdapter);
                recyclerViewDocument.setBackgroundResource(R.color.chatbackground_gray);
            } else {
                viewDocumentLine.setVisibility(View.GONE);
            }
        }
    }

    private void setImage(List<ChooseModel> supported_lang, FlowLayout flowLayout, int i) {
        flowLayout.removeAllViews();
        txtLanguageNames.setText("");
        int size = 0;
        for (ChooseModel chooseModel : supported_lang) {
            if (i == 0) {
                String country_code = chooseModel.getCountry_code();
                if (!TextUtils.isEmpty(country_code)) {
                    txtLanguageNames.append(chooseModel.getLang_title());
                    if (supported_lang.size() > size + 1) {
                        txtLanguageNames.append(" , ");
                        size++;
                    }
                    Country country = Country.getCountryByISO(country_code);
                    if (country != null) {
                        flowLayout.addView(ImageHelper.setImageHeart(country.getFlag(), getApplicationContext()));
                    }
                }
            } else if (i == 1) {
                flowLayout.addView(ImageHelper.setImageCircle(chooseModel.getSpeciality_icon(), this));
            }
        }
    }


    public void set(RecyclerView.Adapter adapter, List<ChooseModel> list, int id, int linearLayoutManager, int type) {
        RecyclerView recyclerVie;
        adapter = new SpecilaitiesAdapter(list, getApplicationContext(), type);
        recyclerVie = (RecyclerView) findViewById(id);
        recyclerVie.setHasFixedSize(true);
        recyclerVie.setLayoutManager(new LinearLayoutManager(this, linearLayoutManager, false));
        recyclerVie.setNestedScrollingEnabled(false);
        recyclerVie.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {

                    /*case CROP_PIC :
                        afterCropFinish();
                        break;*/

                    case Constants.HOURS_CODE:
                        user.setOpen_time((Table) data.getSerializableExtra(Constants.DATA));
                        user.setOpen_Type(data.getIntExtra("type", 0));
                        getTimaTableData(user.getOpen_time());
                        break;
                    case Constants.HOURS_TYPE_CODE:
                        user.setOpen_Type(data.getIntExtra("type", 0));
                        break;
                }
                if (requestCode == PLACE_PICKER_REQUEST) {
                    Place place = PlacePicker.getPlace(this, data);
                    user.setLocation_lat(place.getLatLng().latitude);
                    user.setLocation_long(place.getLatLng().longitude);
                }
            }
        } catch (Exception e) {
        }
    }

    private void bindData() {


        if (userInfo.getAvatar() != null && !userInfo.getAvatar().isEmpty()) {
            ImageHelper.setImage(circleImageViewAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + userInfo.getAvatar());
        }
        if (is_me) {
            tvToolbarName.setText(getResources().getString(R.string.my_profile));
            edAddToFavourite.setText(userInfo.getFirstName());
            tvContact.setText(userInfo.getLastName());
            tvOnline.setText(userInfo.getTitle());

        } else {
            tvToolbarName.setText(userInfo.getLastName() + " " + userInfo.getFirstName());
            checkDoctor();
            tvContact.setText(R.string.contact_by_chat);
            if (userInfo.getAvailable() == 1)
                tvOnline.setText(R.string.status_online);
            else
                tvOnline.setText(R.string.status_offline);
        }

        textViewRating.setText(getResources().getString(R.string.rating) + "  " + String.valueOf(userInfo.getRate_count()) + " (" + String.valueOf(user.getRate_avr()) + " " + getResources().getString(R.string.reviews) + ")");

        // set specialities
        if (userInfo.getSpecialities() != null) {
            String specialities = "";
            for (Speciality speciality : userInfo.getSpecialities()) {
                specialities += speciality.getTitle() + ", ";
            }
            specialities = specialities.substring(0, specialities.length() - 2);
            tvSpecilities.setText(specialities);
        }
        // end specialities
        // set country
        String countryDail = userInfo.getCountry_code();
        if (!TextUtils.isEmpty(countryDail)) {
            Country country = null;
            for (Country c : Country.getAllCountries()) {
                if (c.getDialCode().equals(countryDail)) {
                    country = c;
                }
            }
            if (country != null) {
                imageViewLocation.setImageBitmap(ImageHelper.TrimBitmap(country.getFlag(), DoctorProfileActivity.this));
                Locale l = new Locale("", country.getCode());
                if (l != null) {
                    tvLocation.setText(l.getDisplayCountry(Locale.getDefault()));
                }
            }
        }
        // end country
        etStreetName.setText(userInfo.getStreetName());
        etHouseNumber.setText(userInfo.getHouseNumber());
        etZipCode.setText(userInfo.getZipCode());
        etProvince.setText(userInfo.getProvidence());
        textViewPhone.setText(userInfo.getPhone());


        if (userInfo.getSupportedLangs() != null) {
            for (SupportedLang supportedLang : userInfo.getSupportedLangs()) {
                String country_code = supportedLang.getCountryCode();
                Country country = Country.getCountryByISO(country_code);
                if (country != null) {
                    flLanguages.addView(ImageHelper.setImageHeart(country.getFlag(), getApplicationContext()));
                }
            }
        }

        // member at need handle
        if (userInfo.getClinics().size() > 0) {
            /*set(adapter, userInfo.getClinics(), R.id.member_recycleview, LinearLayoutManager.VERTICAL, Constants.MEMBERAT);
            RecyclerView recyclerVie;
            adapter = new SpecilaitiesAdapter(list, getApplicationContext(), type);
            recyclerVie = (RecyclerView) findViewById(id);
            recyclerVie.setHasFixedSize(true);
            recyclerVie.setLayoutManager(new LinearLayoutManager(this, linearLayoutManager, false));
            recyclerVie.setNestedScrollingEnabled(false);
            recyclerVie.setAdapter(adapter);*/
        }





        checkDoctor();
    }


    @OnClick(R.id.edit_speciality_list)
    public void editSpecialityList(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("Constants", Constants.SPECIALITIES);
        bundle.putSerializable(Constants.CHOSED_LIST, (Serializable) user.getSpecialities());
        showDialogFragment(bundle);
    }

    @OnClick(R.id.edit_languages_list)
    public void edit_languages_list() {
        Bundle bundle = new Bundle();
        bundle.putInt("Constants", Constants.LANGUAUGE);
        bundle.putSerializable(Constants.CHOSED_LIST, (Serializable) user.getSupported_lang());
        showDialogFragment(bundle);
    }

    @OnClick(R.id.edit_member_list)
    public void edit_member_list() {
        Bundle bundle = new Bundle();
        bundle.putInt("Constants", Constants.DoctorAll);
        bundle.putSerializable(Constants.CHOSED_LIST, (Serializable) user.getMembers_at());
        showDialogFragment(bundle);
    }

    private void getTimaTableData(Table list) {
        if (user != null) {
            if (user.getOpen_Type() == 3)
                tvNoTime.setText(R.string.permenant_closed);
            else
                tvNoTime.setText(R.string.always_open);
            if (list != null) {
                if (list != null) {
                    llNoTime.setVisibility(View.GONE);
                    tablelayout.removeAllViews();
                    com.germanitlab.kanonhealth.helpers.TimeTable timeTable = new com.germanitlab.kanonhealth.helpers.TimeTable();
                    timeTable.creatTimeTable(list, this, tablelayout);
                } else
                    llNoTime.setVisibility(View.VISIBLE);
            }
        }
    }


    @OnClick(R.id.image_star)
    public void image_star() {
        Intent intent = new Intent(this, RateActivity.class);
        intent.putExtra("doc_id", String.valueOf(user.get_Id()));
        startActivity(intent);
    }

    @OnClick(R.id.ed_add_to_favourite)
    public void addToMyDoctor() {
        if (is_me)
            return;
        if (user != null && !TextUtils.isEmpty(user.getIs_my_doctor()))
            if (user.getIs_my_doctor().equals("0")) {
                new HttpCall(this, new ApiResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        if (response != null && user != null) {
                            user.setIs_my_doctor("1");
                            checkDoctor();
                        }
                    }

                    @Override
                    public void onFailed(String error) {
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();
                        Log.i("Doctor Profile  ", " Activity " + error);
                    }
                }).addToMyDoctor(user.get_Id() + "");
            } else {
                new HttpCall(this, new ApiResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        if (response != null && user != null) {
                            user.setIs_my_doctor("0");
                            checkDoctor();
                        }
                    }

                    @Override
                    public void onFailed(String error) {
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();
                        Log.i("Doctor Profile  ", " Activity " + error);
                    }
                }).removeFromMyDoctor(user.get_Id() + "");
            }

    }

    private void checkDoctor() {
        if (userInfo.getIs_my_doctor() == 0)
            edAddToFavourite.setText(getString(R.string.add_to_my_doctors));
        else
            edAddToFavourite.setText(getString(R.string.remove_from));
    }


    @OnClick(R.id.edit_image)
    public void onEditProfileImageClicked() {
        if (is_me) {
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
    }

    private void askForPermission(String[] permission, Integer requestCode) {
        ActivityCompat.requestPermissions(this, permission, requestCode);
    }

    @Override
    public void Response(ArrayList<ChooseModel> specialitiesArrayList, int type) {
        ArrayList<ChooseModel> templist = new ArrayList<>();
        RecyclerView recyclerView = new RecyclerView(getApplicationContext());
        switch (type) {
            case Constants.SPECIALITIES:
                user.getSpecialities().clear();
                for (ChooseModel item : specialitiesArrayList) {
                    if (item.getIsMyChoise())
                        templist.add(item);
                }
                user.setSpecialities(templist);
                setImage(user.getSpecialities(), flSpeciliaty, 1);
                tvSpecilities.setText("");
                int size = 0;
                for (ChooseModel speciality : user.getSpecialities()) {
                    tvSpecilities.append(speciality.getSpeciality_title());
                    if (size < user.getSpecialities().size())
                        tvSpecilities.append(", ");
                }
                break;
            case Constants.LANGUAUGE:
                user.getSupported_lang().clear();
                for (ChooseModel item : specialitiesArrayList) {
                    if (item.getIsMyChoise())
                        templist.add(item);
                }
                user.setSupported_lang(templist);
                setImage(user.getSupported_lang(), flLanguages, 0);
                // set(adapter, user.getSupported_lang(), recyclerView, R.id.language_recycleview, LinearLayoutManager.HORIZONTAL, Constants.LANGUAUGE);
                break;
            case Constants.DoctorAll:
                user.getMembers_at().clear();
                for (ChooseModel item : specialitiesArrayList) {
                    if (item.getIsMyChoise())
                        templist.add(item);
                }
                user.setMembers_at(templist);
                if (user.getMembers_at().size() > 0) {
                    set(adapter, user.getMembers_at(), recyclerView, R.id.member_recycleview, LinearLayoutManager.VERTICAL, Constants.MEMBERAT);
                }
                break;
        }
    }

    @Override
    public void onSuccess(Object response) {
        Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
        menu.findItem(R.id.mi_edit).setVisible(true);
        menu.findItem(R.id.mi_save).setVisible(false);
        util.dismissProgressDialog();
        setVisiblitiy(View.VISIBLE);
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setUser(user);
        prefManager.put(PrefManager.USER_KEY, new Gson().toJson(userInfoResponse));


    }

    @Override
    public void onFailed(String error) {
        util.dismissProgressDialog();
        Toast.makeText(this, R.string.error_saving_data, Toast.LENGTH_SHORT).show();
        util.dismissProgressDialog();
        Log.i("Doctor Profile  ", " Activity " + error);
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
        user.setAvatar("");
        ImageHelper.setImage(civEditAvatar, Constants.CHAT_SERVER_URL + "/" + user.getAvatar(), R.drawable.placeholder);
        prefManager.put(PrefManager.PROFILE_IMAGE, "");
        pickerDialog.dismiss();
    }

    public void showDialogFragment(Bundle bundle) {
        MultiChoiseListFragment dialogFragment = new MultiChoiseListFragment();
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "list");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void changeGravity(TextView textView, boolean editable) {
        if (editable) {
            textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            textView.setPadding(5, 0, 0, 0);
        } else {
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 0, 0, 0);
        }
    }

    @OnClick(R.id.location_img)
    public void openMap() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("long", user.getLocation_long());
        intent.putExtra("lat", user.getLocation_lat());
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        nestedScrollView.fullScroll(View.FOCUS_UP);
    }


    @Override
    public void ImagePickerCallBack(final Uri uri) {
        ProgressHelper.showProgressBar(DoctorProfileActivity.this);
        prefManager.put(PrefManager.PROFILE_IMAGE, uri.toString());
        pickerDialog.dismiss();
        ImageHelper.setImage(civEditAvatar, uri);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = ApiHelper.editDoctor(DoctorProfileActivity.this, user, new File(ImageFilePath.getPath(DoctorProfileActivity.this, uri)));
                if (result) {
                    // success
                    ProgressHelper.hideProgressBar();
                    // get url and save it in sharePref
                    //user.setAvatar(uploadImageResponse.getFile_url());
                    //prefManager.put(PrefManager.PROFILE_IMAGE, uploadImageResponse.getFile_url());
                } else {
                    // falied
                    ProgressHelper.hideProgressBar();
                    Toast.makeText(getApplicationContext(), R.string.upload_failed, Toast.LENGTH_SHORT).show();
                    civEditAvatar.setImageResource(R.drawable.profile_place_holder);
                }
            }
        }).start();
    }
}
