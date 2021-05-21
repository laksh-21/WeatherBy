package com.example.weatherby;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.weatherby.Data.WeatherContract;
import com.example.weatherby.Sync.WeatherSyncUtils;
import com.example.weatherby.Sync.WeatherSyncWorker;
import com.example.weatherby.Utilities.NotificationsUtils;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView mForecastList;
    private ProgressBar mProgressBar;
    private ForecastAdapter mForecastAdapter;

    private final int ASYNC_LOADER_ID = 22;
    private LoaderManager mLoaderManager;

    private int mPosition = RecyclerView.NO_POSITION;

    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mForecastList = findViewById(R.id.rv_forecast_list);
        mProgressBar = findViewById(R.id.pb_network_loading);
        mForecastAdapter = new ForecastAdapter(this);

        mForecastList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mForecastList.setHasFixedSize(true);
        mForecastList.setAdapter(mForecastAdapter);

        showLoading();
//        showWeatherDataView();
        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(ASYNC_LOADER_ID, null, this);

        WeatherSyncUtils.syncWeatherNow(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId){
            case R.id.action_refresh_btn:
//                invalidateData();
//                mLoaderManager.restartLoader(ASYNC_LOADER_ID, null, this);
                NotificationsUtils.showNotification(MainActivity.this);
                return true;
            case R.id.settings_btn:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showWeatherDataView(){
        mProgressBar.setVisibility(View.INVISIBLE);

        mForecastList.setVisibility(View.VISIBLE);
    }

    private void showLoading(){
        mProgressBar.setVisibility(View.VISIBLE);

        mForecastList.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if(id == ASYNC_LOADER_ID){
            Uri forecastUri = WeatherContract.WeatherEntry.CONTENT_URI;

            String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

            String selection = WeatherContract.WeatherEntry.getSqlSelectByDate();

            return new CursorLoader(this,
                    forecastUri,
                    MAIN_FORECAST_PROJECTION,
                    selection,
                    null,
                    sortOrder);
        } else{
            throw new RuntimeException("Loader not initialised: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mForecastList.smoothScrollToPosition(mPosition);

        if (data.getCount() != 0) showWeatherDataView();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }

    private void invalidateData() {
        mForecastAdapter.swapCursor(null);
    }
}