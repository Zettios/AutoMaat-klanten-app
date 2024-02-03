package com.example.auto_maatklantenapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

import android.Manifest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.auto_maatklantenapp.database.AutoMaatDatabase;
import com.example.auto_maatklantenapp.helper_classes.NotifyWorker;
import com.example.auto_maatklantenapp.listeners.BuildRentalNotification;
import com.example.auto_maatklantenapp.listeners.OnExpiredTokenListener;
import com.example.auto_maatklantenapp.listeners.OnInternetLossListener;
import com.example.auto_maatklantenapp.listeners.OnNavSelectionListener;
import com.example.auto_maatklantenapp.listeners.OnOnlineListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements OnNavSelectionListener,
        OnExpiredTokenListener,
        OnInternetLossListener,
        OnOnlineListener,
        BuildRentalNotification {

    public AutoMaatDatabase db;
    Boolean internetLossNotified = false;

    public final static String NOTIFICATION_CHANNEL_ID = "AutoMaat-01";
    public final static String NOTIFICATION_CHANNEL_NAME = "AutoMaat Huurauto Herinnering";
    public static final String workTag = "notificationWork";
    int reservationCode;
    String fromDate;

    ActivityResultLauncher<String> pushNotificationPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AutoMaatDatabase.class,
                        getResources().getString(R.string.database_name))
                .fallbackToDestructiveMigration()
                .build();

        pushNotificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    buildNotificationChannel();
                    Data inputData = new Data.Builder().putInt("AutoMaat"+reservationCode, reservationCode).build();

                    try {
                        Long currentTime = System.currentTimeMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(fromDate);
                        long millis = date.getTime();

                        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                                .setInitialDelay(millis - currentTime, TimeUnit.MILLISECONDS)
                                //.setInitialDelay(180000, TimeUnit.MILLISECONDS)
                                .setInputData(inputData)
                                .addTag(workTag)
                                .build();

                        WorkManager.getInstance(MainActivity.this).enqueue(notificationWork);
                    } catch (Exception e) {
                        Log.d("AutoMaatApp", e.toString());
                        e.printStackTrace();
                    }
                }
        );
    }

    @Override
    public void OnNavSelection(int nav_id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment;

        switch (nav_id) {
            case 1:
                fragment = CarListFragment.newInstance();
                break;
            case 2:
                fragment = ReserveringenFragment.newInstance();
                break;
            case 3:
                fragment = AccidentRapportFragment.newInstance();
                break;
            case 4:
                fragment = SupportFragment.newInstance();
                break;
            case 5:
                fragment = new LogoutFragment();
                break;
            default:
                fragment = CarListFragment.newInstance();
                break;
        }

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fcvFragmentContainer, fragment, "");
        fragmentTransaction.commit();
    }

    @Override
    public void ReturnToLogin() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void NotifyInternetLoss() {
        if (!internetLossNotified) {
            Toast toast = Toast.makeText(this, "U bent offline. Sommige functies zullen niet meer werken en gegevens kunnen oud zijn.", Toast.LENGTH_LONG);
            toast.show();
            internetLossNotified = true;
        }
    }

    @Override
    public void ResetOfflineVariable() {
        if (internetLossNotified) {
            internetLossNotified = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void ScheduleRentalNotification(int rentalCode, String fromDate) {
        this.reservationCode = rentalCode;
        this.fromDate = fromDate;
        pushNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
    }


    public void buildNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "Herinnering voor de huurauto.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createNotification() {
        Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
        String notificationTitle = "Reservering staat klaar";
        String notificationText = "Uw auto reservering staat klaar.";

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.auto_maat_logo_compact)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, notificationBuilder.build());
    }
}