package com.germanitlab.kanonhealth.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.Specilaities;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.main.MainActivity;
import com.germanitlab.kanonhealth.models.user.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double lat;
    Double longi;
    Intent intent;
    ArrayList<User> list;
    LinearLayout layout;
    String jsonString;
    ProgressDialog progressDialog;
    int create;
    PrefManager prefManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final ImageView back, filter;
        create = 2;
        try {
            prefManager = new PrefManager(this);
            back = (ImageView) this.findViewById(R.id.img_btn_back);
            layout = (LinearLayout) this.findViewById(R.id.toolbar);
            filter = (ImageView) this.findViewById(R.id.filter);
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Specilaities.class);
                    intent.putExtra("hide", true);
                    intent.putExtra("from", true);
                    startActivity(intent);
                }
            });
            intent = getIntent();
            if (intent.getBooleanExtra("from_map", false)) {
                getBySpeciality(intent.getIntExtra("speciality_id", 0), intent.getIntExtra("type", 0));
            } else {
                if (intent.getBooleanExtra("from", false)) {
                    layout.setVisibility(View.VISIBLE);
                    jsonString = intent.getStringExtra("Location");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<User>>() {
                    }.getType();
                    list = gson.fromJson(jsonString, listType);
                } else {
                    lat = Double.parseDouble(intent.getStringExtra("lat"));
                    longi = Double.parseDouble(intent.getStringExtra("long"));
                }
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("from", 2);
                        startActivity(intent);
                    }
                });
                if (create == 1)
                    draw(intent);
                create = 0;
            }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            Intent intent = getIntent();
            if (create == 0) {
                draw(intent);

            }
            create = 1;
        }catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    public void draw(Intent intent) {
        try {
            if (intent.getBooleanExtra("from", false) || intent.getBooleanExtra("from_map", false)) {
                for (User doctor : list
                        ) {

                    LatLng sydney = new LatLng(doctor.getLocation_lat(), doctor.getLocation_long());
                    mMap.addMarker(new MarkerOptions().position(sydney).title(doctor.getName()));
                }
            } else {
                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(lat, longi);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void getBySpeciality(final int id, int type) {
        showProgressDialog();
        new HttpCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                try {
                    Log.e("Update user response :", "no response found");
                    Gson gson = new Gson();
                    jsonString = gson.toJson(response);
                    dismissProgressDialog();
                    layout.setVisibility(View.VISIBLE);
                    jsonString = intent.getStringExtra("Location");
                    Type listType = new TypeToken<List<User>>() {
                    }.getType();
                    list = gson.fromJson(jsonString, listType);
                    if (create == 1)
                        draw(intent);
                    create = 0;
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailed(String error) {
                dismissProgressDialog();
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                Log.e("Error", error);
            }
        }).getlocations(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), id, type);
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.waiting_text), true);
    }
}
