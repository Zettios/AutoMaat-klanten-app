package com.example.auto_maatklantenapp.custom_adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auto_maatklantenapp.R;
import com.example.auto_maatklantenapp.classes.Rental;
import com.example.auto_maatklantenapp.helper_classes.ApiCallback;
import com.example.auto_maatklantenapp.helper_classes.ApiCalls;
import com.example.auto_maatklantenapp.view_holders.RentalViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class RentalListAdapter extends RecyclerView.Adapter<RentalViewHolder>{

    private List<Rental> rentals;

    public RentalListAdapter(List<Rental> rentals) {
        this.rentals = rentals;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.reservation_list_item;
    }

    @NonNull
    @Override
    public RentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RentalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalViewHolder holder, int position) {
        Rental rental = rentals.get(position);
        fetchCarDetails(rental, new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                try {
                    if (jsonArray.length() > 0) {
                        JSONObject carData = jsonArray.getJSONObject(0);

                        holder.getRentalCode().setText(carData.getString("brand") + " " + carData.getString("model"));
                        holder.getRentalPeriod().setText(rental.getFromDate() + " t/m " + rental.getToDate());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IOException e) {
                holder.getRentalCode().setText(String.valueOf(rentals.get(position).getCode()));
                holder.getRentalPeriod().setText(rentals.get(position).getFromDate() + " t/m " + rentals.get(position).getToDate());

                Log.w("AutoMaatApp", "Failed to fetch car details");
                e.printStackTrace();
            }
        });
    }

    private void fetchCarDetails(Rental rental, ApiCallback callback) {
        int carId = rental.getCarId();
        ApiCalls api = new ApiCalls();
        api.GetCarDetails(carId, callback);
    }

    @Override
    public int getItemCount() {
        return rentals.size();
    }
}
