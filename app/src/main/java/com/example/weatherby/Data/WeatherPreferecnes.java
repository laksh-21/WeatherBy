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
}
