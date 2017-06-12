package com.germanitlab.kanonhealth;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.germanitlab.kanonhealth.adapters.SpecilaitiesAdapter;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.callback.Message;
import com.germanitlab.kanonhealth.chat.ChatActivity;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.initialProfile.DialogPickerCallBacks;
import com.germanitlab.kanonhealth.initialProfile.PickerDialog;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.*;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.payment.PaymentActivity;
import com.germanitlab.kanonhealth.payment.PreRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileActivity extends AppCompatActivity implements Message<ChooseModel>  , Serializable, ApiResponse, DialogPickerCallBacks {

    @BindView(R.id.speciality_recycleview)
    RecyclerView speciliatyRecycleView;
    SpecilaitiesAdapter adapter;
    List<Object> list;
    List<SpecilaitiesModels> languageList;
    List<SpecilaitiesModels> memberList;
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
    @BindView(R.id.tv_online)
    TextView tv_online;
    @BindView(R.id.img_edit_avatar)
    CircleImageView imageAvatar;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_contact)
    TextView tv_contact;
    @BindView(R.id.tv_add_to_favourite)
    TextView tv_add_to_favourite;
    @BindView(R.id.tv_qr_code)
    TextView tv_qr_code;
    @BindView(R.id.tv_telephone)
    TextView tv_telephone;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.tv_rating)
    TextView tv_rating;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.document_recycler_view)
    RecyclerView document_recycler_view;
    @BindView(R.id.tv_specilities)
    TextView tv_specilities;
    @BindView(R.id.tv_languages)
    TextView tv_languages;
    @BindView(R.id.image_star)
    ImageView image_star;
    @BindView(R.id.edit_time_table)
    ImageView edit_time_table;
    User user;
    UploadImageResponse uploadImageResponse;
    Util util;
    @BindView(R.id.linear_practice_profile)
    LinearLayout linear_practice_profile;


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

    // data of edit
    @BindView(R.id.edit)
    ImageView edit;
    @BindView(R.id.et_last_name)
    EditText et_last_name;
    @BindView(R.id.et_first_name)
    EditText et_first_name;
    @BindView(R.id.et_telephone)
    EditText et_telephone;
    @BindView(R.id.et_location)
    EditText et_location;
    Boolean is_me;
    @BindView(R.id.edit_speciality_list)
    ImageView edit_speciality_list;
    @BindView(R.id.edit_languages_list)
    ImageView edit_languages_list;
    @BindView(R.id.edit_member_list)
    ImageView edit_member_list;
    @BindView(R.id.edit_name_layout)
    LinearLayout edit_name_layout;
    @BindView(R.id.permenant_closed)
    LinearLayout permenant_closed ;
    @BindView(R.id.always_open)
    LinearLayout always_open;
    @BindView(R.id.save)
    ImageView save;
    @BindView(R.id.ll_doctor_data)
    LinearLayout llDoctorData;
    @BindView(R.id.edit_image)
    CircleImageView edit_image;
    private DoctorDocumentAdapter doctorDocumentAdapter;
    PrefManager prefManager;
    PickerDialog pickerDialog;
    private Uri selectedImageUri;
    private static final int TAKE_PICTURE = 1;

    @Override
    protected void onResume() {
        super.onResume();
        bindData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile_view);
        ButterKnife.bind(this);

        // check if doctor or clinic
