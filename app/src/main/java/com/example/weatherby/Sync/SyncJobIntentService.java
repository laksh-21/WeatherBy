package com.example.weatherby.Sync;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class SyncJobIntentService extends JobIntentService {

    private static final int JOB_ID = 22;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }

    public static void enqueueWork(Context context, Intent intent){
        enqueueWork(context, SyncJobIntentService.class, JOB_ID, intent);
    }
}
