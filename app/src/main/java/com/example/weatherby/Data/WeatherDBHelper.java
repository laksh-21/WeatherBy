package com.example.weatherby.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.weatherby.Data.WeatherContract.WeatherEntry;

public class WeatherDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";

//  !!!INCREMENT THIS EACH TIME YOU UPDATE THE SCHEMA OR OnUpgrade WILL NOT BE CALLED!!!
    private static final int DATABASE_VERSION = 3;

    public WeatherDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_WEATHER_TABLE_CREATE =
                "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
                WeatherEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WeatherEntry.COLUMN_WEATHER_ID  + " INTEGER NOT NULL, "  +
                WeatherEntry.COLUMN_DATE        + " INTEGER NOT NULL, "  +
                WeatherEntry.COLUMN_MAX_TEMP    + " REAL NOT NULL, "     +
                WeatherEntry.COLUMN_MIN_TEMP    + " REAL NOT NULL, "     +
                WeatherEntry.COLUMN_HUMIDITY    + " REAL NOT NULL, "     +
                WeatherEntry.COLUMN_PRESSURE    + " REAL NOT NULL, "     +
                WeatherEntry.COLUMN_WIND_SPEED  + " REAL NOT NULL, "     +
                WeatherEntry.COLUMN_DEGREES     + " REAL NOT NULL, "     +
                "UNIQUE (" + WeatherEntry.COLUMN_DATE + ") ON CONFLICT REPLACE" + ");";

        db.execSQL(SQL_WEATHER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(db);
    }
}
