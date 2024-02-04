package com.example.auto_maatklantenapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

import com.example.auto_maatklantenapp.dao.CustomerDao;
import com.example.auto_maatklantenapp.helper_classes.NavSelection;
import com.example.auto_maatklantenapp.listeners.OnNavSelectionListener;

public class LogoutFragment extends Fragment {
    Button logoutBtn;
    OnNavSelectionListener onNavSelectionListener;
    CustomerDao customerDao;
    Handler logoutHandler;

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
        customerDao = ((MainActivity) activity).db.customerDao();
        logoutHandler = new Handler(Looper.getMainLooper());

        logoutBtn.setOnClickListener(v -> {
            new Thread(() -> {
                logoutUser();
                logoutHandler.post(() -> swapToLogin(activity));
            }).start();
        });
    }

    private void swapToLogin(Activity activity) {
        Intent i = new Intent(activity, LoginActivity.class);
        startActivity(i);
        activity.finish();
    }

    private void logoutUser() {
        customerDao.deleteAll();
    }
}
