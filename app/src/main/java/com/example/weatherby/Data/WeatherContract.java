package com.example.weatherby.Data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.weatherby.Utilities.WeatherUnitUtils;


public class WeatherContract {

    public final static String CONTENT_AUTHORITY = "com.example.weatherby";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public final static String PATH_WEATHER = "weather";

    public static final class WeatherEntry implements BaseColumns{
        public static final Uri CONTENT_URI =BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_WIND_SPEED = "wind";
        public static final String COLUMN_DEGREES = "degrees";

        public static String getSqlSelectByDate(){
            long normalizedTodayDate = WeatherUnitUtils.normalizeDate(System.currentTimeMillis());
            return WeatherEntry.COLUMN_DATE + " >= " +normalizedTodayDate;
        }
    }
}
