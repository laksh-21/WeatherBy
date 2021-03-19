package com.example.weatherby;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.weatherby.Utilities.NetworkUtils;
import com.example.weatherby.Utilities.WeatherJSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherTextView = (TextView)findViewById(R.id.tv_weather_data);

        new WeatherAsyncTask().execute("Kolkata");
    }

    public class WeatherAsyncTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected String[] doInBackground(String... locations) {
            String location = locations[0];
            URL url = NetworkUtils.buildWeatherUrl(location);
            try {
                String response = NetworkUtils.getHttpResponse(url);
                String[] parsedResponse = WeatherJSONUtils.extractJSONData(MainActivity.this, response);
                return parsedResponse;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] s) {
            if(s != null && s.length != 0){
                for(String forecast: s){
                    mWeatherTextView.append("\n\n\n" + forecast);
                }
            }
        }
    }
}