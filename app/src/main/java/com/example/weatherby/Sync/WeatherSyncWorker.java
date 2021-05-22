package com.example.weatherby.Sync;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.weatherby.Utilities.NotificationsUtils;

public class WeatherSyncWorker extends Worker {
    public WeatherSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("Worker", "Called");
//        NotificationsUtils.showNotification(getApplicationContext());
        WeatherSyncUtils.syncWeather(getApplicationContext());
        return Result.success();
    }
}
