package com.example.weatherby.Data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.weatherby.R;

public class WeatherPreferecnes {
    public static boolean prefersMetric(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForUnitsPref = context.getString(R.string.metrics_pref_key);
        String defaultForUnitsPref = context.getString(R.string.metrics_pref_default);

        String metric = context.getString(R.string.metric);

        String unitPreference = sharedPreferences.getString(keyForUnitsPref, defaultForUnitsPref);

        return metric.equals(unitPreference);
    }

    public static void saveLastNotificationTime(Context context, long timeOfNotification) {
        SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        editor.putLong(lastNotificationKey, timeOfNotification);
        editor.apply();
    }

    public static long getEllapsedTimeSinceLastNotification(Context context) {
        long lastNotificationTimeMillis =
                WeatherPreferecnes.getLastNotificationTimeInMillis(context);
        long timeSinceLastNotification = System.currentTimeMillis() - lastNotificationTimeMillis;
        return timeSinceLastNotification;
    }

    public static long getLastNotificationTimeInMillis(Context context) {
        String lastNotificationKey = context.getString(R.string.pref_last_notification);

        SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context);

        long lastNotificationTime = sp.getLong(lastNotificationKey, 0);

        return lastNotificationTime;
    }
}
