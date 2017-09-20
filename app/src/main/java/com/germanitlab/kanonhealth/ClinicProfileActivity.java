package com.germanitlab.kanonhealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.germanitlab.kanonhealth.adapters.DoctorListAdapter;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Language;
import com.germanitlab.kanonhealth.api.models.Speciality;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.responses.IsOpenResponse;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.ParentActivity;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.httpchat.DocumentChatAdapter;
import com.germanitlab.kanonhealth.httpchat.HttpChatActivity;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.inquiry.InquiryActivity;
import com.google.gson.Gson;
import com.mukesh.countrypicker.Country;
import com.nex3z.flowlayout.FlowLayout;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class
ClinicProfileActivity extends ParentActivity {

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

    @BindView(R.id.tv_languages)
    TextView tvLanguages;

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
    @BindView(R.id.tv_no_time)
    TextView tvNoTime;
    @BindView(R.id.view_below_province)
    View view_below_province;
    @BindView(R.id.document_recycleview)
    RecyclerView document_recycleview;
    @BindView(R.id.tv_location_name)
    TextView location;
    //---------------------
    UserInfo clinic;
    PickerDialog pickerDialog;
    int PLACE_PICKER_REQUEST = 7;
    DoctorListAdapter doctorListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_profile);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initToolbar();
        try {
            clinic = new UserInfo();
            clinic = (UserInfo) getIntent().getSerializableExtra("clinic_data");
            pickerDialog = new PickerDialog(true);
            bindData();
            setVisiblitiy();

        } catch (Exception e) {
            Toast.makeText(this, getResources().getText(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                //*********** it's comment becuase i am waiting karim finish task
                final IsOpenResponse result = ApiHelper.getIsOpen(PrefHelper.get(ClinicProfileActivity.this, PrefHelper.KEY_USER_ID, -1),clinic.getId(),clinic.getUserType());

                ClinicProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.getStatus()==1){
                            clinic.setIsSessionOpen(1);
                            clinic.setRequestID(result.getRequestId());
                            clinic.setUserType(UserInfo.CLINIC);
                            Intent intent = new Intent(ClinicProfileActivity.this, HttpChatActivity.class);
                            intent.putExtra("userInfo", clinic);
                            intent.putExtra("doctorID", clinic.getId());
                            intent.putExtra("type",clinic.getUserType());
                            startActivity(intent);
                        }else {
                            clinic.setIsSessionOpen(0);
//                            Intent intent = new Intent(ClinicProfileActivity.this, InquiryActivity.class);
//                            intent.putExtra("doctor_data", new Gson().toJson(clinic));
//                            startActivity(intent);
                            clinic.setRequestID(result.getRequestId());
                            clinic.setUserType(UserInfo.CLINIC);
                            Intent intent = new Intent(ClinicProfileActivity.this, HttpChatActivity.class);
                            intent.putExtra("userInfo", clinic);
                            intent.putExtra("doctorID", clinic.getId());
                            intent.putExtra("type",clinic.getUserType());
                            startActivity(intent);
                        }
                    }
                });
            }
        }).start();
    }

    private void setVisiblitiy() {
        tvOnline.setFocusable(false);
        edAddToFavourite.setFocusable(false);
        tvContact.setFocusable(false);
    }




    private void bindData() {


        if (clinic.getAvatar() != null && !clinic.getAvatar().isEmpty()) {
            ImageHelper.setImage(circleImageViewAvatar, ApiHelper.SERVER_IMAGE_URL + "/" + clinic.getAvatar(), R.drawable.placeholder);
        }

        tvToolbarName.setText(clinic.getName());
        checkDoctor();
        tvContact.setText(R.string.contact_by_chat);
        if (clinic.getAvailable() == 1)
            tvOnline.setText(R.string.status_online);
        else
            tvOnline.setText(R.string.status_offline);

        textViewRating.setText(getResources().getString(R.string.rating) + "  " + String.valueOf(clinic.getRateNum()) + " (" + String.valueOf(clinic.getRateCount()) + " " + getResources().getString(R.string.reviews) + ")");

        // set specialities
        if (clinic.getSpecialities() != null) {
            tvSpecilities.setText("");
            flSpeciliaty.removeAllViews();
            int size = 0;
            for (Speciality speciality : clinic.getSpecialities()) {
                flSpeciliaty.addView(ImageHelper.setImageCircleSpecial(speciality.getImage(), this));
                tvSpecilities.append(speciality.getTitle());
                size++;
                if (size < clinic.getSpecialities().size()) {
                    tvSpecilities.append(", ");
                }
            }


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
        etProvince.setText(clinic.getProvidence());
        textViewPhone.setText(clinic.getPhone());
        ratingBar.setRating(clinic.getRateNum());


        if (clinic.getSupportedLangs() != null) {
            tvLanguages.setText("");
            int size = 0;
            for (Language supportedLang : clinic.getSupportedLangs()) {
                String country_code = supportedLang.getLanguageCountryCode();
                Country country = Country.getCountryByISO(country_code);
                if (country != null) {
                    flLanguages.addView(ImageHelper.setImageHeart(country.getFlag(), getApplicationContext()));
                    tvLanguages.append(supportedLang.getLanguageTitle());
                    if (clinic.getSupportedLangs().size() > size + 1) {
                        tvLanguages.append(", ");
                        size++;
                    }
                }
            }
        }

        // member at need handle
        if (clinic.getClinics().size() > 0) {
            RecyclerView recyclerVie;
            doctorListAdapter = new DoctorListAdapter(clinic.getClinics(), this);
            recyclerVie = (RecyclerView) findViewById(R.id.member_recycleview);
            recyclerVie.setHasFixedSize(true);
            recyclerVie.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerVie.setNestedScrollingEnabled(false);
            recyclerVie.setAdapter(doctorListAdapter);
        }

        // TimeTable
        getTimaTableData();
        if(!clinic.getLocationLat().equals("") && !clinic.getLocationLong().equals("")){
            String URL = "http://maps.google.com/maps/api/staticmap?center=" + clinic.getLocationLat() + "," + clinic.getLocationLong()+ "&zoom=15&size=200x200&sensor=false";
            ImageHelper.setImage(imageViewMap, URL, -1);
        }else{
            imageViewMap.setVisibility(View.GONE);
            view_below_province.setVisibility(View.GONE);
            //location.setVisibility(View.GONE);
        }
        setDocuments();
    }

    private void setDocuments() {
        if (clinic.getDocuments() != null) {
            DocumentChatAdapter doctorDocumentAdapter = new DocumentChatAdapter(clinic.getDocuments(), this,false);
            document_recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            document_recycleview.setAdapter(doctorDocumentAdapter);
            document_recycleview.setBackgroundResource(R.color.chatbackground_gray);
        }
    }


    @OnClick(R.id.image_star)
    public void image_star() {
        Intent intent = new Intent(this, RateActivity.class);
        intent.putExtra("clinic_info", clinic);
        intent.putExtra("type", "clinic");
        startActivity(intent);
    }

    @OnClick(R.id.ed_add_to_favourite)
    public void addToMyDoctor() {
        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean result ;
                if(clinic.getIsMyDoc()==1){
                    result = ApiHelper.setFavouriteOperation(String.valueOf(PrefHelper.get(ClinicProfileActivity.this,PrefHelper.KEY_USER_ID,-1)),clinic.getUserID().toString(),clinic.getUserType(),false);
                    if(result)
                        clinic.setIsMyDoc(0);
                }else{
                    result = ApiHelper.setFavouriteOperation(String.valueOf(PrefHelper.get(ClinicProfileActivity.this,PrefHelper.KEY_USER_ID,-1)),clinic.getUserID().toString(),clinic.getUserType(),true);
                    if(result)
                        clinic.setIsMyDoc(1);
                }
                ClinicProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result) {
                            checkDoctor();
                        }
                        hideProgressBar();
                    }
                });
            }
        }).start();
    }

    private void checkDoctor() {
        if (clinic.getIsMyDoc() == 0)
            edAddToFavourite.setText(getString(R.string.add_to_my_clinics));
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
        if (Helper.isNetworkAvailable(getApplicationContext())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserInfo temp = ApiHelper.postGetClinic( clinic.getId(),ClinicProfileActivity.this);
                    if (temp != null) {
                        clinic=temp;
                        ClinicProfileActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                clinic.setUserType(UserInfo.CLINIC);
                                bindData();
                                setVisiblitiy();
                            }
                        });
                    }
                }
            }).start();
        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void getTimaTableData() {
        tableLayoutTime.removeAllViews();
        tvNoTime.setVisibility(View.VISIBLE);
        if (clinic.getOpenType() == 2) {
            tvNoTime.setText(R.string.permenant_closed);
        }
        else if(clinic.getOpenType() == 1) {
            tvNoTime.setText(R.string.always_open);
        }
        else if(clinic.getOpenType()==4) {
            tvNoTime.setText(R.string.no_hours_available);
        }else {

            if (clinic.getTimeTable() != null ) {
                tvNoTime.setVisibility(View.GONE);
                tableLayoutTime.removeAllViews();
                com.germanitlab.kanonhealth.helpers.TimeTable timeTable = new com.germanitlab.kanonhealth.helpers.TimeTable();
                timeTable.creatTimeTable(clinic.getTimeTable(), this, tableLayoutTime);
            } else {
                tvNoTime.setVisibility(View.VISIBLE);
                tvNoTime.setText(R.string.no_time_has_set);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }


}