//
        if(getIntent().getExtras().containsKey("CLINIC")){
            llDoctorData.setVisibility(View.GONE);
            tv_add_to_favourite.setVisibility(View.GONE);
            setVisiblitiy(View.GONE);

        }else {
            llDoctorData.setVisibility(View.VISIBLE);
            tv_add_to_favourite.setVisibility(View.VISIBLE);

            util = Util.getInstance(this);
            user = new User();
            user = (User) getIntent().getSerializableExtra("doctor_data");
            chechEditPermission();
            prefManager = new PrefManager(this);
            pickerDialog = new PickerDialog(true);

        }



    }

    private void chechEditPermission() {
        if (user.get_Id() == AppController.getInstance().getClientInfo().getUser_id())
            is_me = true;
        else
            is_me = false;
    }


    @OnClick(R.id.tv_contact)
    public void contactClick(View v) {
        Gson gson = new Gson();
        if (user.getIsDoc() == 1 && user.getIsOpen() == 1) {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("doctor_data", gson.toJson(user));
            intent.putExtra("from", true);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PaymentActivity.class);
//            intent.putExtra("doctor_obj", user);
            intent.putExtra("doctor_data", user);

            startActivity(intent);
        }

    }

    @OnClick(R.id.edit)
    public void edit(View view) {
        setVisiblitiy(View.GONE);
    }

    @OnClick(R.id.save)
    public void save(View view) {
        handleNewData();
        bindData();
    }

    private void handleNewData() {
        tv_name.setText(et_last_name.getText().toString() + "," + et_first_name.getText().toString());
        user.setLast_name(et_last_name.getText().toString());
        user.setFirst_name(et_first_name.getText().toString());
        tv_location.setText(et_location.getText().toString());
        user.setAddress(et_location.getText().toString());
        tv_telephone.setText(et_telephone.getText().toString());
        user.setPhone(et_telephone.getText().toString());
        // Edit ahmed 12 - 6-2017
        /**

         ed_zip_code.setEnabled(editable);
         ed_city.setEnabled(editable);
         ed_province.setEnabled(editable);
         ed_country.setEnabled(editable);*/
        user.setAddress(ed_location.getText().toString());
        user.getInfo().setStreetname(ed_street_name.getText().toString());
        user.getInfo().setHouseNumber(ed_house_number.getText().toString());
        user.getInfo().setZipCode(ed_zip_code.getText().toString());
        user.getInfo().setProvinz(ed_province.getText().toString());
        user.getInfo().setCountry(ed_country.getText().toString());
        sendDataToserver();
    }

    private void sendDataToserver() {
        util.showProgressDialog();
        new HttpCall(this, this).editProfile(user);

    }

    private void setVisiblitiy(int visiblitiy) {
        int notvisibility = (visiblitiy == View.VISIBLE) ? View.GONE : View.VISIBLE;
        tv_name.setVisibility(visiblitiy);
        et_location.setVisibility(notvisibility);
        tv_location.setVisibility(visiblitiy);
        et_telephone.setVisibility(notvisibility);
        tv_telephone.setVisibility(visiblitiy);
        edit_speciality_list.setVisibility(notvisibility);
        edit_languages_list.setVisibility(notvisibility);
        edit_member_list.setVisibility(notvisibility);
        edit_name_layout.setVisibility(notvisibility);
        edit_time_table.setVisibility(notvisibility);
        edit.setVisibility(visiblitiy);
        save.setVisibility(notvisibility);
        edit_image.setVisibility(notvisibility);

        //Edit ahmed 12-6-2017
        boolean editable=(visiblitiy==View.GONE) ? true:false;
        ed_location.setEnabled(editable);
        ed_street_name.setEnabled(editable);
        ed_house_number.setEnabled(editable);
        ed_zip_code.setEnabled(editable);
        ed_city.setEnabled(editable);
        ed_province.setEnabled(editable);
        ed_country.setEnabled(editable);
    }

    @OnClick(R.id.edit_time_table)
    public void editTimeTable(View view) {
        if (user.getOpen_Type() == 0) {
            Intent intent = new Intent(this, TimeTable.class);
            intent.putExtra(Constants.DATA, (Serializable) user.getOpen_time());
            startActivityForResult(intent, Constants.HOURS_CODE);
        } else {
            Intent intent = new Intent(this, OpeningHoursActivity.class);
            intent.putExtra("type" , user.getOpen_Type());
            intent.putExtra(Constants.DATA, (Serializable) user.getOpen_time());
            startActivityForResult(intent, Constants.HOURS_TYPE_CODE);
        }
    }

    private void setAdapters() {
        RecyclerView recyclerView = new RecyclerView(getApplicationContext());
        set(adapter, user.getSpecialities(), View.GONE, recyclerView, R.id.speciality_recycleview, LinearLayoutManager.HORIZONTAL, Constants.SPECIALITIES);
        set(adapter, user.getSupported_lang(), View.GONE, recyclerView, R.id.language_recycleview, LinearLayoutManager.HORIZONTAL, Constants.LANGUAUGE);
        set(adapter, user.getMembers_at(), View.VISIBLE, recyclerView, R.id.member_recycleview, LinearLayoutManager.VERTICAL, Constants.MEMBERAT);
        if (user.getDocuments() != null) {
            doctorDocumentAdapter = new DoctorDocumentAdapter(user.getDocuments(), getApplicationContext(), this);
            document_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            document_recycler_view.setItemAnimator(new DefaultItemAnimator());
            document_recycler_view.setAdapter(doctorDocumentAdapter);
        }
    }

    public void set(RecyclerView.Adapter adapter, List<ChooseModel> list, int visibilty, RecyclerView recyclerVie, int id, int linearLayoutManager, int type) {

        adapter = new SpecilaitiesAdapter(list, visibilty, getApplicationContext(), type);
        recyclerVie = (RecyclerView) findViewById(id);
        recyclerVie.setHasFixedSize(true);
        recyclerVie.setLayoutManager(new LinearLayoutManager(this, linearLayoutManager, false));
        recyclerVie.setNestedScrollingEnabled(false);
        recyclerVie.setAdapter(adapter);
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

                    break;
                case TAKE_PICTURE:
                    util.showProgressDialog();
                    Log.e("ImageUri", selectedImageUri != null ? selectedImageUri.toString() : "Empty Uri");
/*
                    decodeFile(selectedImageUri.toString());
*/
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

                    break;
                case Constants.HOURS_CODE:
                    user.setOpen_time((List<Table>) data.getSerializableExtra(Constants.DATA));
                    user.setOpen_Type(data.getIntExtra("type" , 0));
                    break;
                case Constants.HOURS_TYPE_CODE :
                    user.setOpen_Type(data.getIntExtra("type" ,0));
            }
            bindData();
        }
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

    private void bindData() {
//        getTimaTableData(user.getTable());
        checkDoctor();
        tv_name.setText(user.getLast_name() + ", " + user.getFirst_name());
        et_last_name.setText(user.getLast_name());
        et_first_name.setText(user.getFirst_name());
        tv_telephone.setText(user.getPhone());
        et_telephone.setText(user.getPhone());
        ratingBar.setRating(user.getRate_avr());
        tv_location.setText(user.getAddress());
        if (user.getIs_available() != null) {
            if (!user.getIs_available().equals("1"))
                tv_online.setText("Offline");
        }
        loadQRCode(tv_qr_code);
        tv_telephone.setText(user.getPhone());
        et_location.setText(user.getAddress());
        if (is_me) {
            edit.setVisibility(View.VISIBLE);
            tv_add_to_favourite.setVisibility(View.INVISIBLE);
            tv_contact.setVisibility(View.INVISIBLE);
        }
        setAdapters();
        tv_rating.setText( "Rating  " + String.valueOf(user.getRate_count()) + " (" + String.valueOf(user.getRate_avr()) + " Reviews)");
        tv_languages.setText("");
        for (ChooseModel lang : user.getSupported_lang()
                ) {
            tv_languages.append(lang.getLang_title() + " ");
        }
        tv_specilities.setText("");
        for (ChooseModel speciality : user.getSpecialities()
                ) {
            tv_specilities.append(speciality.getSpeciality_title() + " ");
        }
        getTimaTableData(user.getOpen_time());
        if(user.isClinic==1)
            linear_practice_profile.setVisibility(View.VISIBLE);
        else
            linear_practice_profile.setVisibility(View.GONE);

        // edit ahmed 12 - 6 - 2017
        ed_location.setText(user.getAddress());
        ed_street_name.setText(user.getInfo().getStreetname());
        ed_house_number.setText(user.getInfo().getHouseNumber());
        ed_zip_code.setText(user.getInfo().getZipCode());
        ed_province.setText(user.getInfo().getProvinz());
        ed_country.setText(user.getInfo().getCountry());

    }

    public List<User> MockDataforChosen() {
        String json = " [{\"id\":4,\"email\":null,\"password\":\"78052a040e70547a0df844744bcfa1ee\",\"last_login\":\"2017-05-14 10:46:56\",\"name\":\"Amr \",\"first_name\":\"Amr\",\"last_name\":\"\",\"avatar\":\"\",\"subtitle\":\" \",\"phone\":\"4668426666655\",\"country_code\":\"+49\",\"active\":null,\"is_doc\":1,\"platform\":3,\"token\":\"fESlX7VwypA:APA91bG78yfSbjyayM8ObfRf88UUPUzPoG2ua0nOpTAQjlyK8CRpSoHh_Wqtpr13YE-z3XYEhr8c1sd4IhvlV9wSUvhM-zPW9XsqXnp_jk_OeBT2LB_ggKAYJM2GTzjiwoobz9_Ihq0w\",\"info\":{\"Country\":\"\",\"Provinz\":\"\",\"Birthday\":\"\",\"Zip Code\":\"\",\"Streetname\":\"\",\"House number\":\"\"},\"location_lat\":null,\"location_long\":null,\"payment\":null,\"settings\":null,\"questions\":{},\"address\":null,\"birth_date\":\"2017-06-08\",\"speciality\":null,\"about\":null,\"last_online\":null,\"unreaded_count\":null,\"passcode\":null,\"code\":\"616401\",\"request_type\":null,\"is_open\":null,\"is_clinic\":null,\"parent_id\":null,\"open_time\":null,\"is_available\":null,\"supported_lang\":null,\"rate\":0,\"douments\":null,\"specialities\":null,\"speciality_id\":null,\"speciality_title\":null,\"speciality_icon\":null},{\"id\":6,\"email\":null,\"password\":\"3b018c5427950c2bb33261011cc8f69b\",\"last_login\":\"2017-05-14 11:38:44\",\"name\":\"Andrew Dr\",\"first_name\":\"Dr\",\"last_name\":\"Andrew\",\"avatar\":\"\",\"subtitle\":\" \",\"phone\":\"123456789\",\"country_code\":\"+49\",\"active\":null,\"is_doc\":1,\"platform\":3,\"token\":\"f2MKK3XDa5E:APA91bF_ddycThjvn_ZEhXIoC9236I_L8Yf6Mjhq6jkGjVXbsEMWyaB8tKiMVnuMsPubbiiBKbK7hB3h1YzstjUEra--37u3r0a5Ts19l5nwhP4xWpvfIQ5lu6jrTz8v0X8U2joYLaXA\",\"info\":{\"Country\":\"\",\"Provinz\":\"\",\"Zip Code\":\"\",\"Streetname\":\"\",\"House number\":\"\"},\"location_lat\":\"0\",\"location_long\":\"0\",\"payment\":null,\"settings\":null,\"questions\":{},\"address\":null,\"birth_date\":\"2017-05-15\",\"speciality\":null,\"about\":null,\"last_online\":null,\"unreaded_count\":null,\"passcode\":null,\"code\":\"724299\",\"request_type\":null,\"is_open\":null,\"is_clinic\":null,\"parent_id\":null,\"open_time\":null,\"is_available\":null,\"supported_lang\":null,\"rate\":0,\"douments\":null,\"specialities\":null,\"speciality_id\":null,\"speciality_title\":null,\"speciality_icon\":null}]";
        Gson gson = new Gson();
        List<User> chosen = new ArrayList<>();
        chosen = gson.fromJson(json, new TypeToken<List<User>>() {
        }.getType());
        return chosen;
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
    public void edit_member_list(){
        Bundle bundle = new Bundle();
        bundle.putInt("Constants",Constants.MEMBERAT);
        bundle.putSerializable(Constants.CHOSED_LIST, (Serializable) user.getMembers_at());
        showDialogFragment(bundle);
    }
    private void getTimaTableData(List<Table> list) {
        if(user.getOpen_Type() == 1)
            always_open.setVisibility(View.VISIBLE);
        else if(user.getOpen_Type() == 2)
            ll_no.setVisibility(View.VISIBLE);
        else if(user.getOpen_Type() == 3)
            permenant_closed.setVisibility(View.VISIBLE);

        if (list.size() > 0)
            ll_no.setVisibility(View.GONE);


        passData(list);
    }

    private void passData(List<Table> list) {
        clearTexts();
        int size = 0 ;
        for (Table table : list) {
            if(size >= list.size())
                return;
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
                size ++ ;
            }
        }
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
        textView.append(textView.getText() + table.getFrom() + " - " + table.getTo());
        textView.append(" \n " + System.getProperty("line.separator"));
    }

    @OnClick(R.id.image_star)
    public void image_star() {
        Intent intent = new Intent(this, RateActivity.class);
        intent.putExtra("doc_id", String.valueOf(user.get_Id()));
        startActivity(intent);
    }

    @OnClick(R.id.tv_add_to_favourite)
    public void addToMyDoctor() {
        if (user.getIs_my_doctor() == null) {
            new HttpCall(this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    Log.i("Answers ", response.toString());
                    user.setIs_my_doctor("1");
                    checkDoctor();
                }

                @Override
                public void onFailed(String error) {
                    Log.i("Error ", " " + error);
                }
            }).addToMyDoctor(user.get_Id() + "");
        } else {
            new HttpCall(this, new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    user.setIs_my_doctor(null);
                    checkDoctor();
                }

                @Override
                public void onFailed(String error) {
                    Log.i("Error ", " " + error);
                }
            }).removeFromMyDoctor(user.get_Id() + "");
        }
    }

    private void checkDoctor() {
        try {
            if (user.getIs_my_doctor() == null)
                tv_add_to_favourite.setText(getString(R.string.add_to));
            else
                tv_add_to_favourite.setText(getString(R.string.remove_from));
        } catch (Exception e) {
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
                    Helper.setImage(DoctorProfileActivity.this, Constants.CHAT_SERVER_URL + "/" + user.getQr_url(), imageView, R.drawable.qr);
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


    @OnClick(R.id.img_edit_avatar)
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
    public void Response(ArrayList<ChooseModel> specialitiesArrayList,int type) {
        ArrayList<ChooseModel> templist=new ArrayList<>();
        switch (type){
            case Constants.SPECIALITIES:
                user.getSpecialities().clear();
                for(ChooseModel item:specialitiesArrayList) {
                    if (item.getIsMyChoise())
                        templist.add(item);
                }
                user.setSpecialities(templist);
                break;
            case Constants.LANGUAUGE:
                user.getSupported_lang().clear();
                for(ChooseModel item:specialitiesArrayList) {
                    if (item.getIsMyChoise())
                        templist.add(item);
                }
                user.setSupported_lang(templist);
                break;
            case Constants.MEMBERAT:
                user.getMembers_at().clear();
                for(ChooseModel item:specialitiesArrayList) {
                    if (item.getIsMyChoise())
                        templist.add(item);
                }
                user.setMembers_at(templist);
                break;
        }
    }

    @Override
    public void onSuccess(Object response) {
        Toast.makeText(this, "Data saved Successfully", Toast.LENGTH_SHORT).show();
        util.dismissProgressDialog();
        setVisiblitiy(View.VISIBLE);
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setUser(user);
        prefManager.put(PrefManager.USER_KEY , new Gson().toJson(userInfoResponse));


    }

    @Override
    public void onFailed(String error) {
        Toast.makeText(this, "Error in saving data", Toast.LENGTH_SHORT).show();
        util.dismissProgressDialog();

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

    public void showDialogFragment(Bundle bundle) {
        MultiChoiseListFragment dialogFragment = new MultiChoiseListFragment();
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "list");
    }
}
