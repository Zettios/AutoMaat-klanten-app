package com.example.auto_maatklantenapp.rentals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auto_maatklantenapp.Car;
import com.example.auto_maatklantenapp.R;
import com.example.auto_maatklantenapp.RecyclerViewHolder;

import java.util.List;

public class RentalListAdapter extends RecyclerView.Adapter<RentalViewHolder>{

    private List<Rental> rentals;

    public RentalListAdapter(List<Rental> rentals) {
        this.rentals = rentals;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.list_view_item;
    }

    @NonNull
    @Override
    public RentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RentalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalViewHolder holder, int position) {
        holder.getRental().setText(rentals.get(position).code);
    }

    @Override
    public int getItemCount() {

        return rentals.size();
    }
}