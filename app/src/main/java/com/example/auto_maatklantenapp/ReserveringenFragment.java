package com.example.auto_maatklantenapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        api.GetAllRentals(new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
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
                            recyclerView.setAdapter(new RentalListAdapter(rentals));
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
        }, "/api/rentals");

//        recyclerView = view.findViewById(R.id.rvRentalLijst);
//        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}