package com.germanitlab.kanonhealth.httpchat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.germanitlab.kanonhealth.R;

/**
 * Created by bassiouny on 13/07/17.
 */

public class Notification {

    public static void showNotification(Context context,String title,String message,String from_id,boolean hasAction){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(title)
                        .setSound(uri)
                        .setAutoCancel(true)
                        .setContentText(message);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.notification_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo40));
        } else {
            builder.setSmallIcon(R.drawable.stethoscope)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo64));
        }
        if(hasAction) {
            Intent notificationIntent = new Intent(context, HttpChatActivity.class);
            notificationIntent.putExtra("doctorID", from_id);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 2, notificationIntent,
                    PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(contentIntent);
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());
    }
}
