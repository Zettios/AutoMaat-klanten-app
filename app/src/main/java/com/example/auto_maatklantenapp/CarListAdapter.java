package com.example.auto_maatklantenapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auto_maatklantenapp.classes.Car;

import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter<CarListRecyclerViewHolder> {
    private List<Car> cars;

    public CarListAdapter(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.car_list_item;
    }

    @NonNull
    @Override
    public CarListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new CarListRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarListRecyclerViewHolder holder, int position) {
        String carBrandModel = cars.get(position).getBrand() + " " + cars.get(position).getModel();
        String carInfo = cars.get(position).getFuel() + " | " + cars.get(position).getModelYear() +
                " | " + cars.get(position).getLicensePlate();
        String carOptions = cars.get(position).getOptions();
        String carPrice = cars.get(position).getPrice() + " d.d.";

        holder.getCarModel().setText(carBrandModel);
        holder.getCarInfo().setText(carInfo);
        holder.getCarOptions().setText(carOptions);
        holder.getPrice().setText(carPrice);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }
}