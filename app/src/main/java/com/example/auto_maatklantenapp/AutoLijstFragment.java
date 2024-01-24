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
import android.widget.Button;
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
    private Button ibRefresh;
    private Button ibOpenFilters;
    private List<Car> cars;
    private List<Car> allCars;
    private List<Car> filteredCars;

    ArrayList<String> merkArray;
    ArrayList<String> modelArray;
    ArrayList<String> brandstofArray;
    ArrayList<String> bodyArray;
    AtomicInteger maxSeats;
    AtomicInteger maxPrice;

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

        merkArray = new ArrayList<>();
        modelArray = new ArrayList<>();
        brandstofArray = new ArrayList<>();
        bodyArray = new ArrayList<>();
        maxSeats = new AtomicInteger();
        maxPrice = new AtomicInteger();

        getCars(api);
        handleFilterData();

        recyclerView = view.findViewById(R.id.rvAutoLijst);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        ibRefresh = view.findViewById(R.id.ibRefresh);
        ibOpenFilters = view.findViewById(R.id.ibOpenFilters);

        ibRefresh.setOnClickListener(v -> {
            cars.clear();
            cars.addAll(this.allCars);
            carListAdapter.notifyDataSetChanged();
        });

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

    public void getCars(ApiCalls api) {
        api.GetDataFromCars(new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                getActivity().runOnUiThread(() -> {
                    cars = new ArrayList<>();
                    allCars = new ArrayList<>();
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject carData = jsonArray.getJSONObject(i);

                            populateFilterDataArrays(carData);

                            Car carItem = new Car(
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
                                    carData.getString("rentals"));

                            cars.add(carItem);
                            allCars.add(carItem);
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
        } , "/api/cars");
    }

    public void populateFilterDataArrays(JSONObject carData) throws JSONException {
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
    }

    public void handleFilterData() {
        getChildFragmentManager().setFragmentResultListener("filterData", this,
                (requestKey, bundle) -> {
                    String filterMerk = bundle.getString("merk");
                    String filterModel = bundle.getString("model");
                    String filterBrandstof = bundle.getString("brandstof");
                    String filterBody = bundle.getString("body");
                    String filterAmountOfSeats = bundle.getString("amountOfSeats");
                    String filterMaxPrice = bundle.getString("maxPrice");

                    this.filteredCars.clear();

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
    }
}