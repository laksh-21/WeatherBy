package com.example.weatherby;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.weatherby.Utilities.NetworkUtils;
import com.example.weatherby.Utilities.WeatherJSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]> {

    private RecyclerView mForecastList;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;
    private ForecastAdapter mForecastAdapter;

    private final int ASYNC_LOADER_ID = 22;
    private LoaderManager mLoaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mForecastList = findViewById(R.id.rv_forecast_list);
        mErrorTextView = findViewById(R.id.tv_error_message);
        mProgressBar = findViewById(R.id.pb_network_loading);
        mForecastAdapter = new ForecastAdapter();

        mForecastList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mForecastList.setHasFixedSize(true);
        mForecastList.setAdapter(mForecastAdapter);

        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(ASYNC_LOADER_ID, null, this);
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
                invalidateData();
                mLoaderManager.restartLoader(ASYNC_LOADER_ID, null, this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showWeatherDataView(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);

        mForecastList.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);

        mForecastList.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {

            String[] weatherData = null;

            @Nullable
            @Override
            public String[] loadInBackground() {
                String locationQuery = "Kolkata";

                URL weatherRequestUrl = NetworkUtils.buildWeatherUrl(locationQuery);

                try {
                    String jsonWeatherResponse = NetworkUtils.getHttpResponse(weatherRequestUrl);
                    return WeatherJSONUtils.extractJSONData(MainActivity.this, jsonWeatherResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
                if(weatherData == null) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                } else{
                    deliverResult(weatherData);
                }
            }

            @Override
            public void deliverResult(@Nullable String[] data) {
                weatherData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] s) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if(s != null && s.length != 0){
            showWeatherDataView();
            mForecastAdapter.setWeatherData(s);
        } else{
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }

    private void invalidateData() {
        mForecastAdapter.setWeatherData(null);
    }
}