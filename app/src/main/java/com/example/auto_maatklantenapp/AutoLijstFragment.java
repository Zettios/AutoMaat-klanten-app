package com.example.auto_maatklantenapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class AutoLijstFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageButton ibOpenFilters;

    public AutoLijstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_lijst, container, false);

        List<Car> cars = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            cars.add(new Car("Brand "+i, "Model "+i, "GASOLINE", "None",
                    "G-241-GH", 1, 1990, "1990-11-02", 999,
                    4, "STATIONWAGON", "", "", ""));
        }

        recyclerView = view.findViewById(R.id.rvAutoLijst);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new CarListAdapter(cars));

        ibOpenFilters = view.findViewById(R.id.ibOpenFilters);
        ibOpenFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CarFilterDialogFragment().show(getChildFragmentManager(), "TAG");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}