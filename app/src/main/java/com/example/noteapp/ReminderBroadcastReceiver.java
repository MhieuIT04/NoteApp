package com.example.noteapp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import java.util.Date;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    final String CHANNEL_ID = "201";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("MyNote".equals(intent.getAction())) {
            String time = intent.getStringExtra("time");

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                            "Reminder Channel",
                            NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription("Mô tả cho Reminder Channel");
                    notificationManager.createNotificationChannel(channel);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle("Báo thức: " + time)
                        .setContentText("Đến giờ thực hiện hoạt động rồi ==> " + time + " <==")
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setColor(Color.RED)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                notificationManager.notify(getNotificationId(), builder.build());
            } else {
                Log.e("ReminderBroadcastReceiver", "NotificationManager is null");
            }
        }

        // Request POST_NOTIFICATIONS permission if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (context instanceof Activity) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                } else {
                    Log.e("ReminderBroadcastReceiver", "Context is not an instance of Activity");
                }
            }
        }
    }

    private int getNotificationId() {
        return (int) (new Date().getTime() / 1000L % Integer.MAX_VALUE);
    }
}
