package com.example.auto_maatklantenapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

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
        holder.getView().setText(cars.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }
}