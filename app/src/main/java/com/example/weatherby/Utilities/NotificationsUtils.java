package com.example.weatherby.Utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.weatherby.Data.WeatherContract;
import com.example.weatherby.MainActivity;
import com.example.weatherby.R;
import com.example.weatherby.Sync.NotifBroadcastReciever;
import com.example.weatherby.Sync.WeatherSyncUtils;

public class NotificationsUtils {
    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel_id";
    private static final int NOTIFICATION_ID = 6199;
    private static final int INTENT_CODE = 9411;

    public static final String[] WEATHER_NOTIFICATION_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
    };

    public static final int INDEX_WEATHER_ID = 0;
    public static final int INDEX_MAX_TEMP = 1;
    public static final int INDEX_MIN_TEMP = 2;

    public static void showNotification(Context context){
        Log.d("Notification", "Called");
        createChannel(context);


        Uri todaysWeatherUri = WeatherContract.WeatherEntry
                .buildWeatherUriWithDate(WeatherUnitUtils.normalizeDate(System.currentTimeMillis()));

        Cursor todayWeatherCursor = context.getContentResolver().query(
                todaysWeatherUri,
                WEATHER_NOTIFICATION_PROJECTION,
                null,
                null,
                null);

        if(todayWeatherCursor.moveToFirst()){
            int weatherId = todayWeatherCursor.getInt(INDEX_WEATHER_ID);
            double high = todayWeatherCursor.getDouble(INDEX_MAX_TEMP);
            double low = todayWeatherCursor.getDouble(INDEX_MIN_TEMP);
            String notificationTitle = context.getString(R.string.app_name);
            String notificationText = getNotificationText(context, weatherId, high, low);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context,
                    NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    // small icon is absolutely necessary
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    // it sets the tap action
                    .setContentIntent(getTapAction(context))
                    // add button for cancelling the thing
                    .addAction(R.drawable.ic_settings,
                            "Dismiss",
                            getCancelAction(context))
                    // it dismisses the notification if user taps one it
                    .setAutoCancel(true);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            }


            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private static PendingIntent getTapAction(Context context){
        Intent mainActivityIntent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(context,
                INTENT_CODE,
                mainActivityIntent,
                0
                );
    }

    private static void createChannel(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static PendingIntent getCancelAction(Context context){
        Intent cancelIntent = new Intent(context, NotifBroadcastReciever.class);
        cancelIntent.setAction(WeatherSyncUtils.NOTIFICATION_CANCEL_ACTION);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        return pendingIntent;
    }

    public static void cancelNotifications(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private static String getNotificationText(Context context, int weatherId, double high, double low) {
        String shortDescription = WeatherUnitUtils
                .getStringForWeatherCondition(context, weatherId);

        String notificationFormat = context.getString(R.string.format_notification);
        String notificationText = String.format(notificationFormat,
                shortDescription,
                WeatherUnitUtils.formatTemperature(context, high),
                WeatherUnitUtils.formatTemperature(context, low));

        return notificationText;
    }
}
