package com.germanitlab.kanonhealth.Firebase;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.germanitlab.kanonhealth.R;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.chat.ChatActivity;
import com.germanitlab.kanonhealth.helpers.Constants;

/**
 * Created by Geram IT Lab on 20/04/2017.
 */

// Edit by ahmed bassiouny on 24/05/2017

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    String TAG="*a*a*a*a**a";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("545", "575");
        Object obj = remoteMessage.getData().get("from_id");
        if (obj != null) {
            int id = Integer.valueOf(obj.toString());
            JSONObject sendSeen = new JSONObject();
            try {
                sendSeen.put("id", id);
                sendSeen.put("is_seen", 1);
                AppController.getInstance().getSocket().emit("IsDeliver", sendSeen);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "remoteMessage.getData().get(\"from_id\") = "+remoteMessage.getData().get("from_id"));
            Log.i(TAG, "String.valueOf(ChatActivity.user_id )="+String.valueOf(ChatActivity.user_id ));
            Log.i(TAG, ChatActivity.appStatus+"");
            if (remoteMessage.getData().get("from_id") != String.valueOf(ChatActivity.user_id ) && ChatActivity.appStatus != true)
                showNotification(remoteMessage.getData().get("msg"), 1, id);
/*            if (MainActivity.appStatus != true) {
                obj = remoteMessage.getData().get("msg");
            }*/

        }
    }

    private void showNotification(String message, int type, int from_id) {


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Neue Nachricht")
                        .setSound(uri)
                        .setAutoCancel(true)
                        .setContentText(message);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.notification_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo40));
        } else {
            builder.setSmallIcon(R.drawable.stethoscope)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo64));
        }


        Intent notificationIntent = new Intent(this, ChatActivity.class);
        notificationIntent.putExtra("message", message);
        notificationIntent.putExtra("type", type);
        notificationIntent.putExtra("from_id", from_id);
        notificationIntent.putExtra("from_notification", 1);
        notificationIntent.putExtra("notification_type", Constants.OPEN_CHAT);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 2, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT |PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());

    }
}
