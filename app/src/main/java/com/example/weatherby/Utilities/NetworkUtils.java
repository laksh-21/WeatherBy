package com.example.weatherby.Utilities;

import java.net.URL;

/**
 * This class is used to communicate with the server*/

public final class NetworkUtils {
    private static String LOG_TAG = NetworkUtils.class.getSimpleName();

    final static String QUERY_PARAM = "q";
    final static String LAT_PARAM = "lat";
    final static String LONG_PARAM = "long";
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "metric";

    public static URL buildWeatherUrl(String location){
        return null;
    }

    public static String getHttpResponse(URL weatherUrl){
        return null;
    }

}
