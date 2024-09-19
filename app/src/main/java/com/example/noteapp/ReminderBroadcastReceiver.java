package com.example.noteapp;

import static com.example.noteapp.R.drawable.baseline_notifications_none_24;
import static com.example.noteapp.R.drawable.ic_baseline_notifications_24;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import java.util.Date;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    final String CHANNEL_ID = "201";
    private Object ic_baseline_notifications_24;

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("MyNote".equals(intent.getAction())) { // Kiểm tra chính xác action
            String time = intent.getStringExtra("time");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) { // Kiểm tra nếu NotificationManager không null
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
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24) // Kiểm tra lại icon
                        .setColor(Color.RED)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setPriority(NotificationCompat.PRIORITY_HIGH) // Đảm bảo hiển thị rõ
                        .setAutoCancel(true); // Tự động tắt sau khi nhấn

                notificationManager.notify(getNotificationId(), builder.build()); // Sử dụng ID cố định hoặc hợp lý hơn
            } else {
                // Log lỗi nếu NotificationManager là null
                System.err.println("NotificationManager is null");
            }
        }
    }

    private int getNotificationId() {
        return (int) (new Date().getTime() / 1000L % Integer.MAX_VALUE); // ID dựa trên thời gian để tránh quá lớn
    }
}
