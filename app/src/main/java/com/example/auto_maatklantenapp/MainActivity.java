package com.example.auto_maatklantenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.os.Bundle;

import com.example.auto_maatklantenapp.database.AutoMaatDatabase;
import com.example.auto_maatklantenapp.listeners.OnNavSelectionListener;

public class MainActivity extends AppCompatActivity implements OnNavSelectionListener {

    AutoMaatDatabase db;

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
}