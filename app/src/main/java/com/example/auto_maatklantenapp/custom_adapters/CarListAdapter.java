package com.example.auto_maatklantenapp.custom_adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auto_maatklantenapp.R;
import com.example.auto_maatklantenapp.classes.Car;
import com.example.auto_maatklantenapp.custom_dialogs.CarFilterDialogFragment;
import com.example.auto_maatklantenapp.custom_dialogs.CarReservationDialogFragment;
import com.example.auto_maatklantenapp.view_holders.CarListRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter<CarListRecyclerViewHolder> {
    private List<Car> cars;
    private FragmentManager fragmentManager;
    private Activity activity;

    public CarListAdapter(List<Car> cars, FragmentManager fragmentManager, Activity activity) {
        this.cars = cars;
        this.fragmentManager = fragmentManager;
        this.activity = activity;
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

        holder.getFrameLayout().setOnClickListener(v -> {
            new CarFilterDialogFragment();
            CarReservationDialogFragment dFragment = new CarReservationDialogFragment(
                    cars.get(position).getUid(),
                    carBrandModel,
                    cars.get(position).getFuel(),
                    cars.get(position).getLicensePlate(),
                    cars.get(position).getModelYear(),
                    cars.get(position).getNrOfSeats(),
                    cars.get(position).getBody(),
                    carOptions,
                    carPrice,
                    fragmentManager,
                    activity);
            dFragment.show(fragmentManager, "CarReservationDialogFragment");
        });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }
}