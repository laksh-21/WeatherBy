package com.example.weatherby.Sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.weatherby.Data.WeatherContract;
import com.example.weatherby.Data.WeatherPreferecnes;
import com.example.weatherby.MainActivity;
import com.example.weatherby.Utilities.NetworkUtils;
import com.example.weatherby.Utilities.NotificationsUtils;
import com.example.weatherby.Utilities.WeatherJSONUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class WeatherSyncUtils {
    public final static String NOTIFICATION_CANCEL_ACTION = "notification-cancel-action";
    public final static String SYNC_WEATHER_ACTION = "weather-sync-action";

    public static boolean firstOpen = true;


    public static void executeTask(Context context, String action){
        if(action.equals(NOTIFICATION_CANCEL_ACTION)){
            NotificationsUtils.cancelNotifications(context);
        } else if(action.equals(SYNC_WEATHER_ACTION)){
            syncWeather(context);
        }
    }

    synchronized public static void syncWeather(Context context){
        if(!firstOpen) return;

        startSyncingWork(context);
        Log.d("SyncUtils", "I'm here");

        try {
            URL weatherURL = NetworkUtils.buildWeatherUrl("Kolkata");

            String jsonResponse = NetworkUtils.getHttpResponse(weatherURL);

            ContentValues[] contentValues = WeatherJSONUtils.extractJSONData(context, jsonResponse);

            if(contentValues != null && contentValues.length > 0){
                ContentResolver resolver = context.getContentResolver();

                resolver.delete(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        null,
                        null
                );

                resolver.bulkInsert(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        contentValues
                );

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        firstOpen = false;

        long timeSinceLastNotification = WeatherPreferecnes
                .getEllapsedTimeSinceLastNotification(context);

        boolean oneDayPassedSinceLastNotification = false;

//              COMPLETED (14) Check if a day has passed since the last notification
        if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
            oneDayPassedSinceLastNotification = true;
        }

        /*
         * We only want to show the notification if the user wants them shown and we
         * haven't shown a notification in the past day.
         */
//              COMPLETED (15) If more than a day have passed and notifications are enabled, notify the user
        if (oneDayPassedSinceLastNotification) {
            NotificationsUtils.showNotification(context);
        }
    }

    public static void syncWeatherNow(Context context){
        Intent intent = new Intent(context, SyncJobIntentService.class);
        intent.setAction(SYNC_WEATHER_ACTION);
        SyncJobIntentService.enqueueWork(context, intent);
    }

    static void startSyncingWork(Context context){
//        Constraints constraints = new Constraints.Builder()
//                .build();
//        final PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(WeatherSyncWorker.class,
//                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS,
//                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS)
//                .setConstraints(constraints)
//                .addTag("Syncing-work")
//                .build();
////        WorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(WeatherSyncWorker.class).build();
//        WorkManager.getInstance(context).enqueueUniquePeriodicWork("Syncing-work",
//                ExistingPeriodicWorkPolicy.KEEP, myWorkRequest);
        Constraints constraints = new Constraints.Builder()
                .build();


        final PeriodicWorkRequest periodicWorkRequest
                = new PeriodicWorkRequest.Builder(WeatherSyncWorker.class, 2, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("Sync-task",
                ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
    }


}
