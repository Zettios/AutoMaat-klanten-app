package com.example.auto_maatklantenapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoLijstFragment extends Fragment {

    CarListAdapter carListAdapter;

    private RecyclerView recyclerView;
    private ImageButton ibOpenFilters;
    private List<Car> cars;
    private List<Car> allCars;
    private List<Car> filteredCars;

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

        filteredCars = new ArrayList<>();

        ArrayList<String> merkArray = new ArrayList<>();
        ArrayList<String> modelArray = new ArrayList<>();
        ArrayList<String> brandstofArray = new ArrayList<>();
        ArrayList<String> bodyArray = new ArrayList<>();
        AtomicInteger maxSeats = new AtomicInteger();
        AtomicInteger maxPrice = new AtomicInteger();

        api.GetDataFromCars(new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                getActivity().runOnUiThread(() -> {
                    cars = new ArrayList<>();
                    allCars = new ArrayList<>();
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

                            if (maxSeats.get() < carData.getInt("nrOfSeats")) {
                                maxSeats.set(carData.getInt("nrOfSeats"));
                            }

                            if (maxPrice.get() < carData.getInt("price")) {
                                maxPrice.set(carData.getInt("price"));
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

                            allCars.add(new Car(
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

                        carListAdapter = new CarListAdapter(cars);
                        recyclerView.setAdapter(carListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }
        }, "/api/cars");

        getChildFragmentManager().setFragmentResultListener("filterData", this,
                (requestKey, bundle) -> {
                    String filterMerk = bundle.getString("merk");
                    String filterModel = bundle.getString("model");
                    String filterBrandstof = bundle.getString("brandstof");
                    String filterBody = bundle.getString("body");
                    String filterAmountOfSeats = bundle.getString("amountOfSeats");
                    String filterMaxPrice = bundle.getString("maxPrice");

                    if (allCars != null) {
                        for (Car car : allCars) {
                            if (car.getBrand().equals(filterMerk) &&
                                    car.getModel().equals(filterModel) &&
                                    car.getFuel().equals(filterBrandstof) &&
                                    car.getBody().equals(filterBody) &&
                                    car.getNrOfSeats() <= Integer.parseInt(filterAmountOfSeats) &&
                                    car.getPrice() <= Integer.parseInt(filterMaxPrice)) {
                                this.filteredCars.add(car);
                            }
                        }
                    }

                    cars.clear();
                    cars.addAll(this.filteredCars);
                    carListAdapter.notifyDataSetChanged();
        });

        recyclerView = view.findViewById(R.id.rvAutoLijst);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        ibOpenFilters = view.findViewById(R.id.ibOpenFilters);
        ibOpenFilters.setOnClickListener(v -> {
            new CarFilterDialogFragment();
            CarFilterDialogFragment dFragment = CarFilterDialogFragment
                    .newInstance(merkArray, modelArray, brandstofArray, bodyArray,
                            maxSeats.get(), maxPrice.get());
            dFragment.show(getChildFragmentManager(), "CarFilterDialogFragment");
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}