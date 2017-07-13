package com.germanitlab.kanonhealth.httpchat;

import android.content.Context;
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

    public void showNotification(Context context,String title,String message){
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
    }
}
