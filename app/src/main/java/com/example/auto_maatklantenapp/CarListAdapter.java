package com.example.auto_maatklantenapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<Car> cars;

    public CarListAdapter(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.list_view_item;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.getCarModel().setText(cars.get(position).getModel());
        holder.getCarInfo().setText(cars.get(position).getBrand() + " | " + cars.get(position).getFuel() + " | " + cars.get(position).getNrOfSeats());
        holder.getPrice().setText(String.valueOf(cars.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {

        return cars.size();
    }
}