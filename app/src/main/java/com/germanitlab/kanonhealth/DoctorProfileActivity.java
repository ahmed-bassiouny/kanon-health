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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.Table;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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

public class DoctorProfileActivity extends ParentActivity implements DialogPickerCallBacks {

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
    @BindView(R.id.table_layout_time)
    TableLayout tableLayoutTime;
    @BindView(R.id.nsv_soctor_profile_scroll)
    NestedScrollView nestedScrollView;
    @BindView(R.id.tv_rating)
    TextView textViewRating;
    UserInfo userInfo;
    File avatar = null;
    SpecilaitiesAdapter adapter;
    Boolean is_me = false;
    PrefManager prefManager;
    PickerDialog pickerDialog;
    int PLACE_PICKER_REQUEST = 7;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile_view);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initToolbar();
        try {
            is_me = userInfo.getUserID() == Integer.parseInt(prefManager.getData(PrefManager.USER_ID));
            userInfo = new UserInfo();
            userInfo = (UserInfo) getIntent().getSerializableExtra("doctor_data");
            prefManager = new PrefManager(this);
            pickerDialog = new PickerDialog(true);
            bindData();
            setVisiblitiy();

        } catch (Exception e) {
            Toast.makeText(this, getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (menu != null) {
                getMenuInflater().inflate(R.menu.menu_doctor_profile, menu);
                menu.findItem(R.id.mi_save).setVisible(false);
                menu.findItem(R.id.mi_edit).setVisible(true);
                menu.findItem(R.id.mi_save).setVisible(false);
                if (is_me) {
                    menu.findItem(R.id.mi_edit).setVisible(true);

                } else {
                    menu.findItem(R.id.mi_edit).setVisible(false);
                }
                this.menu = menu;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.mi_edit:
                menu.findItem(R.id.mi_save).setVisible(true);
                menu.findItem(R.id.mi_edit).setVisible(false);
                tvOnline.setText(userInfo.getTitle());
                edAddToFavourite.setText(userInfo.getFirstName());
                tvContact.setText(userInfo.getLastName());
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


    @OnClick(R.id.tv_contact)
    public void contactClick(View v) {
        if (is_me)
            return;
        Intent intent = new Intent(this, HttpChatActivity.class);
        intent.putExtra("doctorID", userInfo.getUserID());
        startActivity(intent);
    }


    private void handleNewData() {
        if (tvContact.getText().toString().trim().isEmpty() || edAddToFavourite.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.please_fill_data, Toast.LENGTH_SHORT).show();
            return;
        }
        userInfo.setTitle(tvOnline.getText().toString());
        userInfo.setLastName(tvContact.getText().toString());
        userInfo.setFirstName(edAddToFavourite.getText().toString());
        userInfo.setStreetName(etStreetName.getText().toString());
        userInfo.setHouseNumber(etHouseNumber.getText().toString());
        userInfo.setZipCode(etZipCode.getText().toString());
        userInfo.setProvidence(etProvince.getText().toString());

        sendDataToserver();
    }

    private void sendDataToserver() {

        ProgressHelper.showProgressBar(DoctorProfileActivity.this);
        pickerDialog.dismiss();
        ImageHelper.setImage(circleImageViewAvatar, avatar.getAbsolutePath());
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = ApiHelper.editDoctor(DoctorProfileActivity.this, userInfo, avatar);
                if (result) {
                    // success
                    ProgressHelper.hideProgressBar();
                    Toast.makeText(DoctorProfileActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
                    menu.findItem(R.id.mi_edit).setVisible(true);
                    menu.findItem(R.id.mi_save).setVisible(false);

                    // get url and save it in sharePref
                    //user.setAvatar(uploadImageResponse.getFile_url());
                    //prefManager.put(PrefManager.PROFILE_IMAGE, uploadImageResponse.getFile_url());
                } else {
                    // falied
                    ProgressHelper.hideProgressBar();
                    circleImageViewAvatar.setImageResource(R.drawable.profile_place_holder);
                    Toast.makeText(DoctorProfileActivity.this, R.string.error_saving_data, Toast.LENGTH_SHORT).show();

                }
            }
        }).start();

    }

    private void setVisiblitiy() {
        if (!is_me) {
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
        if (userInfo.getOpenType() == 0) {
            Intent intent = new Intent(this, TimeTable.class);
            intent.putExtra(Constants.DATA, userInfo.getTimeTable());
            startActivityForResult(intent, Constants.HOURS_CODE);
        } else {
            Intent intent = new Intent(this, OpeningHoursActivity.class);
            intent.putExtra("type", userInfo.getOpenType());
            intent.putExtra(Constants.DATA, userInfo.getTimeTable());
            startActivityForResult(intent, Constants.HOURS_TYPE_CODE);
        }
    }

    /*private void setAdapters() {
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
    }*/

    /*private void setImage(List<ChooseModel> supported_lang, FlowLayout flowLayout, int i) {
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
    }*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                switch (requestCode) {

                    /*case CROP_PIC :
                        afterCropFinish();
                        break;*/

                    case Constants.HOURS_CODE:
                        userInfo.setTimeTable((Table) data.getSerializableExtra(Constants.DATA));
                        userInfo.setOpenType(data.getIntExtra("type", 0));
                        getTimeTableData(userInfo.getTimeTable());
                        break;
                    case Constants.HOURS_TYPE_CODE:
                        userInfo.setOpenType(data.getIntExtra("type", 0));
                        break;
                }
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

        textViewRating.setText(getResources().getString(R.string.rating) + "  " + String.valueOf(userInfo.getRateNum()) + " (" + String.valueOf(userInfo.getRateNum()) + " " + getResources().getString(R.string.reviews) + ")");

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
        // TimeTable
        getTimeTableData(userInfo.getTimeTable());
    }


    @OnClick(R.id.edit_speciality_list)
    public void editSpecialityList(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("Constants", Constants.SPECIALITIES);
        bundle.putSerializable(Constants.CHOSED_LIST, userInfo.getSpecialities());
        showDialogFragment(bundle);
    }

    @OnClick(R.id.edit_languages_list)
    public void edit_languages_list() {
        Bundle bundle = new Bundle();
        bundle.putInt("Constants", Constants.LANGUAUGE);
        bundle.putSerializable(Constants.CHOSED_LIST,userInfo.getSupportedLangs());
        showDialogFragment(bundle);
    }

    @OnClick(R.id.edit_member_list)
    public void edit_member_list() {
        Bundle bundle = new Bundle();
        bundle.putInt("Constants", Constants.DoctorAll);
        bundle.putSerializable(Constants.CHOSED_LIST,userInfo.getClinics());
        showDialogFragment(bundle);
    }

    private void getTimeTableData(Table list) {
        if (userInfo != null) {
            if (list != null) {
                if (list != null) {
                    tableLayoutTime.removeAllViews();
                    com.germanitlab.kanonhealth.helpers.TimeTable timeTable = new com.germanitlab.kanonhealth.helpers.TimeTable();
                    timeTable.creatTimeTable(list, this, tableLayoutTime);
                }
            }
        }
    }


    @OnClick(R.id.image_star)
    public void image_star() {
        Intent intent = new Intent(this, RateActivity.class);
        intent.putExtra("doc_id", String.valueOf(userInfo.getUserID()));
        startActivity(intent);
    }

    @OnClick(R.id.ed_add_to_favourite)
    public void addToMyDoctor() {
        /*if (is_me)
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
*/
    }

    private void checkDoctor() {
        if (userInfo.getIsMyDoc() == 0)
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

    /*@Override
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
                break;
            case Constants.DoctorAll:
                user.getMembers_at().clear();
                for (ChooseModel item : specialitiesArrayList) {
                    if (item.getIsMyChoise())
                        templist.add(item);
                }
                user.setMembers_at(templist);
                if (userInfo.getMembers_at().size() > 0) {
                    set(adapter, userInfo.getMembers_at(), recyclerView, R.id.member_recycleview, LinearLayoutManager.VERTICAL, Constants.MEMBERAT);
                }
                break;
        }
    }*/


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
        ImageHelper.setImage(circleImageViewAvatar, Constants.CHAT_SERVER_URL + "/" + userInfo.getAvatar(), R.drawable.placeholder);
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


    @Override
    protected void onResume() {
        super.onResume();
        nestedScrollView.fullScroll(View.FOCUS_UP);
    }


    @Override
    public void ImagePickerCallBack(final Uri uri) {
        avatar = new File(ImageFilePath.getPath(DoctorProfileActivity.this, uri));
    }
}
