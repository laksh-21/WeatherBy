package com.example.weatherby.Utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.weatherby.R;

public class NotificationsUtils {
    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel_id";
    private static final int NOTIFICATION_ID = 6199;

    public static void showNotification(Context context){
        createChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context,
                NOTIFICATION_CHANNEL_ID)
                .setContentTitle("This is the title")
                .setContentText("Here is the text that is in the descripton")
                // small icon is absolutely necessary
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
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
}
