package com.example.schooltracker;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

public class MyNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create channel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "my_channel", "My Notifications", NotificationManager.IMPORTANCE_HIGH
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                assert alarmManager != null;
                if (!alarmManager.canScheduleExactAlarms()) {
                    // Send user to settings
                    Intent intent2 = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    context.startActivity(intent2);
                }
            }
            nm.createNotificationChannel(channel);
        }

        // Build the notification
        Notification notification = new NotificationCompat.Builder(context, "my_channel")
                .setContentTitle("Scheduled Reminder")
                .setContentText("This is your scheduled notification!")
                .setSmallIcon(R.drawable.ic_launcher_background) // replace with your icon
                .setOngoing(true) // makes it unremovable until canceled programmatically
                .build();

        // Show the notification
        nm.notify(1, notification);
    }


}
