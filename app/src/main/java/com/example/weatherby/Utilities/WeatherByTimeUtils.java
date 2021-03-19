package com.example.weatherby.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherByTimeUtils {
    public static String convertEpochToDate(long epochTime){
        Date date = new Date(epochTime);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
