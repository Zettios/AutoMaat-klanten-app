package com.example.auto_maatklantenapp;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.auto_maatklantenapp.listeners.OnNavSelectionListener;

public class MainActivity extends AppCompatActivity implements OnNavSelectionListener {

    int currentActivity = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void OnNavSelection(int nav_id) {
        Log.d("Nav test", "Send id: " + nav_id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment;

        if (this.currentActivity != nav_id) {
            this.currentActivity = nav_id;

            switch (nav_id) {
                case 1:
                    fragment = new AutoLijstFragment();
                    break;
                case 2:
                    fragment = new ReserveringenFragment();
                    break;
                case 3:
                    fragment = SchadeMeldingFragment.newInstance();
                    break;
                case 4:
                    fragment = new SupportFragment();
                    break;
                default:
                    fragment = new AutoLijstFragment();
                    break;
            }

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fcvFragmentContainer, fragment, "");
            fragmentTransaction.commit();
        }
    }
}