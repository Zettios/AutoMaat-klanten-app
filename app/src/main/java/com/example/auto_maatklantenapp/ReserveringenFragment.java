package com.example.auto_maatklantenapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.auto_maatklantenapp.helper_classes.ApiCallback;
import com.example.auto_maatklantenapp.helper_classes.ApiCalls;
import com.example.auto_maatklantenapp.rentals.Rental;
import com.example.auto_maatklantenapp.custom_adapters.RentalListAdapter;
import com.example.auto_maatklantenapp.rentals.RentalState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReserveringenFragment extends Fragment {

    private RecyclerView recyclerView;
    RentalListAdapter rentalListAdapter;

    public ReserveringenFragment() {
        // Required empty public constructor
    }

    public static ReserveringenFragment newInstance() {
        return new ReserveringenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserveringen, container, false);

        ApiCalls api = new ApiCalls();

        recyclerView = view.findViewById(R.id.rvRentalLijst);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        try {
            api.LoginUser(new ApiCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    String authToken;
                    try {
                        authToken = api.getAuthToken();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    fetchRentals(authToken);
                }

                @Override
                public void onFailure(IOException e) {
                    e.printStackTrace();
                }
            }, "admin", "admin", false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    private void fetchRentals(String authToken){
        ApiCalls api = new ApiCalls();
        api.GetAllRentals(authToken, new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                getActivity().runOnUiThread(() -> {
                    List<Rental> rentals = new ArrayList<>();
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject rentalData = jsonArray.getJSONObject(i);
                            String fromDateStr = rentalData.getString("fromDate");
                            String toDateStr = rentalData.getString("toDate");

                            LocalDate fromDate = null;
                            LocalDate toDate = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                fromDate = LocalDate.parse(fromDateStr);
                                toDate = LocalDate.parse(toDateStr);
                            }


                            RentalState state = RentalState.valueOf(rentalData.getString("state"));
                            rentals.add(new Rental(rentalData.getString("code"), rentalData.getDouble("longitude"),
                                    rentalData.getDouble("latitude"), fromDate,
                                    toDate, state,
                                    null, null
                            ));
                        }
                        rentalListAdapter = new RentalListAdapter(rentals);
                        recyclerView.setAdapter(rentalListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(IOException e) {
                Log.w("myApp", "onfailure");
                e.printStackTrace();

            }
        });
    }
}