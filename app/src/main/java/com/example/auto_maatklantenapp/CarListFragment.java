package com.example.auto_maatklantenapp;

import android.app.Activity;
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

import com.example.auto_maatklantenapp.classes.Car;
import com.example.auto_maatklantenapp.custom_adapters.CarListAdapter;
import com.example.auto_maatklantenapp.custom_dialogs.CarFilterDialogFragment;
import com.example.auto_maatklantenapp.helper_classes.ApiCallback;
import com.example.auto_maatklantenapp.helper_classes.ApiCalls;
import com.example.auto_maatklantenapp.helper_classes.InternetChecker;
import com.example.auto_maatklantenapp.dao.CarDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CarListFragment extends Fragment {

    CarDao carDao;
    InternetChecker internetChecker;
    CarListAdapter carListAdapter;

    RecyclerView recyclerView;
    List<Car> cars;
    List<Car> allCars;
    List<Car> filteredCars;

    ArrayList<String> merkArray;
    ArrayList<String> modelArray;
    ArrayList<String> brandstofArray;
    ArrayList<String> bodyArray;
    AtomicInteger maxSeats;
    AtomicInteger maxPrice;

    public CarListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_car_list, container, false);
        ApiCalls api = new ApiCalls();
        defineVariables(getActivity(), view);

        if (internetChecker.isOnline(getActivity())) {
            getCars(api);
        } else {
            new Thread(this::getOfflineCars).start();
        }

        setFilterDataListener();

        Button ibRefresh = view.findViewById(R.id.ibRefresh);
        Button ibOpenFilters = view.findViewById(R.id.ibOpenFilters);

        ibRefresh.setOnClickListener(v -> {
            if (internetChecker.isOnline(getActivity())) {
                refreshCars(api);
            } else {
                refreshOfflineCars();
            }
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

    private void defineVariables(Activity activity, View view) {
        carDao = ((MainActivity) activity).db.carDao();

        internetChecker = new InternetChecker();

        filteredCars = new ArrayList<>();

        merkArray = new ArrayList<>();
        modelArray = new ArrayList<>();
        brandstofArray = new ArrayList<>();
        bodyArray = new ArrayList<>();
        maxSeats = new AtomicInteger();
        maxPrice = new AtomicInteger();

        recyclerView = view.findViewById(R.id.rvAutoLijst);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void getCars(ApiCalls api) {
        api.GetAllCars(new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                cars = new ArrayList<>();
                allCars = new ArrayList<>();
                try {
                    formCarData(jsonArray);
                    storeCarDataLocally(allCars);

                    getActivity().runOnUiThread(() -> {
                        carListAdapter = new CarListAdapter(cars);
                        recyclerView.setAdapter(carListAdapter);
                    });
                } catch (Exception e) {
                    Log.e("AutoMaatApp", e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void refreshCars(ApiCalls api) {
        api.GetAllCars(new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                cars.clear();
                allCars.clear();

                merkArray.clear();
                modelArray.clear();
                brandstofArray.clear();
                bodyArray.clear();
                maxSeats.set(0);
                maxPrice.set(0);

                try {
                    formCarData(jsonArray);

                    storeCarDataLocally(allCars);

                    getActivity().runOnUiThread(() -> {
                        carListAdapter.notifyDataSetChanged();
                    });
                } catch (Exception e) {
                    Log.e("AutoMaatApp", e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void formCarData(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject carData = jsonArray.getJSONObject(i);
            Car carItem = new Car(
                    carData.getInt("id"),
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
                    (float) carData.getDouble("longitude"),
                    (float) carData.getDouble("latitude")
            );
            populateFilterDataArrays(carItem);
            cars.add(carItem);
            allCars.add(carItem);
        }
    }

    public void populateFilterDataArrays(Car carData) {
        if (!merkArray.contains(carData.getBrand())) {
            merkArray.add(carData.getBrand());
        }

        if (!modelArray.contains(carData.getModel())) {
            modelArray.add(carData.getModel());
        }

        if (!brandstofArray.contains(carData.getFuel())) {
            brandstofArray.add(carData.getFuel());
        }

        if (!bodyArray.contains(carData.getBody())) {
            bodyArray.add(carData.getBody());
        }

        if (maxSeats.get() < carData.getNrOfSeats()) {
            maxSeats.set(carData.getNrOfSeats());
        }

        if (maxPrice.get() < carData.getPrice()) {
            maxPrice.set(carData.getPrice());
        }
    }

    public void storeCarDataLocally(List<Car> cars) {
        carDao.deleteAll();
        carDao.insertAll(cars);
    }

    private void getOfflineCars() {
        cars = carDao.getAll();
        allCars = carDao.getAll();

        for (int index = 0; index < cars.size(); index++) {
            populateFilterDataArrays(cars.get(index));
        }

        getActivity().runOnUiThread(() -> {
            carListAdapter = new CarListAdapter(cars);
            recyclerView.setAdapter(carListAdapter);
        });
    }

    private void refreshOfflineCars() {
        cars.clear();
        cars.addAll(this.allCars);
        carListAdapter.notifyDataSetChanged();
    }

    public void setFilterDataListener() {
        getChildFragmentManager().setFragmentResultListener("filterData", this,
                (requestKey, bundle) -> {
                    handleFilterData(bundle);
        });
    }

    public void handleFilterData(Bundle bundle) {
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
    }
}