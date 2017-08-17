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
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.Crop.PickerBuilder;
import com.germanitlab.kanonhealth.adapters.ClinicListAdapter;
import com.germanitlab.kanonhealth.adapters.SpecilaitiesAdapter;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Language;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.models.WorkingHours;
import com.germanitlab.kanonhealth.callback.Message;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.httpchat.HttpChatActivity;
import com.germanitlab.kanonhealth.initialProfile.DialogPickerCallBacks;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.mukesh.countrypicker.Country;
import com.nex3z.flowlayout.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileActivity extends ParentActivity implements DialogPickerCallBacks, Message {

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
    @BindView(R.id.edit_member_list)
    ImageView ivMemberList;
    @BindView(R.id.table_layout_time)
    TableLayout tableLayoutTime;
    @BindView(R.id.nsv_soctor_profile_scroll)
    NestedScrollView nestedScrollView;
    @BindView(R.id.tv_rating)
    TextView textViewRating;
    @BindView(R.id.edit_image)
    CircleImageView editImage;
    @BindView(R.id.tv_no_time)
    TextView tvNoTime;


    UserInfo userInfo;
    File avatar = null;
    SpecilaitiesAdapter adapter;
    ClinicListAdapter clinicListAdapter;
    Boolean is_me = false;
    PickerDialog pickerDialog;
    int PLACE_PICKER_REQUEST = 7;
    private Menu menu;
    String specialityIds = "";
    String langIds = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile_view);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initToolbar();
        try {
            pickerDialog = new PickerDialog(true);
            userInfo = new UserInfo();
            userInfo = (UserInfo) getIntent().getSerializableExtra("doctor_data");
            is_me = userInfo.getUserID() == PrefHelper.get(this, PrefHelper.KEY_USER_ID, -1);
            bindData();
            setVisiblitiy();

        } catch (Exception e) {
            Toast.makeText(this, getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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
                tvOnline.setFocusableInTouchMode(true);
                edAddToFavourite.setFocusableInTouchMode(true);
                tvContact.setFocusableInTouchMode(true);
                ivLanguagesList.setVisibility(View.VISIBLE);
                ivSpecialityList.setVisibility(View.VISIBLE);
                etStreetName.setEnabled(true);
                etHouseNumber.setEnabled(true);
                etZipCode.setEnabled(true);
                etProvince.setEnabled(true);
                textViewPhone.setEnabled(true);
                editImage.setVisibility(View.VISIBLE);
                ivTimeTable.setVisibility(View.VISIBLE);
//                ivMemberList.setVisibility(View.VISIBLE);
                break;
            case R.id.mi_save:
                changeGravity(tvOnline, false);
                changeGravity(edAddToFavourite, false);
                changeGravity(tvContact, false);
                ivLanguagesList.setVisibility(View.GONE);
                ivSpecialityList.setVisibility(View.GONE);
//                ivMemberList.setVisibility(View.GONE);
                etStreetName.setEnabled(false);
                etHouseNumber.setEnabled(false);
                etZipCode.setEnabled(false);
                etProvince.setEnabled(false);
                textViewPhone.setEnabled(false);
                tvOnline.setFocusable(false);
                edAddToFavourite.setFocusable(false);
                tvContact.setFocusable(false);
                editImage.setVisibility(View.GONE);
                ivTimeTable.setVisibility(View.GONE);
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
        intent.putExtra("userInfo", userInfo);
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

        showProgressBar();
        // pickerDialog.dismiss();
//        ImageHelper.setImage(circleImageViewAvatar, avatar.getAbsolutePath());
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = ApiHelper.editDoctor(DoctorProfileActivity.this, userInfo, avatar);
                if (result) {
                    // success
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressBar();
                            Toast.makeText(DoctorProfileActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
                            menu.findItem(R.id.mi_edit).setVisible(true);
                            menu.findItem(R.id.mi_save).setVisible(false);

                        }
                    });


                    // get url and save it in sharePref
                    //user.setAvatar(uploadImageResponse.getFile_url());
                    //prefManager.put(PrefManager.PROFILE_IMAGE, uploadImageResponse.getFile_url());
                } else {
                    // failed
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressBar();
                            if (avatar != null)
                                circleImageViewAvatar.setImageResource(R.drawable.placeholder);
                            Toast.makeText(DoctorProfileActivity.this, R.string.error_saving_data, Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        }).start();

    }

    private void setVisiblitiy() {
//        if (is_me) {
//            tvOnline.setFocusableInTouchMode(true);
//            edAddToFavourite.setFocusableInTouchMode(true);
//            tvContact.setFocusableInTouchMode(true);
//        } else {
        tvOnline.setFocusable(false);
        edAddToFavourite.setFocusable(false);
        tvContact.setFocusable(false);
        // }
    }

    @OnClick(R.id.edit_time_table)
    public void editTimeTable(View view) {
        try {
            Intent intent = new Intent(this, TimeTable.class);
            intent.putExtra(Constants.DATA, userInfo.getTimeTable());
            intent.putExtra("type", userInfo.getOpenType());
            startActivityForResult(intent, Constants.HOURS_CODE);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Add Practics Tag", "Add Practics about Exception ", e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
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
                    userInfo.setTimeTable((ArrayList<WorkingHours>) data.getSerializableExtra("list"));
                    userInfo.setOpenType(data.getIntExtra("type", 0));
                    getTimaTableData();
                    break;
                case Constants.HOURS_TYPE_CODE:
                    userInfo.setOpenType(data.getIntExtra("type", 0));
                    break;
            }
        }
    }

    private void bindData() {

        if (userInfo.getAvatar() != null && !userInfo.getAvatar().isEmpty()) {
            ImageHelper.setImage(circleImageViewAvatar, ApiHelper.SERVER_IMAGE_URL + "/" + userInfo.getAvatar(), R.drawable.placeholder);
        }
        if (is_me) {
            tvToolbarName.setText(getResources().getString(R.string.my_profile));
            edAddToFavourite.setText(userInfo.getFirstName());
            tvContact.setText(userInfo.getLastName());
            tvOnline.setText(userInfo.getTitle());

        } else {
            tvToolbarName.setText(userInfo.getFullName());
            checkDoctor();
            tvContact.setText(R.string.contact_by_chat);
            if (userInfo.getAvailable() == 1)
                tvOnline.setText(R.string.status_online);
            else
                tvOnline.setText(R.string.status_offline);
        }

        textViewRating.setText(getResources().getString(R.string.rating) + "  " + String.valueOf(userInfo.getRateNum()) + " (" + String.valueOf(userInfo.getRateNum()) + " " + getResources().getString(R.string.reviews) + ")");

        //setRate
        Float rate = userInfo.getRateNum();
        ratingBar.setRating(rate);


        // set specialities
        setSpecialities();
        setLanguages();
        setClinics();
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


        // member at need handle

        // TimeTable
        getTimaTableData();
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
        bundle.putSerializable(Constants.CHOSED_LIST, userInfo.getSupportedLangs());
        showDialogFragment(bundle);
    }

    @OnClick(R.id.edit_member_list)
    public void edit_member_list() {
        Bundle bundle = new Bundle();
        bundle.putInt("Constants", Constants.DoctorAll);
        bundle.putSerializable(Constants.CHOSED_LIST, userInfo.getClinics());
        showDialogFragment(bundle);
    }

    private void getTimaTableData() {
        tableLayoutTime.removeAllViews();
        tvNoTime.setVisibility(View.VISIBLE);
        if (userInfo.getOpenType() == 3) {
            tvNoTime.setText(R.string.permenant_closed);
        } else if (userInfo.getOpenType() == 1) {
            tvNoTime.setText(R.string.always_open);
        } else if (userInfo.getOpenType() == 2) {
            tvNoTime.setText(R.string.no_hours_available);
        } else {

            if (userInfo.getTimeTable() != null && userInfo.getTimeTable().size() > 0) {
                tvNoTime.setVisibility(View.GONE);
                tableLayoutTime.removeAllViews();
                com.germanitlab.kanonhealth.helpers.TimeTable timeTable = new com.germanitlab.kanonhealth.helpers.TimeTable();
                timeTable.creatTimeTable(userInfo.getTimeTable().get(0), this, tableLayoutTime);
            } else
                tvNoTime.setVisibility(View.VISIBLE);
            tvNoTime.setText(R.string.no_time_has_set);
        }
    }


    @OnClick(R.id.image_star)
    public void image_star() {
        Intent intent = new Intent(this, RateActivity.class);
        intent.putExtra("doctor_info", userInfo);
        intent.putExtra("type", "doctor");
        startActivity(intent);
    }

    @OnClick(R.id.ed_add_to_favourite)
    public void addToMyDoctor() {
        if (is_me)
            return;
        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean result;
                if (userInfo.getIsMyDoc() == 1) {
                    result = ApiHelper.setFavouriteOperation(String.valueOf(PrefHelper.get(DoctorProfileActivity.this, PrefHelper.KEY_USER_ID, -1)), userInfo.getUserID().toString(), userInfo.getUserType(), false);
                    if (result)
                        userInfo.setIsMyDoc(0);
                } else {
                    result = ApiHelper.setFavouriteOperation(String.valueOf(PrefHelper.get(DoctorProfileActivity.this, PrefHelper.KEY_USER_ID, -1)), userInfo.getUserID().toString(), userInfo.getUserType(), true);
                    if (result)
                        userInfo.setIsMyDoc(1);
                }
                DoctorProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result) {
                            checkDoctor();
                        }
                        hideProgressBar();
                    }
                });
            }
        }).start();
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
    private void setSpecialities() {
        if (userInfo.getSpecialities() != null) {

            tvSpecilities.setText("");
            specialityIds = "";
            flSpeciliaty.removeAllViews();
            int size = 0;
            for (Speciality speciality : userInfo.getSpecialities()) {
                flSpeciliaty.addView(ImageHelper.setImageCircle(speciality.getImage(), this));
                tvSpecilities.append(speciality.getTitle());
                specialityIds = specialityIds.concat(String.valueOf(speciality.getSpecialityID()));
                size++;
                if (size < userInfo.getSpecialities().size()) {
                    tvSpecilities.append(", ");
                    specialityIds = specialityIds.concat(",");
                }
            }
        }

    }

    private void setLanguages() {
        if (userInfo.getSupportedLangs() != null) {
            langIds = "";
            flLanguages.removeAllViews();
            int size = 0;
            for (Language language : userInfo.getSupportedLangs()) {
                if (!TextUtils.isEmpty(language.getLanguageCountryCode())) {
                    Country country = Country.getCountryByISO(language.getLanguageCountryCode());
                    if (country != null) {
                        flLanguages.addView(ImageHelper.setImageHeart(country.getFlag(), getApplicationContext()));
                    }
                    langIds = langIds.concat(String.valueOf(language.getLanguageID()));
                    if (userInfo.getSupportedLangs().size() > size + 1) {
                        langIds = langIds.concat(",");
                        size++;
                    }
                }
            }
        }
    }

    private void setClinics() {
        ArrayList<UserInfo> allClinics = new ArrayList<>();
        allClinics.addAll(userInfo.getClinics());
        allClinics.addAll(userInfo.getMyClinics());
        if (allClinics.size() > 0) {
            RecyclerView recyclerVie;
            clinicListAdapter = new ClinicListAdapter(allClinics, this);
            recyclerVie = (RecyclerView) findViewById(R.id.member_recycleview);
            recyclerVie.setHasFixedSize(true);
            recyclerVie.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerVie.setNestedScrollingEnabled(false);
            recyclerVie.setAdapter(clinicListAdapter);
        }
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
        avatar = null;
        ImageHelper.setImage(circleImageViewAvatar, ApiHelper.SERVER_IMAGE_URL + "/" + userInfo.getAvatar(), R.drawable.placeholder);
        PrefHelper.put(getApplicationContext(), PrefHelper.KEY_PROFILE_IMAGE, "");
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
        avatar = new File(ImageFilePath.getPath(this, uri));
        ImageHelper.setImage(circleImageViewAvatar, uri);
        pickerDialog.dismiss();
    }

    @Override
    public void returnChoseSpecialityList(ArrayList<Speciality> specialitiesArrayList) {
        userInfo.setSpecialities(specialitiesArrayList);
        setSpecialities();
    }

    @Override
    public void returnChoseLanguageList(ArrayList<Language> languageArrayList) {
        userInfo.setSupportedLangs(languageArrayList);
        setLanguages();

    }

    @Override
    public void returnChoseDoctorList(ArrayList<UserInfo> doctorArrayList) {
//        userInfo.setMyClinics(doctorArrayList);
//        setMemberDoctors();

    }
}
