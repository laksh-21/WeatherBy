package com.example.weatherby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.weatherby.Data.WeatherContract;
import com.example.weatherby.Utilities.WeatherUnitUtils;

public class WeatherDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mHighTemperatureView;
    private TextView mLowTemperatureView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;

    private Uri mUri;

    public static final String[] WEATHER_DETAIL_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_HUMIDITY = 3;
    public static final int INDEX_WEATHER_PRESSURE = 4;
    public static final int INDEX_WEATHER_WIND_SPEED = 5;
    public static final int INDEX_WEATHER_DEGREES = 6;
    public static final int INDEX_WEATHER_CONDITION_ID = 7;

    private final int LOADER_ID = 619;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        Intent creationIntent = getIntent();

        mDateView = findViewById(R.id.tv_date);
        mDescriptionView = findViewById(R.id.tv_weather_description);
        mHighTemperatureView = findViewById(R.id.tv_high_temperature);
        mLowTemperatureView = findViewById(R.id.tv_low_temperature);
        mHumidityView = findViewById(R.id.tv_humidity);
        mWindView = findViewById(R.id.tv_wind);
        mPressureView = findViewById(R.id.tv_pressure);

        mUri = creationIntent.getData();

        if(mUri == null)throw new NullPointerException("Uri for WeatherDetailActivity cannot be null");
    }

    private void shareWeather(){
        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText("Hello")
                .getIntent();
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.detaal_share_btn:
                shareWeather();
                return true;
            case R.id.settings_btn:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == LOADER_ID){
            return new CursorLoader(this,
                    mUri,
                    WEATHER_DETAIL_PROJECTION,
                    null,
                    null,
                    null);
        } else{
             throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean validData = false;
        if (data != null && data.moveToFirst()) {
            validData = true;
        }

        if (!validData) {
            return;
        }

//        Date
        long dateEpcoh = data.getLong(INDEX_WEATHER_DATE);
        String formattedDate = WeatherUnitUtils.convertEpochToDate(dateEpcoh);
        mDateView.setText(formattedDate);

//        Weather Desc
        int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
        String weatherDesc = WeatherUnitUtils.getStringForWeatherCondition(this, weatherId);
        mDescriptionView.setText(weatherDesc);

//        High Temp
        double maxTemp = data.getDouble(INDEX_WEATHER_MAX_TEMP);
        String highTempString = WeatherUnitUtils.formatTemperature(this, maxTemp);
        mHighTemperatureView.setText(highTempString);

//        Low Temp
        double minTemp = data.getDouble(INDEX_WEATHER_MIN_TEMP);
        String lowTempString = WeatherUnitUtils.formatTemperature(this, minTemp);
        mHighTemperatureView.setText(lowTempString);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}