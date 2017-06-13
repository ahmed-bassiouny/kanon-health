package com.germanitlab.kanonhealth;

import android.*;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.germanitlab.kanonhealth.adapters.SpecilaitiesAdapter;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.callback.Message;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.ChooseModel;
import com.germanitlab.kanonhealth.models.user.Info;
import com.germanitlab.kanonhealth.models.user.UploadImageResponse;
import com.germanitlab.kanonhealth.models.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddPractics extends AppCompatActivity implements Message<ChooseModel> {

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
    // additional data
    User user;
    Info info;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_practics);
        ButterKnife.bind(this);
        user = new User();
        info=new Info();
    }

    @OnClick(R.id.save)
    public void save(View view) {
        user.setAddress(ed_location.getText().toString());
        user.setName(ed_name.getText().toString());
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
                Toast.makeText(AddPractics.this, response.toString(), Toast.LENGTH_LONG).show();
                Log.i("onSuccess onSuccess", response.toString());
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(AddPractics.this, error, Toast.LENGTH_LONG).show();
                Log.i("oneRROR oneRROR", error);
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

    @OnClick(R.id.edit_member_list)
    public void edit_member_list() {
        Bundle bundle = new Bundle();
        bundle.putInt("Constants", Constants.DoctorAll);
        bundle.putSerializable(Constants.CHOSED_LIST, (Serializable)user.getMembers_at());
        showDialogFragment(bundle);
    }
    @OnClick(R.id.edit_image)
    public void edit_image(){
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


}
