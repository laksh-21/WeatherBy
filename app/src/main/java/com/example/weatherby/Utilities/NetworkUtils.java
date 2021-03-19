package com.example.weatherby.Utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This class is used to communicate with the server*/

public final class NetworkUtils {
    private final static String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String DYNAMIC_WEATHER_URL = "https://andfun-weather.udacity.com/weather";
    private static final String STATIC_WEATHER_URL = "https://andfun-weather.udacity.com/staticweather";
    private static final String FORECAST_BASE_URL = STATIC_WEATHER_URL;

    private static final String format = "json";
    private static final String units = "metric";
    private static final int numDays = 14;

    final static String QUERY_PARAM = "q";
    final static String LAT_PARAM = "lat";
    final static String LONG_PARAM = "long";
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "units";
    final static String DAYS_PARAM = "cnt";

    public static URL buildWeatherUrl(String location){
        Uri uri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, location)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays)).build();
        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.e(LOG_TAG, url.toString());
        return url;
    }

    public static String getHttpResponse(URL weatherUrl) throws IOException{
        HttpURLConnection httpURLConnection = (HttpURLConnection) weatherUrl.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner sc = new Scanner(inputStream);
            sc.useDelimiter("\\A");
            if (sc.hasNext()){
                return sc.next();
            } else{
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

}
