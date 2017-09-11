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
import com.germanitlab.kanonhealth.api.models.Message;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.parameters.MessageOperationParameter;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.httpchat.HttpChatFragment;
import com.germanitlab.kanonhealth.httpchat.MessageRequest;
import com.germanitlab.kanonhealth.httpchat.MessageRequestSeen;
import com.germanitlab.kanonhealth.httpchat.Notification;
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

        // notificationType ==  1 for new messagee , 2 for deliver message , 3 for seen message
            //2 for login , ,, for close session *** i will handle it with karim
        try {
            int notificationType = Integer.parseInt(remoteMessage.getData().get(Constants.notificationType));
            switch (notificationType) {
                case 1:
                    if (HttpChatFragment.chatRunning)
                        getMessage(remoteMessage, notificationType);
                    else //notify
                        if (remoteMessage.getData().get(Message.KEY_FROMID) != null && remoteMessage.getData().get(Message.KEY_TOID) != null) {
                            String msgType=remoteMessage.getData().get(Message.KEY_TYPE);
                            if(msgType.equals(Message.MESSAGE_TYPE_TEXT)){
                                Notification.showNotification(this, "doctor name", remoteMessage.getData().get(Message.KEY_MESSAGE), remoteMessage.getData().get(Message.KEY_FROMID), false);
                            }else{
                                Notification.showNotification(this, "doctor name", msgType, remoteMessage.getData().get(Message.KEY_FROMID), false);
                            }
                            messagesDeliver(Integer.parseInt(remoteMessage.getData().get(Message.KEY_TOID)),remoteMessage.getData().get(Message.KEY_FROMID), remoteMessage.getData().get(Message.KEY_ID));
                        }
//                        } else if (remoteMessage.getData().get("msg") != null) {
//                            messagesDeliver(remoteMessage.getData().get("from_id"), remoteMessage.getData().get("to_id"));
//                            Notification.showNotification(this, "title", remoteMessage.getData().get("msg"), "", false);
//
//                        }
                    break;
                /*case 2:
                    if (remoteMessage.getData().get("body") != null && remoteMessage.getData().get("title") != null)
                        Notification.showNotification(this, remoteMessage.getData().get("body"), remoteMessage.getData().get("title"), "", false);
                    break;*/
                case 2:
                case 3:
                    if (HttpChatFragment.chatRunning)
                        getMessageDeliveredSeen(remoteMessage, notificationType);
                    break;
               /* case 4: // for closeing session
                    if (HttpChatFragment.chatRunning)
                        getMessage(remoteMessage, notificationType);*/

            }
        } catch (Exception e) {
        }

    }

    private void getCloseChat(RemoteMessage remoteMessage) {
        Intent intent = new Intent("MyData");
        intent.putExtra(Constants.notificationType, Integer.parseInt(remoteMessage.getData().get(Constants.notificationType)));
        intent.putExtra("to_id", Integer.parseInt(remoteMessage.getData().get(Constants.notificationType)));
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
        if (remoteMessage.getData().get(Message.KEY_FROMID) != null)
            message.setFromID(Integer.valueOf(remoteMessage.getData().get(Message.KEY_FROMID)));
        message.setToID(Integer.valueOf(remoteMessage.getData().get(Message.KEY_TOID)));
        message.setType(remoteMessage.getData().get(Message.KEY_TYPE));
        message.setStatus(0);
        message.setForward(0);
        message.setMessageID(Integer.valueOf(remoteMessage.getData().get(Message.KEY_ID)));
        if(message.getType().equals(Message.MESSAGE_TYPE_TEXT)||message.getType().equals(Message.MESSAGE_TYPE_LOCATION)) {
            message.setMessage(remoteMessage.getData().get(Message.KEY_MESSAGE));
        }else{
            message.setMedia(remoteMessage.getData().get(Message.KEY_MEDIA));
        }
        message.setDateTime(remoteMessage.getData().get(Message.KEY_CREATED_AT));
        //message.setMessageID(remoteMessage.getData().get(Message.KEY_ID));
        Intent intent = new Intent("MyData");
        intent.putExtra("extra", message);
        intent.putExtra(Constants.notificationType, notificationtype);
        broadcaster.sendBroadcast(intent);
    }

    private void getMessageDeliveredSeen(RemoteMessage remoteMessage, int notificationtype) {
        Intent intent = new Intent("MyData");
        intent.putExtra(Constants.notificationType, notificationtype);
        intent.putExtra("extra",remoteMessage.getData().get(MessageOperationParameter.PARAMATER_TO_ID));
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

    private void messagesDeliver(final int userID,final String doctorID,final String messageId) {
        // i sent request to make my msg seen
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiHelper.deliveredMessgae(FirebaseMessagingService.this, userID, String.valueOf(doctorID), messageId, UserInfo.DOCTOR);
            }
        }).start();
    }
}
