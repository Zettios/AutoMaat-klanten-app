package com.example.auto_maatklantenapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.auto_maatklantenapp.classes.Car;
import com.example.auto_maatklantenapp.classes.Customer;
import com.example.auto_maatklantenapp.custom_adapters.CarListAdapter;
import com.example.auto_maatklantenapp.dao.CustomerDao;
import com.example.auto_maatklantenapp.dao.RentalDao;
import com.example.auto_maatklantenapp.helper_classes.ApiCallback;
import com.example.auto_maatklantenapp.helper_classes.ApiCalls;
import com.example.auto_maatklantenapp.classes.Rental;
import com.example.auto_maatklantenapp.custom_adapters.RentalListAdapter;
import com.example.auto_maatklantenapp.helper_classes.InternetChecker;
import com.example.auto_maatklantenapp.helper_classes.RentalState;
import com.example.auto_maatklantenapp.listeners.OnExpiredTokenListener;
import com.example.auto_maatklantenapp.listeners.OnInternetLossListener;
import com.example.auto_maatklantenapp.listeners.OnOnlineListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReserveringenFragment extends Fragment {
    CustomerDao customerDao;
    Customer customer;
    RentalDao rentalDao;

    List<Rental> rentals;
    List<Rental> allRentals;

    OnInternetLossListener onInternetLossListener;
    OnOnlineListener onOnlineListener;
    OnExpiredTokenListener onExpiredTokenListener;

    InternetChecker internetChecker;
    RecyclerView recyclerView;
    RentalListAdapter rentalListAdapter;

    ArrayList<String> codeArray;

    public ReserveringenFragment() {
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
        defineVariables(getActivity(), view);

        if (internetChecker.isOnline(getActivity())) {
            onOnlineListener.ResetOfflineVariable();
            new Thread(() -> {
                customer = customerDao.getFirstCustomer();
                fetchRentals(customer, customer.authToken);
            }).start();
        } else {
            onInternetLossListener.NotifyInternetLoss();
            new Thread(this::getOfflineReserveringen).start();
        }

        return view;
    }

    public void populateFilterDataArrays(Rental rentalData) {
        Log.w("AutoMaatApp", "CodeArray: " + codeArray);
        if (!codeArray.contains(rentalData.getCode())) {
            codeArray.add(rentalData.getCode());
        }

    }

    private void getOfflineReserveringen() {
        rentals = rentalDao.getAll();
        allRentals = rentalDao.getAll();

        for (int index = 0; index < rentals.size(); index++) {
            populateFilterDataArrays(rentals.get(index));
        }

        getActivity().runOnUiThread(() -> {
            rentalListAdapter = new RentalListAdapter(rentals);
            recyclerView.setAdapter(rentalListAdapter);
        });
    }

    private void defineVariables(Activity activity, View view) {
        customerDao = ((MainActivity) activity).db.customerDao();
        rentalDao = ((MainActivity) activity).db.rentalDao();

        codeArray = new ArrayList<>();

        internetChecker = new InternetChecker();

        onInternetLossListener = (OnInternetLossListener) getContext();
        onOnlineListener = (OnOnlineListener) getContext();
        onExpiredTokenListener = (OnExpiredTokenListener) getContext();

        recyclerView = view.findViewById(R.id.rvRentalLijst);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void fetchRentals(Customer customer, String authToken){
        ApiCalls api = new ApiCalls();

        api.GetAllRentals(authToken, customer.getId(), new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                List<Rental> rentals = new ArrayList<>();
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject rentalData = jsonArray.getJSONObject(i);

                        int uid = rentalData.getInt("id");
                        String code = rentalData.getString("code");
                        Double longitude = rentalData.getDouble("longitude");
                        Double latitude = rentalData.getDouble("latitude");
                        String fromDate = rentalData.getString("fromDate");
                        String toDate = rentalData.getString("toDate");
                        RentalState state = RentalState.valueOf(rentalData.getString("state"));
                        int customerId = rentalData.getJSONObject("customer").getInt("id");
                        int carId = rentalData.getJSONObject("car").getInt("id");

                        rentals.add(new Rental(uid, code, longitude, latitude,
                                fromDate, toDate, state, carId, customerId));
                    }

                    rentalDao.deleteAll();
                    rentalDao.insertAll(rentals);
                } catch (Exception e) {
                    Log.d("AutoMaatApp", e.toString());
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(() -> {
                    rentalListAdapter = new RentalListAdapter(rentals);
                    recyclerView.setAdapter(rentalListAdapter);
                });
            }

            @Override
            public void onFailure(IOException e) {
                if (e.getMessage().equals("401")) {
                    customerDao.deleteAll();
                    getActivity().runOnUiThread(() -> {
                        Toast toast = Toast.makeText(getActivity(), "Log opnieuw in", Toast.LENGTH_SHORT);
                        toast.show();
                        onExpiredTokenListener.ReturnToLogin();
                    });
                } else if (e.getMessage().equals("404")) {
                    getOfflineReserveringen();
                } else {
                    Log.w("AutoMaatApp", e.toString());
                    e.printStackTrace();
                }
            }
        });
    }
}