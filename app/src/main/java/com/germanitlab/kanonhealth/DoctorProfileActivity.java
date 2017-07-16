package com.germanitlab.kanonhealth;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.germanitlab.kanonhealth.httpchat.HttpChatActivity;
import com.germanitlab.kanonhealth.initialProfile.DialogPickerCallBacks;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.inquiry.InquiryActivity;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.Table;
import com.germanitlab.kanonhealth.models.user.Info;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.payment.PaymentActivity;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.HeartImageView;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.mukesh.countrypicker.Country;

import org.apmem.tools.layouts.FlowLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileActivity extends AppCompatActivity implements Message<ChooseModel>, Serializable, ApiResponse, DialogPickerCallBacks {

    @BindView(R.id.speciality_recycleview)
    FlowLayout flSpeciliaty;
    SpecilaitiesAdapter adapter;

    @BindView(R.id.no_time)
    LinearLayout llNoTime;
    @BindView(R.id.tv_no_time)
    TextView tvNoTime;
    @BindView(R.id.toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.tablelayout)
    TableLayout tablelayout;

    @BindView(R.id.tv_online)
    EditText tvOnline;
    @BindView(R.id.img_edit_avatar)
    CircleImageView civEditAvatar;

    @BindView(R.id.tv_contact)
    EditText tvContact;
    @BindView(R.id.ed_add_to_favourite)
    EditText edAddToFavourite;
    //    @BindView(R.id.tv_qr_code)
//    TextView tv_qr_code;
    @BindView(R.id.tv_telephone)
    TextView tvTelephone;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.document_recycler_view)
    RecyclerView recyclerViewDocument;
    @BindView(R.id.tv_specilities)
    TextView tvSpecilities;
    @BindView(R.id.image_star)
    ImageView ivStar;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.tv_locations)
    TextView tvLocations;

    @BindView(R.id.edit_time_table)
    ImageView ivTimeTable;
    User user;
    UploadImageResponse uploadImageResponse;
    Util util;
    @BindView(R.id.linear_practice_profile)
    LinearLayout llPracticeProfile;
    @BindView(R.id.border)
    View vBorder;


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

    // data of ivEdit
    @BindView(R.id.edit)
    ImageView ivEdit;
    @BindView(R.id.et_telephone)
    EditText etTelephone;
    @BindView(R.id.et_location)
    EditText et_location;
    Boolean is_me = false;
    @BindView(R.id.edit_speciality_list)
    ImageView ivSpecialityList;
    @BindView(R.id.edit_languages_list)
    ImageView ivLanguagesList;
    @BindView(R.id.edit_member_list)
    ImageView ivMemberList;
    @BindView(R.id.save)
    ImageView iSave;
    @BindView(R.id.edit_image)
    CircleImageView civEditImage;
    @BindView(R.id.location_img)
    ImageView location_img;
    @BindView(R.id.mapContanier)
    RelativeLayout mapContanier;
    private DoctorDocumentAdapter doctorDocumentAdapter;
    PrefManager prefManager;
    PickerDialog pickerDialog;
    private Uri selectedImageUri;
    private static final int TAKE_PICTURE = 1;
    int PLACE_PICKER_REQUEST = 7;
    private Menu menu;
    @BindView(R.id.fl_language)
    FlowLayout flLanguages;
    boolean is_doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile_view);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initTB();

        handleImeActions();

        // check if doctor or clinic

        try {
            is_doc = new PrefManager(this).get(PrefManager.IS_DOCTOR);

            util = Util.getInstance(this);
            user = new User();
            user = (User) getIntent().getSerializableExtra("doctor_data");
            prefManager = new PrefManager(this);
            pickerDialog = new PickerDialog(true);
            bindData();

            prefManager = new PrefManager(this);
            pickerDialog = new PickerDialog(true);

        } catch (Exception e) {
            Toast.makeText(this, getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            Log.i("Doctor Profile  ", " Activity ", e);
            finish();
        }

    }

    private void handleImeActions() {
        et_location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                handleNewData();
                bindData();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                tvOnline.setText(user.getSubTitle());
                edAddToFavourite.setText(user.getFirst_name());
                tvContact.setText(user.getLast_name());
                /*android:gravity="left|center_vertical"
                                android:paddingLeft="5dp"*/
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
                menu.findItem(R.id.mi_edit).setVisible(true);
                menu.findItem(R.id.mi_save).setVisible(false);
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    private void initTB() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void chechEditPermission() {

        if (user.get_Id() == prefManager.getInt(PrefManager.USER_ID))
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
            if (user.isClinic == 1) {
                Intent intent = new Intent(this, InquiryActivity.class);
                UserInfoResponse userInfoResponse = new UserInfoResponse();
                userInfoResponse.setUser(user);
                intent.putExtra("doctor_data", gson.toJson(userInfoResponse));
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, HttpChatActivity.class);
                intent.putExtra("doctorID", user.get_Id());
                PrefManager prefManager = new PrefManager(this);
                prefManager.put(prefManager.USER_INTENT, gson.toJson(user));
                intent.putExtra("doctorName", user.getLast_name() + " " + user.getFirst_name());
                intent.putExtra("iamDoctor", false);
                intent.putExtra("doctorUrl", user.getAvatar());
                startActivity(intent);
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Doctor Profile Activity", e.toString());
            Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
        }
    }


    /*@OnClick(R.id.edit)
    public void edit(View view) {
        setVisiblitiy(View.GONE);
        editboolean = true;
        tvOnline.setText(user.getSubTitle());
        edAddToFavourite.setText(user.getFirst_name());
        tvContact.setText(user.getLast_name());
    }

    @OnClick(R.id.save)
    public void save(View view) {
        handleNewData();
        bindData();
    }*/

    private void handleNewData() {
        if (tvContact.getText().toString().trim().isEmpty() || edAddToFavourite.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.answer, Toast.LENGTH_SHORT).show();
            return;
        }
        user.setSubTitle(tvOnline.getText().toString());
        user.setLast_name(tvContact.getText().toString());
        user.setFirst_name(edAddToFavourite.getText().toString());
        tvLocation.setText(et_location.getText().toString());
        user.setAddress(et_location.getText().toString());
        tvTelephone.setText(etTelephone.getText().toString());
        user.setPhone(etTelephone.getText().toString());
        user.setPassword(prefManager.getData(PrefManager.USER_PASSWORD));
        user.setUserID_request(user.get_Id());
        // Edit ahmed 12 - 6-2017
        /**

         etZipCode.setEnabled(editable);
         etCity.setEnabled(editable);
         etProvince.setEnabled(editable);
         etCountry.setEnabled(editable);*/
        if (user.isClinic == 1) {
            user.setAddress(etLocation.getText().toString());
            user.getInfo().setStreetname(etStreetName.getText().toString());
            user.getInfo().setHouseNumber(etHouseNumber.getText().toString());
            user.getInfo().setZipCode(etZipCode.getText().toString());
            user.getInfo().setProvinz(etProvince.getText().toString());
            user.getInfo().setCountry(etCountry.getText().toString());
        } else {
            user.setInfo(new Info());
        }
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
        ivMemberList.setVisibility(notvisibility);
        ivTimeTable.setVisibility(notvisibility);
        ivEdit.setVisibility(visiblitiy);
        iSave.setVisibility(notvisibility);
        civEditImage.setVisibility(notvisibility);
        if (user.isClinic == 1)
            ivMemberList.setVisibility(notvisibility);
        //Edit ahmed 12-6-2017
        boolean editable = (visiblitiy == View.GONE) ? true : false;
        etLocation.setEnabled(editable);
        etStreetName.setEnabled(editable);
        etHouseNumber.setEnabled(editable);
        etZipCode.setEnabled(editable);
        etCity.setEnabled(editable);
        etProvince.setEnabled(editable);
        etCountry.setEnabled(editable);
        if (user.isClinic == 1)
            ivMemberList.setVisibility(View.VISIBLE);
        else
            ivMemberList.setVisibility(View.GONE);
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
        if (user.getSupported_lang() != null)
            setImage(user.getSupported_lang(), flLanguages, 0);
        if (user.getMembers_at() != null)
            set(adapter, user.getMembers_at(), recyclerView, R.id.member_recycleview, LinearLayoutManager.VERTICAL, Constants.MEMBERAT);
        if (user.getDocuments() != null) {
            doctorDocumentAdapter = new DoctorDocumentAdapter(user.getDocuments(), this);
            recyclerViewDocument.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerViewDocument.setAdapter(doctorDocumentAdapter);
            recyclerViewDocument.setBackgroundResource(R.color.chatbackground_gray);
        }
    }

    private void setImage(List<ChooseModel> supported_lang, FlowLayout flowLayout, int i) {
        flowLayout.removeAllViews();
        for (ChooseModel chooseModel : supported_lang) {
            if (i == 0) {
                String country_code = chooseModel.getCountry_code();
                if(!TextUtils.isEmpty(country_code)) {
                    Country country = Country.getCountryByISO(country_code);
                    if (country != null) {
                        flowLayout.addView(setImageHeart(country.getFlag()));
                    }
                }
            } else if (i == 1) {
                flowLayout.addView(setImageCircle(chooseModel.getSpeciality_icon()));
            }

        }
    }


    private View setImageCircle(String speciality_icon) {
        CircularImageView circularImageView = new CircularImageView(this);
        ImageHelper.setImage(circularImageView, Constants.CHAT_SERVER_URL_IMAGE + "/" + speciality_icon, -1, getApplicationContext());
        return circularImageView;
    }

    private View setImageHeart(int src) {
        View view;
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.heart_item_layout, null);
        HeartImageView item = (HeartImageView) view.findViewById(R.id.hiv_heart);
        item.setImageResource(src);
        return view;
    }

    public void set(RecyclerView.Adapter adapter, List<ChooseModel> list, RecyclerView recyclerVie, int id, int linearLayoutManager, int type) {

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
                    case Constants.IMAGE_REQUEST:
                        selectedImageUri = data.getData();
                        prefManager.put(PrefManager.PROFILE_IMAGE, selectedImageUri.toString());
                        util.showProgressDialog();
                        pickerDialog.dismiss();
                        Log.e("ImageUri", selectedImageUri != null ? selectedImageUri.toString() : "Empty Uri");
                        ImageHelper.setImage(civEditAvatar, selectedImageUri, this);
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
                                Toast.makeText(DoctorProfileActivity.this, R.string.upload_failed, Toast.LENGTH_SHORT).show();
                                Log.i("Doctor Profile  ", " Activity " + error);
                            }
                        }).uploadImage(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), ImageFilePath.getPath(this, selectedImageUri));
                        break;
                    case TAKE_PICTURE:
                        util.showProgressDialog();
                        Log.e("ImageUri", selectedImageUri != null ? selectedImageUri.toString() : "Empty Uri");

                        pickerDialog.dismiss();
                        prefManager.put(PrefManager.PROFILE_IMAGE, selectedImageUri.toString());
                        ImageHelper.setImage(civEditAvatar, selectedImageUri, this);
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
                                Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();
                                Log.i("Doctor Profile  ", " Activity " + error);
                            }
                        }).uploadImage(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), ImageFilePath.getPath(this, selectedImageUri));
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
                if (requestCode == PLACE_PICKER_REQUEST) {
                    Place place = PlacePicker.getPlace(this, data);
                    user.setLocation_lat(place.getLatLng().latitude);
                    user.setLocation_long(place.getLatLng().longitude);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            Log.i("Doctor Profile  ", " Activity ", e);
        }
    }


    private void bindData() {

        //Helper.setImage(getApplicationContext(), Constants.CHAT_SERVER_URL_IMAGE + "/" + user.getAvatar(), civEditAvatar, R.drawable.placeholder);
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            ImageHelper.setImage(civEditAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + user.getAvatar(), this);
        }


        tvTelephone.setText(user.getPhone());
        etTelephone.setText(user.getPhone());
        ratingBar.setRating(user.getRate_avr());
        tvLocation.setText(user.getAddress());
        tvLocations.setText(user.getAddress());
        //ImageHelper.setImage(ivLocation, Constants.CHAT_SERVER_URL_IMAGE + "/" + user.getCountry_flag(), getApplicationContext());
        ImageHelper.setCountryImage(ivLocation, user.getCountry_flag());


