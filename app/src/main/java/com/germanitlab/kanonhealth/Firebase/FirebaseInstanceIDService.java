package com.germanitlab.kanonhealth.Firebase;

import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
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
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void sendRegistrationToServer(String token) {
        RequsetToken requsetToken = new RequsetToken(String.valueOf(AppController.getInstance().getClientInfo().getUser_id()),
                AppController.getInstance().getClientInfo().getPassword(), 1, token);
        new HttpCall(new ApiResponse() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onFailed(String error) {

            }
        }).updateToken(requsetToken);

    }
}
