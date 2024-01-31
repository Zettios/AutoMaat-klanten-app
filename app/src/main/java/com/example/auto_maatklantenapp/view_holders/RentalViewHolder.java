package com.example.auto_maatklantenapp.view_holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auto_maatklantenapp.R;

public class RentalViewHolder extends RecyclerView.ViewHolder {

    private TextView rentalCode;
    private TextView rentalPeriod;

    public TextView getRentalCode() {
        return rentalCode;
    }

    public TextView getRentalPeriod() {
        return rentalPeriod;
    }


    public RentalViewHolder(@NonNull View itemView) {
        super(itemView);
        rentalCode = itemView.findViewById(R.id.txtReservationCode);
        rentalPeriod = itemView.findViewById(R.id.txtReservationDate);
    }
}
