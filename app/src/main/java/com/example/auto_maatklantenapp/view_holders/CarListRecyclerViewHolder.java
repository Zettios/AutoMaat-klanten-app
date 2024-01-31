package com.example.auto_maatklantenapp.view_holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auto_maatklantenapp.R;

public class CarListRecyclerViewHolder extends RecyclerView.ViewHolder {

    private final TextView carModel;
    private final TextView carInfo;
    private final TextView carOptions;
    private final TextView price;

    public CarListRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        carModel = itemView.findViewById(R.id.txtCarModel);
        carInfo = itemView.findViewById(R.id.txtCarInfo);
        carOptions = itemView.findViewById(R.id.txtCarOptions);
        price = itemView.findViewById(R.id.txtPrice);
    }

    public TextView getCarModel(){
        return carModel;
    }

    public TextView getCarInfo() {
        return carInfo;
    }

    public TextView getCarOptions() {
        return carOptions;
    }

    public TextView getPrice() {
        return price;
    }

}