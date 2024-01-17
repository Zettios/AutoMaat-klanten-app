package com.example.auto_maatklantenapp.rentals;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auto_maatklantenapp.R;

public class RentalViewHolder extends RecyclerView.ViewHolder {

    private TextView rental;

    public TextView getRental() {
        return rental;
    }

    public TextView getRentalInfo() {
        return rentalInfo;
    }

    public TextView getPrice() {
        return price;
    }

    private TextView rentalInfo;
    private TextView price;
    public RentalViewHolder(@NonNull View itemView) {
        super(itemView);
        rental = itemView.findViewById(R.id.txtCarModel);
        rentalInfo = itemView.findViewById(R.id.txtCarInfo);
        price = itemView.findViewById(R.id.txtPrice);
    }
}
