package com.example.weatherby.Sync;

import android.content.Context;

import com.example.weatherby.Utilities.NotificationsUtils;

public class WeatherSyncUtils {
    public final static String NOTIFICATION_CANCEL_ACTION = "notification-cancel-action";

    public static void executeTask(Context context, String action){
        if(action.equals(NOTIFICATION_CANCEL_ACTION)){
            NotificationsUtils.cancelNotifications(context);
        }
    }
}
