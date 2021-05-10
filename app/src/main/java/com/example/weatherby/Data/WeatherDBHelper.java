package com.example.weatherby.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.weatherby.Data.WeatherContract.WeatherEntry;

public class WeatherDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    public WeatherDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_WEATHER_TABLE_CREATE =
                "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
                WeatherEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WeatherEntry.COLUMN_WEATHER_ID  + " INTEGER, "  +
                WeatherEntry.COLUMN_DATE        + " INTEGER, "  +
                WeatherEntry.COLUMN_MAX_TEMP    + " REAL, "     +
                WeatherEntry.COLUMN_MIN_TEMP    + " REAL, "     +
                WeatherEntry.COLUMN_HUMIDITY    + " REAL, "     +
                WeatherEntry.COLUMN_PRESSURE    + " REAL, "     +
                WeatherEntry.COLUMN_WIND_SPEED  + " REAL, "     +
                WeatherEntry.COLUMN_DEGREES     + " REAL"       + " );";

        db.execSQL(SQL_WEATHER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
