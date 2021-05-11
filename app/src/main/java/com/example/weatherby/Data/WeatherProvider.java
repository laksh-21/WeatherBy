package com.example.weatherby.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weatherby.Utilities.WeatherUnitUtils;

public class WeatherProvider extends ContentProvider {
    private WeatherDBHelper mOpenHelper;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int CODE_WEATHER = 200;
    private static final int CODE_WEATHER_DATE = 201;

    public static UriMatcher buildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER, CODE_WEATHER);
        matcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER + "/#", CODE_WEATHER_DATE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new WeatherDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case CODE_WEATHER:
                cursor = mOpenHelper.getReadableDatabase().query(
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_WEATHER_DATE:
                String epochDateString = uri.getLastPathSegment();

                String newSelection = WeatherContract.WeatherEntry.COLUMN_DATE + "= ?";
                String[] newSelectionArgs = new String[]{epochDateString};

                cursor = mOpenHelper.getReadableDatabase().query(
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        projection,
                        newSelection,
                        newSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("unknown URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if(match == CODE_WEATHER){
            db.beginTransaction();
            int rowsInserted = 0;

            try{
                for(ContentValues value: values){
                    long weatherDate = value.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);

                    if(!WeatherUnitUtils.isDateNormalized(weatherDate)){
                        throw new IllegalArgumentException("Invalid date: " + weatherDate + ". Must normalise date");
                    }

                    long _id = db.insert(
                            WeatherContract.WeatherEntry.TABLE_NAME,
                            null,
                            value);

                    if(_id != -1){
                        rowsInserted++;
                    }
                }
            } finally {
                db.endTransaction();
            }

            if(rowsInserted > 0){
                getContext().getContentResolver().notifyChange(uri, null);
            }

            return rowsInserted;

        } else{
            return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        if (selection == null){
            selection = "1";
        }

        int match = sUriMatcher.match(uri);

        if(match == CODE_WEATHER) {
            numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                    WeatherContract.WeatherEntry.TABLE_NAME,
                    selection,
                    selectionArgs);
        }
        else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
