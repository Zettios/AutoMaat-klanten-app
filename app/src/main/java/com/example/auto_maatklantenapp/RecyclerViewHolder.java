package com.example.auto_maatklantenapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView carModel;
    private TextView carInfo;
    private TextView price;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        carModel = itemView.findViewById(R.id.txtCarModel);
        carInfo = itemView.findViewById(R.id.txtCarInfo);
        price = itemView.findViewById(R.id.txtPrice);
    }

    public TextView getCarModel(){
        return carModel;
    }

    public TextView getCarInfo() {
        return carInfo;
    }

    public TextView getPrice() {
        return price;
    }
}