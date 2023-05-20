package com.example.coffeeorder.data;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.example.coffeeorder.R;

public class CoffeeOrderUtils {
    public static final String CHANNEL_ID = "610";

    public static void showNotify(Context c, String tittle,  String message){
        NotificationManager notificationManager = createNotificationChannel(c);
        //                   > Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Notification.Builder builder = new Notification.Builder(c.getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(tittle)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            notificationManager.notify(1, notification);
        }
        //                   < Android 8.0
        else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(c.getApplicationContext())
                    .setContentTitle(tittle)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            notificationManager.notify(1, notification);
        }

        //                    Rung thong bao
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) c.getApplicationContext().getSystemService(c.getApplicationContext().VIBRATOR_SERVICE)).vibrate(VibrationEffect.createWaveform(new long[]{0, 300, 150, 300}, -1));
        } else {
            ((Vibrator) c.getApplicationContext().getSystemService(c.getApplicationContext().VIBRATOR_SERVICE)).vibrate(new long[]{0, 300, 150, 300}, -1);
        }
    }

    public static NotificationManager createNotificationChannel(Context c) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        NotificationManager notificationManager = c.getApplicationContext().getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = c.getApplicationContext().getString(R.string.app_name);
            String description = c.getApplicationContext().getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);

        }
        return notificationManager;
    }
}
