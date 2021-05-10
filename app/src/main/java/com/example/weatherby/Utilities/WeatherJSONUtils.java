package com.example.weatherby.Utilities;

import android.content.Context;

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

     final static String WEATHER_DESC_CODE = "weather";
     final static String WEATHER_MAIN_CODE = "main";
     final static String WEATHER_SUM_CODE = "description";

     final static String PRESSURE_CODE = "pressure";
     final static String HUMIDITY_CODE = "humidity";

    public static String[] extractJSONData(Context context, String jsonString) throws JSONException {
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

        String[] parsedForecastList = new String[count];

        for(int pos = 0; pos<count; pos++){
            JSONObject posForecast = forecastList.getJSONObject(pos);

            long epochTime = posForecast.getLong(EPOCH_CODE) * 1000L;
            String date = WeatherUnitUtils.convertEpochToDate(epochTime);

            JSONObject tempObj = posForecast.getJSONObject(TEMP_DESC_CODE);
            double minTemp = tempObj.getDouble(TEMP_MIN_CODE);
            double maxTemp = tempObj.getDouble(TEMP_MAX_CODE);

            String minMaxTemp = WeatherUnitUtils.factorMinMaxTemp(context, minTemp, maxTemp);

            parsedForecastList[pos] = date + " " + minMaxTemp;
        }
        return parsedForecastList;
    }
}
