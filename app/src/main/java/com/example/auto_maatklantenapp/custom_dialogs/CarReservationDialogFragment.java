package com.example.auto_maatklantenapp.custom_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.auto_maatklantenapp.R;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CarReservationDialogFragment extends DialogFragment {
    FragmentManager fragmentManager;
    int carId;
    String carBrandAndModel;
    String fuelType;
    String licensePlate;
    int modelYear;
    int seats;
    String bodyType;
    String options;
    String price;
    String from;
    String to;

    public CarReservationDialogFragment(int carId, String carBrandAndModel,
                                        String fuelType, String licensePlate,
                                        int modelYear, int seats,
                                        String bodyType, String options,
                                        String price, FragmentManager fragmentManager) {
        this.carId = carId;
        this.carBrandAndModel = carBrandAndModel;
        this.fuelType = fuelType;
        this.licensePlate = licensePlate;
        this.modelYear = modelYear;
        this.seats = seats;
        this.bodyType = bodyType;
        this.options = options;
        this.price = price;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_fragment_car_reservation, null);
        builder.setView(view);

        TextView detailCarNameModel = view.findViewById(R.id.txtDetailCarNameModel);
        TextView detailFuelType = view.findViewById(R.id.txtDetailFuel);
        TextView detailLicensePlate = view.findViewById(R.id.txtDetailLicensePlate);
        TextView detailModelYear = view.findViewById(R.id.txtDetailModelYear);
        TextView detailSeats = view.findViewById(R.id.txtDetailNrOfSeats);
        TextView detailBodyType = view.findViewById(R.id.txtDetailBody);
        TextView detailOptions = view.findViewById(R.id.txtDetailOption);
        TextView detailPrice = view.findViewById(R.id.txtDetailPrice);
        TextView reservationDate = view.findViewById(R.id.txtDetailReservationDate);
        Button pickDate = view.findViewById(R.id.btnPickDate);
        Button reserve = view.findViewById(R.id.btnReserverCar);

        detailCarNameModel.setText(carBrandAndModel);
        detailFuelType.setText(fuelType);
        detailLicensePlate.setText(licensePlate);
        detailModelYear.setText(String.valueOf(modelYear));
        detailSeats.setText(String.valueOf(seats));
        detailBodyType.setText(bodyType);
        detailOptions.setText(options);
        detailPrice.setText(String.valueOf(price));

        MaterialDatePicker.Builder<Pair<Long, Long>> dateRangePickerBuilder = MaterialDatePicker.Builder.dateRangePicker();
        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = dateRangePickerBuilder
                .setTitleText("Selecteer reservering datum.")
                .build();

        pickDate.setOnClickListener(v -> dateRangePicker.show(fragmentManager, "dateRangePickerBuilder"));

        dateRangePicker.addOnPositiveButtonClickListener(v -> {
            to = new SimpleDateFormat("dd/MM/yyyy")
                    .format(new Date(dateRangePicker.getSelection().first));
            from = new SimpleDateFormat("dd/MM/yyyy")
                    .format(new Date(dateRangePicker.getSelection().second));

            String selectedReservationDate = to + " - " + from;
            reservationDate.setText(selectedReservationDate);
        });

        reserve.setOnClickListener(v -> {});


        return builder.create();
    }
}
