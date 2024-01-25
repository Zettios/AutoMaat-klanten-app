package com.example.auto_maatklantenapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.auto_maatklantenapp.rentals.Rental;
import com.example.auto_maatklantenapp.rentals.RentalListAdapter;
import com.example.auto_maatklantenapp.rentals.RentalState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReserveringenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReserveringenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private RecyclerView recyclerView;

    RentalListAdapter rentalListAdapter;

    private String mParam1;
    private String mParam2;

    public ReserveringenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReserveringenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReserveringenFragment newInstance(String param1, String param2) {
        ReserveringenFragment fragment = new ReserveringenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserveringen, container, false);

        recyclerView = view.findViewById(R.id.rvRentalLijst);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        ApiCalls api = new ApiCalls();
        Log.w("myApp", "view created");

        try {
            Log.w("myApp", "inside try");
            api.Authenticate(new ApiCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    Log.w("myApp", "success, Now fetching rentals");
                    String authToken;
                    try {
                        authToken = api.getAuthToken();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Log.w("myApp", "AUTHTOKEN IN ONCREATEVIEW: " + authToken);
                    fetchRentals(authToken);
                }

                @Override
                public void onFailure(IOException e) {
                    Log.w("myApp", "failed");
                }
            });
        } catch (IOException e) {
            Log.w("myApp", "error");
            throw new RuntimeException(e);
        }
        return view;
    }

    private void fetchRentals(String authToken){
        Log.w("myApp", "fetchrentals called");
        ApiCalls api = new ApiCalls();
        api.GetAllRentals(new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                Log.w("myApp", "onsucces");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Rental> rentals = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject rentalData = jsonArray.getJSONObject(i);
                                String fromDateStr = rentalData.getString("fromDate");
                                String toDateStr = rentalData.getString("toDate");

                                LocalDate fromDate = LocalDate.parse(fromDateStr);
                                LocalDate toDate = LocalDate.parse(toDateStr);

                                RentalState state = RentalState.valueOf(rentalData.getString("state"));
                                rentals.add(new Rental(rentalData.getString("code"), rentalData.getDouble("longitude"),
                                        rentalData.getDouble("latitude"), fromDate,
                                        toDate, state,
                                        null, null
                                ));
                            }
                            Log.w("myApp", rentals.toString());
                            rentalListAdapter = new RentalListAdapter(rentals);
                            recyclerView.setAdapter(rentalListAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(IOException e) {
                Log.w("myApp", "onfailure");
                e.printStackTrace();

            }
        }, authToken, "/api/rentals");

    }
}