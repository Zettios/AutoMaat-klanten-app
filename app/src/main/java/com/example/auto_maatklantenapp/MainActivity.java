package com.example.auto_maatklantenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.auto_maatklantenapp.database.AutoMaatDatabase;
import com.example.auto_maatklantenapp.listeners.BuildRentalNotification;
import com.example.auto_maatklantenapp.listeners.OnExpiredTokenListener;
import com.example.auto_maatklantenapp.listeners.OnInternetLossListener;
import com.example.auto_maatklantenapp.listeners.OnNavSelectionListener;
import com.example.auto_maatklantenapp.listeners.OnOnlineListener;

public class MainActivity extends AppCompatActivity implements OnNavSelectionListener,
        OnExpiredTokenListener,
        OnInternetLossListener,
        OnOnlineListener,
        BuildRentalNotification {

    public AutoMaatDatabase db;
    Boolean internetLossNotified = false;

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

    private final static String automaat_notification_channel = "Auto Maat Event Reminder";

    public void buildNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //define the importance level of the notification
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            //build the actual notification channel, giving it a unique ID and name
            NotificationChannel channel =
                    new NotificationChannel(automaat_notification_channel, automaat_notification_channel, importance);

            //we can optionally add a description for the channel
            String description = "A channel which shows notifications about events at Manasia";
            channel.setDescription(description);

            //we can optionally set notification LED colour
            channel.setLightColor(Color.MAGENTA);

            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().
                    getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void MakeNotification() {
        try {
            buildNotificationChannel();
            Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
            String notificationTitle = "Reservering staat klaar";
            String notificationText = "Uw auto reservering staat klaar.";

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(getApplicationContext(), automaat_notification_channel)
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
        } catch (Exception e) {
            Log.d("AutoMaatApp", e.toString());
        }
    }
}