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
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutoLijstFragment extends Fragment {

    private RecyclerView recyclerView;

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
        //List<Car> cars = new ArrayList<>();


        api.GetDataFromCars(new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Car> cars = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject carData = jsonArray.getJSONObject(i);
                                cars.add(new Car(carData.getString("brand"), carData.getString("model"), carData.getString("fuel"),
                                        carData.getString("options"), carData.getString("licensePlate"), carData.getInt("engineSize"),
                                        carData.getInt("modelYear"), carData.getString("since"), carData.getInt("price"),
                                        carData.getInt("nrOfSeats"), carData.getString("body"), carData.getString("inspections"),
                                        carData.getString("repairs"), carData.getString("rentals")));
                            }
                            recyclerView.setAdapter(new CarListAdapter(cars));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();

            }
        }, "/api/cars");

        recyclerView = view.findViewById(R.id.rvAutoLijst);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}