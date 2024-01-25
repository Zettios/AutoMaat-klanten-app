package com.example.auto_maatklantenapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.auto_maatklantenapp.listeners.OnNavSelectionListener;

public class NavigationFragment extends Fragment {

    OnNavSelectionListener onNavSelectionListener;
    public NavigationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onNavSelectionListener = (OnNavSelectionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnNavSelectionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        ImageButton ibCarList = (ImageButton) view.findViewById(R.id.ibCarList);
        ImageButton ibCarReservations = (ImageButton) view.findViewById(R.id.ibCarReservations);
        ImageButton ibAccidentReport = (ImageButton) view.findViewById(R.id.ibAccidentReport);
        ImageButton ibSupport = (ImageButton) view.findViewById(R.id.ibSupport);

        ibCarList.setOnClickListener(v -> {
            onNavSelectionListener.OnNavSelection(NavSelection.CAR_LIST.getNumVal());
        });

        ibCarReservations.setOnClickListener(v -> {
            onNavSelectionListener.OnNavSelection(NavSelection.CAR_RESERVATION.getNumVal());
        });

        ibAccidentReport.setOnClickListener(v -> {
            onNavSelectionListener.OnNavSelection(NavSelection.ACCIDENT_REPORT.getNumVal());
        });

        ibSupport.setOnClickListener(v -> {
            onNavSelectionListener.OnNavSelection(NavSelection.SUPPORT.getNumVal());
        });

        return view;
    }
}