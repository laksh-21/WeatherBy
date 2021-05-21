package com.example.weatherby.Sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WeatherSyncWorker extends Worker {
    public WeatherSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        WeatherSyncUtils.syncWeather(getApplicationContext());
        return Result.retry();
    }
}
