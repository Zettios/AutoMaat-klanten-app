package com.example.auto_maatklantenapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

import androidx.room.Room;

import com.example.auto_maatklantenapp.dao.CustomerDao;
import com.example.auto_maatklantenapp.database.AutoMaatDatabase;
import com.example.auto_maatklantenapp.helper_classes.NavSelection;
import com.example.auto_maatklantenapp.listeners.OnNavSelectionListener;

public class LogoutFragment extends Fragment {
    Button logoutBtn, cancelBtn;
    OnNavSelectionListener onNavSelectionListener;
    CustomerDao customerDao;
    AutoMaatDatabase db;

    public LogoutFragment() { super(R.layout.fragment_logout); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        defineVariables(getActivity(), view);
        return view;
    }

    private void defineVariables(Activity activity, View view) {
        logoutBtn = view.findViewById(R.id.logoutBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        db = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                AutoMaatDatabase.class,
                "automaat_db")
                .fallbackToDestructiveMigration().build();
        customerDao = db.customerDao();

        cancelBtn.setOnClickListener(v -> {
            swapToMain();
        });

        logoutBtn.setOnClickListener(v -> {
            logoutUser();
            swapToLogin(activity);
        });
    }

    private void swapToMain(){
        onNavSelectionListener.OnNavSelection(NavSelection.CAR_LIST.getNumVal());
    }

    private void swapToLogin(Activity activity){
        Intent i = new Intent(activity, LoginActivity.class);
        startActivity(i);
        activity.finish();
    }

    private void logoutUser(){
        customerDao.deleteAll();
        Log.d("AutoMaatApp", "Logged user out.");
    }
}
