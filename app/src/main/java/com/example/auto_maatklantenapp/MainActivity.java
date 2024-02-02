package com.example.auto_maatklantenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.auto_maatklantenapp.database.AutoMaatDatabase;
import com.example.auto_maatklantenapp.listeners.OnExpiredTokenListener;
import com.example.auto_maatklantenapp.listeners.OnInternetLossListener;
import com.example.auto_maatklantenapp.listeners.OnNavSelectionListener;
import com.example.auto_maatklantenapp.listeners.OnOnlineListener;

public class MainActivity extends AppCompatActivity implements OnNavSelectionListener,
                                                                OnExpiredTokenListener,
                                                                OnInternetLossListener,
                                                                OnOnlineListener {

    AutoMaatDatabase db;
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
}