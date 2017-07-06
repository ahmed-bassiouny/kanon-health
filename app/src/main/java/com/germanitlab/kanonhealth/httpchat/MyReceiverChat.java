package com.germanitlab.kanonhealth.httpchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.germanitlab.kanonhealth.models.messages.Message;

public class MyReceiverChat extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Message message=(Message) intent.getSerializableExtra("extra");
        Log.e("aa*a*a*a*", "onReceive: " );
    }
}
