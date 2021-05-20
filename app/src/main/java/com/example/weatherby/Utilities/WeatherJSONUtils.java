package com.example.weatherby.Utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.weatherby.Data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class WeatherJSONUtils {

     final static String RESPONSE_CODE = "cod";
     final static String FORECAST_LIST_CODE = "list";
     final static String COUNT_CODE = "cnt";
     final static String EPOCH_CODE = "dt";

     final static String TEMP_DESC_CODE = "temp";
     final static String TEMP_DAY_CODE = "day";
     final static String TEMP_MIN_CODE = "min";
     final static String TEMP_MAX_CODE = "max";
     final static String TEMP_NIGHT_CODE = "night";
     final static String TEMP_MORNING_CODE = "morn";
    private static final String WIND_SPEED_CODE = "speed";
    private static final String WIND_DIRECTION_CODE = "deg";

     final static String WEATHER_DESC_CODE = "weather";
    private static final String WEATHER_ID_CODE = "id";
     final static String WEATHER_MAIN_CODE = "main";
     final static String WEATHER_SUM_CODE = "description";

     final static String PRESSURE_CODE = "pressure";
     final static String HUMIDITY_CODE = "humidity";

    public static ContentValues[] extractJSONData(Context context, String jsonString) throws JSONException {
        JSONObject weatherData = new JSONObject(jsonString);

        if(weatherData.has(RESPONSE_CODE)){
            int responseCode = weatherData.getInt(RESPONSE_CODE);
            switch(responseCode){
                case HttpURLConnection.HTTP_OK:
                    break;
                default:
                    return null;
            }
        }

        JSONArray forecastList = weatherData.getJSONArray(FORECAST_LIST_CODE);
        int count = forecastList.length();

        ContentValues[] contentValues = new ContentValues[count];

        long normalizedUtcStartDay = WeatherUnitUtils.getNormalizedUtcDateForToday();

        for(int pos = 0; pos<count; pos++){
            long dateTimeInMillis;
            double pressure;
            int humidity;
            double windSpeed;
            double windDirection;
            int weatherId;

            JSONObject posForecast = forecastList.getJSONObject(pos);

//            dateTimeInMillis = posForecast.getLong(EPOCH_CODE) * 1000L;
            dateTimeInMillis = normalizedUtcStartDay + WeatherUnitUtils.DAY_IN_MILLIS * pos;
            pressure = posForecast.getDouble(PRESSURE_CODE);
            humidity = posForecast.getInt(HUMIDITY_CODE);
            windSpeed = posForecast.getDouble(WIND_SPEED_CODE);
            windDirection = posForecast.getDouble(WIND_DIRECTION_CODE);

            JSONObject tempObj = posForecast.getJSONObject(TEMP_DESC_CODE);
            double minTemp = tempObj.getDouble(TEMP_MIN_CODE);
            double maxTemp = tempObj.getDouble(TEMP_MAX_CODE);

            JSONObject weatherObj = posForecast.getJSONArray(WEATHER_DESC_CODE).getJSONObject(0);

            weatherId = weatherObj.getInt(WEATHER_ID_CODE);

            ContentValues contentValue = new ContentValues();

            contentValue.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTimeInMillis);
            contentValue.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, humidity);
            contentValue.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, pressure);
            contentValue.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
            contentValue.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
            contentValue.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, maxTemp);
            contentValue.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, minTemp);
            contentValue.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherId);

            contentValues[pos] = contentValue;
        }
        return contentValues;
    }
}
