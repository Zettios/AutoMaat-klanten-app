package com.example.auto_maatklantenapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

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

        getChildFragmentManager().setFragmentResultListener("requestKey", this,
                (requestKey, bundle) -> {
            String result = bundle.getString("bundleKey");
            Log.v("FragmentCommunicationTest", result);
        });

        recyclerView = view.findViewById(R.id.rvAutoLijst);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new CarListAdapter(cars));

        ibOpenFilters = view.findViewById(R.id.ibOpenFilters);
        ibOpenFilters.setOnClickListener(v -> {
            ArrayList<String> merkArray = new ArrayList<>();
            merkArray.add("Volvo");
            merkArray.add("Volkswagen");

            ArrayList<String> modelArray = new ArrayList<>();
            modelArray.add("Volvo XC60000000");
            modelArray.add("Volkswagen Polo");

            ArrayList<String> brandstofArray = new ArrayList<>();
            brandstofArray.add("Elektrisch");
            brandstofArray.add("Gas");

            ArrayList<String> bodyArray = new ArrayList<>();
            bodyArray.add("Small");
            bodyArray.add("Medium");
            bodyArray.add("Big");

            new CarFilterDialogFragment();
            CarFilterDialogFragment dFragment = CarFilterDialogFragment
                    .newInstance(merkArray, modelArray, brandstofArray, bodyArray,
                            6, 120);
            dFragment.show(getChildFragmentManager(), "CarFilterDialogFragment");
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}