package com.example.dima.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WiFi_Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, WiFi_Service.class);
        context.startService(intent1);
    }
}
