package com.example.weatherby.Sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotifBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent.setClass(context, SyncJobIntentService.class);
        SyncJobIntentService.enqueueWork(context, intent);
    }
}
