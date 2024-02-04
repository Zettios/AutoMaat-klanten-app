package com.example.auto_maatklantenapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
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
    int currentScreen = -1;

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
        Fragment fragment;
        switch (nav_id) {
            case 1:
                if (currentScreen != 1) {
                    fragment = CarListFragment.newInstance();
                    currentScreen = 1;
                    initFragment(fragmentManager, fragment);
                }
                break;
            case 2:
                if (currentScreen != 2) {
                    fragment = ReserveringenFragment.newInstance();
                    currentScreen = 2;
                    initFragment(fragmentManager, fragment);
                }
                break;
            case 3:
                if (currentScreen != 3) {
                    fragment = AccidentRapportFragment.newInstance();
                    currentScreen = 3;
                    initFragment(fragmentManager, fragment);
                }
                break;
            case 4:
                if (currentScreen != 4) {
                    fragment = SupportFragment.newInstance();
                    currentScreen = 4;
                    initFragment(fragmentManager, fragment);
                }
                break;
            case 5:
                if (currentScreen != 5) {
                    fragment = new LogoutFragment();
                    currentScreen = 5;
                    initFragment(fragmentManager, fragment);
                }
                break;
            default:
                fragment = CarListFragment.newInstance();
                initFragment(fragmentManager, fragment);
                currentScreen = 1;
                break;
        }
    }

    public void initFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);

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
}