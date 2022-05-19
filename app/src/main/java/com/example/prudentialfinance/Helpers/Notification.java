package com.example.prudentialfinance.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.prudentialfinance.HomeActivity;
import com.example.prudentialfinance.R;

public class Notification {
    private String title;
    private String content;

    public Notification() {
    }

    /***
     *
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    public void showNotification(Activity ctx, String title, String content)
    {
        int NOTIFICATION_ID = 234;
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CHANNEL_ID = "com.example.prudentialfinance";
            CharSequence name = "prudential_finance";
            String Description = "Prudential Finance";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }


        /*Step 1*/
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_message_24)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        /*Step 2*/
        Intent intent = new Intent(ctx, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /*Step 3*/
         PendingIntent pendingIntent =
                PendingIntent.getActivity(ctx, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT );
         builder.setContentIntent(pendingIntent);

         /*Step 4*/
        NotificationManagerCompat manager = NotificationManagerCompat.from(ctx);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
