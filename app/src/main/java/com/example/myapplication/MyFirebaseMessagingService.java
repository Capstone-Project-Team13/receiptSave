package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
            sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        Intent intent = new Intent(this, Food.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final String CHANNEL_ID = "Notification Channer ID";
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_NAME = "Notification";
            final String CHANNEL_DESCRIPTION = "Notification Exp Date.";
            final int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 100, 200});
            channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.food);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.food);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setContentIntent(pendingIntent);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setContentTitle(title);
            builder.setVibrate(new long[]{500, 500});
        }
        manager.notify(0, builder.build());
    }
}
