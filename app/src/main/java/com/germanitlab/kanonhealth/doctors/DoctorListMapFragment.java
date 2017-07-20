package com.germanitlab.kanonhealth.doctors;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.LocationServicesTurn;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.main.MainActivity;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;


public class DoctorListMapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ApiResponse {

    private MainActivity activity;
    MapView mapView;
    ImageButton imgBtnBack;
    GoogleMap map;
    private ImageView myQr;
    private PrefManager mPrefManager;
    private GoogleApiClient mGoogleApiClient;
    private boolean isFirst = true;
    PrefManager prefManager;

    public DoctorListMapFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public DoctorListMapFragment(MainActivity activity) {
        // Required empty public constructor
        this.activity = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getContext());
        buildGoogleApiClient();
        mPrefManager = new PrefManager(getActivity());
        mGoogleApiClient.connect();
        MapsInitializer.initialize(this.getActivity());

        new HttpCall(activity, this).getAllDoctor(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD),getIam());


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_list_map, container, false);

        try {
            intViews(view);
            assignMap(savedInstanceState);
            handleEvent();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void handleEvent() {

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
    }

    private void assignMap(Bundle savedInstanceState) {
        try {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;

                    if (doctorResponseList != null) {

                        for (User doctorResponse : doctorResponseList) {

                            double lat = doctorResponse.getLocation_lat();
                            double lng = doctorResponse.getLocation_long();

                            map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(doctorResponse.getFullName()));
                        }

                    }
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    private void intViews(View view) {
        mapView = (MapView) view.findViewById(R.id.map);
        myQr = (ImageView) view.findViewById(R.id.myQr);
//        Helper.ImportQr(mPrefManager , getActivity() , myQr);
        imgBtnBack = (ImageButton) view.findViewById(R.id.img_btn_back);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(5000); //5 seconds
                mLocationRequest.setFastestInterval(3000); //3 seconds
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    LocationServicesTurn.turnGPSOn(mGoogleApiClient, mLocationRequest, getActivity());
                }
            } else
                askForPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 6);

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    private void askForPermission(String[] permission, Integer requestCode) {

        ActivityCompat.requestPermissions(getActivity(), permission, requestCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (ActivityCompat.checkSelfPermission(getContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == 5) {
                    map.setMyLocationEnabled(true);
                } else if (requestCode == 6) {

                    LocationRequest mLocationRequest = new LocationRequest();
                    mLocationRequest.setInterval(5000); //5 seconds
                    mLocationRequest.setFastestInterval(3000); //3 seconds
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                    final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        LocationServicesTurn.turnGPSOn(mGoogleApiClient, mLocationRequest, getActivity());
                    }
                }

            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (map != null && isFirst) {
                isFirst = false;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(18).build();
                map.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDestroy() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        super.onDestroy();
    }

    List<User> doctorResponseList;

    @Override
    public void onSuccess(Object response) {
        try {
            doctorResponseList = (List<User>) response;
            if (map != null) {

                for (User doctorResponse : doctorResponseList) {

                    double lat = doctorResponse.getLocation_lat();
                    double lng = doctorResponse.getLocation_long();

                    map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(doctorResponse.getFullName()));
                }

            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onFailed(String error) {
        Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        Log.e("error", error + " +++");
    }
    private String getIam(){
        Gson gson = new Gson();
        if(gson.fromJson(prefManager.getData(PrefManager.USER_KEY),UserInfoResponse.class).getUser().getIsDoc()==1)
            return "doc";
        else if(gson.fromJson(prefManager.getData(PrefManager.USER_KEY),UserInfoResponse.class).getUser().getIsClinic()==1)
            return "clinic";
        else
            return "user";
    }
}
