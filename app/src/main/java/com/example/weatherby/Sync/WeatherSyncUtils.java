package com.example.weatherby.Sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.example.weatherby.Data.WeatherContract;
import com.example.weatherby.Utilities.NetworkUtils;
import com.example.weatherby.Utilities.NotificationsUtils;
import com.example.weatherby.Utilities.WeatherJSONUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class WeatherSyncUtils {
    public final static String NOTIFICATION_CANCEL_ACTION = "notification-cancel-action";
    public final static String SYNC_WEATHER_ACTION = "weather-sync-action";

    public static void executeTask(Context context, String action){
        if(action.equals(NOTIFICATION_CANCEL_ACTION)){
            NotificationsUtils.cancelNotifications(context);
        } else if(action.equals(SYNC_WEATHER_ACTION)){
            syncWeather(context);
        }
    }

    synchronized public static void syncWeather(Context context){
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
    }

    public static void syncWeatherNow(Context context){
        Intent intent = new Intent(context, SyncJobIntentService.class);
        intent.setAction(SYNC_WEATHER_ACTION);
        SyncJobIntentService.enqueueWork(context, intent);
    }
}
