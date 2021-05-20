package com.example.weatherby.Sync;

import android.content.Context;

import com.example.weatherby.Utilities.NetworkUtils;
import com.example.weatherby.Utilities.NotificationsUtils;

import java.io.IOException;
import java.net.URL;

public class WeatherSyncUtils {
    public final static String NOTIFICATION_CANCEL_ACTION = "notification-cancel-action";

    public static void executeTask(Context context, String action){
        if(action.equals(NOTIFICATION_CANCEL_ACTION)){
            NotificationsUtils.cancelNotifications(context);
        }
    }

    synchronized public static void syncWeather(Context context){
        try {
            URL weatherURL = NetworkUtils.buildWeatherUrl("Kolkata");

            String jsonResponse = NetworkUtils.getHttpResponse(weatherURL);

            // reformat the parse JSON thingy to send out an array of ContentValues
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
