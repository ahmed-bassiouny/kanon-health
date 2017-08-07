package com.germanitlab.kanonhealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.germanitlab.kanonhealth.adapters.SpecilaitiesAdapter;
import com.germanitlab.kanonhealth.api.models.Clinic;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.SupportedLang;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.httpchat.HttpChatActivity;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.models.Table;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mukesh.countrypicker.Country;
import com.nex3z.flowlayout.FlowLayout;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClinicProfileActivity extends AppCompatActivity {

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
    @BindView(R.id.table_layout_time)
    TableLayout tableLayoutTime;
    @BindView(R.id.nsv_soctor_profile_scroll)
    NestedScrollView nestedScrollView;
    @BindView(R.id.tv_rating)
    TextView textViewRating;
    @BindView(R.id.img_map)
    ImageView imageViewMap;
    //---------------------
    Clinic clinic;
    SpecilaitiesAdapter adapter;
    PrefManager prefManager;
    PickerDialog pickerDialog;
    int PLACE_PICKER_REQUEST = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_profile);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initToolbar();
        try {
            clinic = new Clinic();
            clinic = (Clinic) getIntent().getSerializableExtra("clinic_data");
            prefManager = new PrefManager(this);
            pickerDialog = new PickerDialog(true);

            bindData();
            setVisiblitiy();

        } catch (Exception e) {
            Toast.makeText(this, getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            finish();
        }

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
        Intent intent = new Intent(this, HttpChatActivity.class);
        intent.putExtra("doctorID", clinic.getId());
        startActivity(intent);
    }

    private void setVisiblitiy() {
            tvOnline.setFocusableInTouchMode(true);
            edAddToFavourite.setFocusableInTouchMode(true);
            tvContact.setFocusableInTouchMode(true);

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



    private void bindData() {


        if (clinic.getAvatar() != null && !clinic.getAvatar().isEmpty()) {
            ImageHelper.setImage(circleImageViewAvatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + clinic.getAvatar());
        }

            tvToolbarName.setText(clinic.getName());
            checkDoctor();
            tvContact.setText(R.string.contact_by_chat);
            if (clinic.getAvailable() == 1)
                tvOnline.setText(R.string.status_online);
            else
                tvOnline.setText(R.string.status_offline);

        textViewRating.setText(getResources().getString(R.string.rating) + "  " + String.valueOf(clinic.getRateNum()) + " (" + String.valueOf(clinic.getRateNum()) + " " + getResources().getString(R.string.reviews) + ")");

        // set specialities
        if (clinic.getSpeciality() != null) {
            String specialities = "";
            for (Speciality speciality : clinic.getSpeciality()) {
                specialities += speciality.getTitle() + ", ";
            }
            specialities = specialities.substring(0, specialities.length() - 2);
            tvSpecilities.setText(specialities);
        }
        // end specialities
        // set country
        String countryDail = clinic.getCountry();
        if (!TextUtils.isEmpty(countryDail)) {
            Country country = null;
            for (Country c : Country.getAllCountries()) {
                if (c.getDialCode().equals(countryDail)) {
                    country = c;
                }
            }
            if (country != null) {
                imageViewLocation.setImageBitmap(ImageHelper.TrimBitmap(country.getFlag(), ClinicProfileActivity.this));
                Locale l = new Locale("", country.getCode());
                if (l != null) {
                    tvLocation.setText(l.getDisplayCountry(Locale.getDefault()));
                }
            }
        }
        // end country
        etStreetName.setText(clinic.getStreetName());
        etHouseNumber.setText(clinic.getHouseNumber());
        etZipCode.setText(clinic.getZipCode());
        etProvince.setText(clinic.getProvince());
        textViewPhone.setText(clinic.getPhone());


        if (clinic.getSupportedLangs() != null) {
            for (SupportedLang supportedLang : clinic.getSupportedLangs()) {
                String country_code = supportedLang.getCountryCode();
                Country country = Country.getCountryByISO(country_code);
                if (country != null) {
                    flLanguages.addView(ImageHelper.setImageHeart(country.getFlag(), getApplicationContext()));
                }
            }
        }

        // member at need handle
        if (clinic.getDoctors().size() > 0) {
            /*set(adapter, userInfo.getClinics(), R.id.member_recycleview, LinearLayoutManager.VERTICAL, Constants.MEMBERAT);
            RecyclerView recyclerVie;
            adapter = new SpecilaitiesAdapter(list, getApplicationContext(), type);
            recyclerVie = (RecyclerView) findViewById(id);
            recyclerVie.setHasFixedSize(true);
            recyclerVie.setLayoutManager(new LinearLayoutManager(this, linearLayoutManager, false));
            recyclerVie.setNestedScrollingEnabled(false);
            recyclerVie.setAdapter(adapter);*/
        }

        if(clinic.getLocationLat()>0.0 &&clinic.getLocationLong()>0.0){
            String URL = "http://maps.google.com/maps/api/staticmap?center=" + String.valueOf(clinic.getLocationLat()) + "," + String.valueOf(clinic.getLocationLong()) + "&zoom=15&size=200x200&sensor=false";
            ImageHelper.setImage(imageViewLocation, URL, -1);
        }
        // TimeTable
        getTimeTableData(clinic.getTimeTable());
    }



    @OnClick(R.id.image_star)
    public void image_star() {
        Intent intent = new Intent(this, RateActivity.class);
        intent.putExtra("doc_id", String.valueOf(clinic.getId()));
        startActivity(intent);
    }

    @OnClick(R.id.ed_add_to_favourite)
    public void addToMyDoctor() {
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
        if (clinic.getIsMyDoc() == 0)
            edAddToFavourite.setText(getString(R.string.add_to_my_doctors));
        else
            edAddToFavourite.setText(getString(R.string.remove_from));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    @Override
    protected void onResume() {
        super.onResume();
        nestedScrollView.fullScroll(View.FOCUS_UP);
    }
    private void getTimeTableData(Table list) {
        if (clinic != null) {
            if (list != null) {
                if (list != null) {
                    tableLayoutTime.removeAllViews();
                    com.germanitlab.kanonhealth.helpers.TimeTable timeTable = new com.germanitlab.kanonhealth.helpers.TimeTable();
                    timeTable.creatTimeTable(list, this, tableLayoutTime);
                }
            }
        }
    }

}
