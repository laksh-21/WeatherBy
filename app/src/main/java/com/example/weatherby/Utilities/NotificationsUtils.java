package com.example.weatherby.Utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.weatherby.MainActivity;
import com.example.weatherby.R;

public class NotificationsUtils {
    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel_id";
    private static final int NOTIFICATION_ID = 6199;
    private static final int INTENT_CODE = 9411;

    public static void showNotification(Context context){
        createChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context,
                NOTIFICATION_CHANNEL_ID)
                .setContentTitle("This is the title")
                .setContentText("Here is the text that is in the descripton")
                // small icon is absolutely necessary
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                // it sets the tap action
                .setContentIntent(getTapAction(context))
                // it dismisses the notification if user taps one it
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
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
}
