package com.germanitlab.kanonhealth.doctors;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.germanitlab.kanonhealth.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;


public class DoctorMapActivity extends AppCompatActivity {

    MapView map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_map);

        map = (MapView) findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

            }
        });

    }
}
