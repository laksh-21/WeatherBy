package com.example.weatherby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
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
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherTextView = findViewById(R.id.tv_weather_data);
        mErrorTextView = findViewById(R.id.tv_error_message);
        mProgressBar = findViewById(R.id.pb_network_loading);

        loadWeatherData("Kolkata");
    }

    private void loadWeatherData(String location){
        mWeatherTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        new WeatherAsyncTask().execute(location);
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
                mWeatherTextView.setText("");
                loadWeatherData("Kolkata");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class WeatherAsyncTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected String[] doInBackground(String... locations) {
            String location = locations[0];
            URL url = NetworkUtils.buildWeatherUrl(location);
            try {
                String response = NetworkUtils.getHttpResponse(url);
                return WeatherJSONUtils.extractJSONData(MainActivity.this, response);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if(s != null && s.length != 0){
                mWeatherTextView.setVisibility(View.VISIBLE);
                for(String forecast: s){
                    mWeatherTextView.append("\n\n\n" + forecast);
                }
            } else{
                mErrorTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}