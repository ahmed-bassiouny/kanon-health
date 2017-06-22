package com.germanitlab.kanonhealth.Firebase;

import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.RequsetToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Geram IT Lab on 20/04/2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("taaaaag", "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        try {
            sendRegistrationToServer(refreshedToken);
        } catch (Exception e){
            Crashlytics.logException(e);
        }

    }

    private void sendRegistrationToServer(String token) {
        PrefManager prefManager = new PrefManager(getApplicationContext());
        RequsetToken requsetToken = new RequsetToken(prefManager.getData(PrefManager.USER_ID),prefManager.getData(PrefManager.USER_PASSWORD), 1, token);
        new HttpCall(getApplicationContext(),new ApiResponse() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show();

            }
        }).updateToken(requsetToken);

    }
}
