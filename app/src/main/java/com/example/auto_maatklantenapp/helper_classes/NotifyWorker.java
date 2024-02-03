package com.example.auto_maatklantenapp.helper_classes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.auto_maatklantenapp.MainActivity;
import com.example.auto_maatklantenapp.R;
import com.example.auto_maatklantenapp.SplashScreenActivity;

public class NotifyWorker extends Worker {
    Context context;
    public NotifyWorker(@NonNull Context context, WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        createNotification();
        return Result.success();
    }


    public void createNotification() {
        Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
        String notificationTitle = "Reservering staat klaar";
        String notificationText = "Uw auto reservering staat klaar.";

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), MainActivity.NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.auto_maat_logo_compact)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, notificationBuilder.build());
    }
}