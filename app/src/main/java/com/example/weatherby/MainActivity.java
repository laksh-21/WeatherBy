package com.example.weatherby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
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
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mForecastList;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;
    private ForecastAdapter mForecastAdapter;

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

        loadWeatherData("Kolkata");
    }

    private void loadWeatherData(String location){
        mForecastList.setVisibility(View.INVISIBLE);
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
                loadWeatherData("Kolkata");
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
                showWeatherDataView();
                Log.e("Hello", ""+s.length);
                mForecastAdapter.setWeatherData(s);
            } else{
                showErrorMessage();
            }
        }
    }
}