//        loadQRCode(tv_qr_code);
        tvTelephone.setText(user.getPhone());
        et_location.setText(user.getAddress());
        if (is_me) {
            ivEdit.setVisibility(View.VISIBLE);
            iSave.setVisibility(View.GONE);

        } else {
            ivEdit.setVisibility(View.GONE);
            iSave.setVisibility(View.GONE);

        }
        setAdapters();
        tvRating.setText("Rating  " + String.valueOf(user.getRate_count()) + " (" + String.valueOf(user.getRate_avr()) + " Reviews)");

        tvSpecilities.setText("");
        if (user.getSpecialities() != null)
            for (ChooseModel speciality : user.getSpecialities()) {
                tvSpecilities.append(speciality.getSpeciality_title() + " ");
            }
        if (user.getOpen_time() != null)
            getTimaTableData(user.getOpen_time());

        if (user.isClinic == 1) {
            llPracticeProfile.setVisibility(View.VISIBLE);
            vBorder.setVisibility(View.VISIBLE);
            if (user.getLocation_img() != null && !user.getLocation_img().isEmpty()) {
                mapContanier.setVisibility(View.VISIBLE);
                ImageHelper.setImage(location_img, Constants.CHAT_SERVER_URL_IMAGE + "/" + user.getLocation_img(), this);
            } else
                mapContanier.setVisibility(View.GONE);
        } else {
            llPracticeProfile.setVisibility(View.GONE);
            vBorder.setVisibility(View.GONE);
            mapContanier.setVisibility(View.GONE);

        }

        // ivEdit ahmed 12 - 6 - 2017
        if (user.getInfo() != null) {
            etLocation.setText(user.getAddress());
            etStreetName.setText(user.getInfo().getStreetname());
            etHouseNumber.setText(user.getInfo().getHouseNumber());
            etZipCode.setText(user.getInfo().getZipCode());
            etProvince.setText(user.getInfo().getProvinz());
            etCountry.setText(user.getInfo().getCountry());
        }
        if (user.get_Id() == Integer.parseInt(prefManager.getData(PrefManager.USER_ID))) {
            is_me = true;
            tvToolbarName.setText(getResources().getString(R.string.my_profile));
            edAddToFavourite.setText(user.getFirst_name());
            tvContact.setText(user.getLast_name());
            tvOnline.setText(user.getSubTitle());

        } else {
            is_me = false;
            if (user.isClinic == 1)
                tvToolbarName.setText(user.getFirst_name());
            else
                tvToolbarName.setText(user.getLast_name() + ", " + user.getFirst_name());
            edAddToFavourite.setText(R.string.add_to);
            tvContact.setText(R.string.contact_by_chat);
            if (user.getIs_available() != null && user.getIs_available().equals("1"))
                tvOnline.setText(R.string.status_online);
            else
                tvOnline.setText(R.string.status_offline);
        }
        chechEditPermission();
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

    private void getTimaTableData(List<Table> list) {
        if (user.getOpen_Type() == 3)
            tvNoTime.setText("permenant_closed");
        else
            tvNoTime.setText("Always Open");

        if (list.size() > 0) {
            llNoTime.setVisibility(View.GONE);
            tablelayout.removeAllViews();
            com.germanitlab.kanonhealth.helpers.TimeTable timeTable = new com.germanitlab.kanonhealth.helpers.TimeTable();
            timeTable.creatTimeTable(list, this, tablelayout);
        } else
            llNoTime.setVisibility(View.VISIBLE);

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

        if (user.getIs_my_doctor().equals("0")) {
            new HttpCall(this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    user.setIs_my_doctor("1");
                    checkDoctor();
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
                    user.setIs_my_doctor("0");
                    checkDoctor();
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
        if (is_me)
            return;
        try {
            if (user.getIs_my_doctor() != null && !TextUtils.isEmpty(user.getIs_my_doctor()) && user.getIs_my_doctor().equals("0"))
                edAddToFavourite.setText(getString(R.string.add_to));
            else
                edAddToFavourite.setText(getString(R.string.remove_from));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadQRCode(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                dialog = new Dialog(DoctorProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialoge);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                ImageView imageView = (ImageView) dialog.findViewById(R.id.image);
                imageView.setImageResource(R.drawable.qr);
                if (user.getQr_url() != null && user.getQr_url() != "") {
                    ImageHelper.setImage(imageView, Constants.CHAT_SERVER_URL + "/" + user.getQr_url(), R.drawable.qr, DoctorProfileActivity.this);
                }
                dialog.show();
            }
        });
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
                for (ChooseModel speciality : user.getSpecialities()) {
                    tvSpecilities.append(speciality.getSpeciality_title() + " ");
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
                set(adapter, user.getMembers_at(), recyclerView, R.id.member_recycleview, LinearLayoutManager.VERTICAL, Constants.MEMBERAT);
                break;
        }
    }

    @Override
    public void onSuccess(Object response) {
        Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
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
        startActivityForResult(intent, Constants.IMAGE_REQUEST);

    }

    @Override
    public void onCameraClicked() {
        takeImageWithCamera();
    }

    @Override
    public void deleteMyImage() {
        user.setAvatar("");
        ImageHelper.setImage(civEditAvatar, Constants.CHAT_SERVER_URL + "/" + user.getAvatar(), R.drawable.placeholder, this);
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
            textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
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
}
