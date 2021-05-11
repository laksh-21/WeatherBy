package com.example.weatherby.Utilities;

import android.content.Context;

import com.example.weatherby.Data.WeatherPreferecnes;
import com.example.weatherby.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WeatherUnitUtils {

//    Milliseconds in a day
    public static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static String convertEpochToDate(long epochTime){
        Date date = new Date(epochTime);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static boolean isDateNormalized(long epochDate){
        if(epochDate % DAY_IN_MILLIS == 0){
            return true;
        }
        return false;
    }

    public static long normalizeDate(long date) {
        long daysSinceEpoch = elapsedDaysSinceEpoch(date);
        long millisFromEpochToTodayAtMidnightUtc = daysSinceEpoch * DAY_IN_MILLIS;
        return millisFromEpochToTodayAtMidnightUtc;
    }

    private static long elapsedDaysSinceEpoch(long utcDate) {
        return TimeUnit.MILLISECONDS.toDays(utcDate);
    }

    public static String factorMinMaxTemp(Context context, Double minT, Double maxT){
        boolean preferesMetric = WeatherPreferecnes.prefersMetric(context);

        int formatId = R.string.format_temperature_celsius;

        if(!preferesMetric){
            minT = convertToFahrenheit(minT);
            maxT = convertToFahrenheit(maxT);
            formatId = R.string.format_temperature_fahrenheit;
        }

        String minTemp = String.format(context.getString(formatId), minT);
        String maxTemp = String.format(context.getString(formatId), maxT);

        return "Min: " + minTemp + ", Max: " + maxTemp;
    }

    public static double convertToFahrenheit(double temp){
        return (temp * 1.8) + 32;
    }
}
