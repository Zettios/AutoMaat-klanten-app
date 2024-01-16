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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
        ApiCalls api = new ApiCalls();

        ArrayList<String> merkArray = new ArrayList<>();
        ArrayList<String> modelArray = new ArrayList<>();
        ArrayList<String> brandstofArray = new ArrayList<>();
        ArrayList<String> bodyArray = new ArrayList<>();
        int[] maxSeats = { 0 };
        int maxPrice = 0;

        api.GetDataFromCars(new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                getActivity().runOnUiThread(() -> {
                    List<Car> cars = new ArrayList<>();
                    int tempMaxSeats = 0;
                    int tempMaxPrice = 0;
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject carData = jsonArray.getJSONObject(i);

                            if (!merkArray.contains(carData.getString("brand"))) {
                                merkArray.add(carData.getString("brand"));
                            }

                            if (!modelArray.contains(carData.getString("model"))) {
                                modelArray.add(carData.getString("model"));
                            }

                            if (!brandstofArray.contains(carData.getString("fuel"))) {
                                brandstofArray.add(carData.getString("fuel"));
                            }

                            if (!bodyArray.contains(carData.getString("body"))) {
                                bodyArray.add(carData.getString("body"));
                            }


                            if (tempMaxSeats < carData.getInt("nrOfSeats")) {
                             //   maxSeats = carData.getInt("nrOfSeats");
                            }

                            cars.add(new Car(
                                    carData.getString("brand"),
                                    carData.getString("model"),
                                    carData.getString("fuel"),
                                    carData.getString("options"),
                                    carData.getString("licensePlate"),
                                    carData.getInt("engineSize"),
                                    carData.getInt("modelYear"),
                                    carData.getString("since"),
                                    carData.getInt("price"),
                                    carData.getInt("nrOfSeats"),
                                    carData.getString("body"),
                                    carData.getString("inspections"),
                                    carData.getString("repairs"),
                                    carData.getString("rentals")));
                        }
                        recyclerView.setAdapter(new CarListAdapter(cars));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }
        });

        getChildFragmentManager().setFragmentResultListener("requestKey", this,
                (requestKey, bundle) -> {
            String result = bundle.getString("bundleKey");
            Log.v("FragmentCommunicationTest", result);
        });

        recyclerView = view.findViewById(R.id.rvAutoLijst);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        ibOpenFilters = view.findViewById(R.id.ibOpenFilters);
        ibOpenFilters.setOnClickListener(v -> {
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