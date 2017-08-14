package com.germanitlab.kanonhealth.Firebase;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.httpchat.HttpChatFragment;
import com.germanitlab.kanonhealth.httpchat.MessageRequest;
import com.germanitlab.kanonhealth.httpchat.MessageRequestSeen;
import com.germanitlab.kanonhealth.httpchat.Notification;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Geram IT Lab on 20/04/2017.
 */

// Edit by ahmed bassiouny on 24/05/2017

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    int id = 0;
    private LocalBroadcastManager broadcaster;
    //Object obj;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //obj = remoteMessage.getData().get("from_id");
        /*
        *
        * */
        // notificationType ==  1 for new messagee ,2 for login , 3 for deliver message , 4 for close session ,6 for seen message  Andy

        try {
            int notificationType = Integer.parseInt(remoteMessage.getData().get("notificationtype"));
            switch (notificationType) {
                case 1:
                    if (HttpChatFragment.chatRunning)
                        getMessage(remoteMessage, notificationType);
                    else //notify
                        if (remoteMessage.getData().get("msg") != null && remoteMessage.getData().get("from_id") != null && remoteMessage.getData().get("to_id") != null) {
                            Notification.showNotification(this, "title", remoteMessage.getData().get("msg"), remoteMessage.getData().get("from_id"), true);
                            messagesDeliver(remoteMessage.getData().get("from_id"), remoteMessage.getData().get("to_id"));
                        } else if (remoteMessage.getData().get("msg") != null) {
                            messagesDeliver(remoteMessage.getData().get("from_id"), remoteMessage.getData().get("to_id"));
                            Notification.showNotification(this, "title", remoteMessage.getData().get("msg"), "", false);

                        }
                    break;
                case 2:
                    if (remoteMessage.getData().get("body") != null && remoteMessage.getData().get("title") != null)
                        Notification.showNotification(this, remoteMessage.getData().get("body"), remoteMessage.getData().get("title"), "", false);
                    break;
                case 3:
                case 6:
                    if (HttpChatFragment.chatRunning)
                        getMessageDeliveredSeen(remoteMessage, notificationType);
                    break;
                case 4: // for closeing session
                    if (HttpChatFragment.chatRunning)
                        getMessage(remoteMessage, notificationType);

            }
        } catch (Exception e) {
        }

    }

    private void getCloseChat(RemoteMessage remoteMessage) {
        Intent intent = new Intent("MyData");
        intent.putExtra("notificationtype", Integer.parseInt(remoteMessage.getData().get("notificationtype")));
        intent.putExtra("to_id", Integer.parseInt(remoteMessage.getData().get("notificationtype")));
        broadcaster.sendBroadcast(intent);
    }

   /* private void getNewMessage(RemoteMessage remoteMessage) {
        Message message = new Message();
        if (remoteMessage.getData().get("from_id") != null)
            message.setFrom_id(Integer.valueOf(remoteMessage.getData().get("from_id")));
        message.setTo(Integer.valueOf(remoteMessage.getData().get("to_id")));
        message.setMsg(remoteMessage.getData().get("msg"));
        message.setType(remoteMessage.getData().get("type"));
        message.setSent_at(remoteMessage.getData().get("sent_at"));
        Intent intent = new Intent("MyData");
        intent.putExtra("extra", message);
        intent.putExtra("notificationtype", Integer.parseInt(remoteMessage.getData().get("notificationtype")));
        broadcaster.sendBroadcast(intent);
        /*if (obj != null) {
            try {
                if (obj.toString().trim().isEmpty())
                    return;
                JSONObject sendSeen = new JSONObject();
                sendSeen.put("id", id);
                sendSeen.put("is_seen", 1);
            } catch (JSONException e) {
                Log.e("FirebaseMessaging", "onMessageReceived: ", e);
                Crashlytics.logException(e);
            }
            if (remoteMessage.getData().get("from_id") != String.valueOf(ChatActivity.user_id) && ChatActivity.appStatus != true)
                showNotification(remoteMessage.getData().get("msg"),"Neue Nachricht", 1, id, true);

        }
    }*/

    private void getLogin(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"), 0, 0, false);

    }

    private void getMessage(RemoteMessage remoteMessage, int notificationtype) {
        Message message = new Message();
        if (remoteMessage.getData().get("from_id") != null)
            message.setFrom_id(Integer.valueOf(remoteMessage.getData().get("from_id")));
        message.setTo(Integer.valueOf(remoteMessage.getData().get("to_id")));
        message.setMsg(remoteMessage.getData().get("msg"));
        message.setType(remoteMessage.getData().get("type"));
        message.setSent_at(remoteMessage.getData().get("sent_at"));
        Intent intent = new Intent("MyData");
        intent.putExtra("extra", message);
        intent.putExtra("notificationtype", notificationtype);
        broadcaster.sendBroadcast(intent);
    }

    private void getMessageDeliveredSeen(RemoteMessage remoteMessage, int notificationtype) {
        Intent intent = new Intent("MyData");
        intent.putExtra("notificationtype", notificationtype);
        broadcaster.sendBroadcast(intent);
    }

    private void showNotification(String message, String title, int type, int from_id, Boolean hasAction) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(title)
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
        if (hasAction) {

//            Intent notificationIntent = new Intent(this, ChatActivity.class);
//            notificationIntent.putExtra("message", message);
//            notificationIntent.putExtra("type", type);
//            notificationIntent.putExtra("from_id", from_id);
//            notificationIntent.putExtra("from_notification", 1);
//            notificationIntent.putExtra("notification_type", Constants.OPEN_CHAT);
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            PendingIntent contentIntent = PendingIntent.getActivity(this, 2, notificationIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
//            builder.setContentIntent(contentIntent);

            // Add as notification

        }
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());

    }

    private void messagesDeliver(final String userID, final String messageId) {
        // i sent request to make my msg seen
        try {
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    ApiHelper.deliveredMessgae(getApplicationContext(), Integer.parseInt(userID), messageId);
                }
            })).run();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("messagesDeliver", "messagesDeliver: ", e);
        }
    }
}
