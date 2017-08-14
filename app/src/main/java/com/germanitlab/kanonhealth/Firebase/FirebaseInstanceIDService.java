package com.germanitlab.kanonhealth.Firebase;

import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
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
            if (refreshedToken != null) {
                sendRegistrationToServer(refreshedToken);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }

    }

    private void sendRegistrationToServer(final String token) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                ApiHelper.addToken(getApplicationContext(), String.valueOf(PrefHelper.get(FirebaseInstanceIDService.this,PrefHelper.KEY_USER_ID,-1)),token);
            }
        })).run();
    }
}
