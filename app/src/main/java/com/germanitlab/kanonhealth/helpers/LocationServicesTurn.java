package com.germanitlab.kanonhealth.helpers;

import android.app.Activity;
import android.content.IntentSender;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by fume_010 on 24/08/16.
 */
public class LocationServicesTurn {

    public static void turnGPSOn(GoogleApiClient mGoogleApiClient, LocationRequest mLocationRequest, final Activity requestActivity) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //if status of location services is disable
                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        status.startResolutionForResult(
                                requestActivity, 1000);
                    } catch (Exception e){
                        Crashlytics.logException(e);
                        Toast.makeText(requestActivity, requestActivity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

}
