package com.example.auto_maatklantenapp.custom_dialogs;

import android.app.Activity;
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

import com.example.auto_maatklantenapp.MainActivity;
import com.example.auto_maatklantenapp.R;
import com.example.auto_maatklantenapp.classes.Customer;
import com.example.auto_maatklantenapp.classes.Rental;
import com.example.auto_maatklantenapp.dao.CustomerDao;
import com.example.auto_maatklantenapp.helper_classes.ApiCallback;
import com.example.auto_maatklantenapp.helper_classes.ApiCalls;
import com.example.auto_maatklantenapp.helper_classes.InternetChecker;
import com.example.auto_maatklantenapp.helper_classes.RentalState;
import com.example.auto_maatklantenapp.listeners.BuildRentalNotification;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONArray;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CarReservationDialogFragment extends DialogFragment {
    FragmentManager fragmentManager;
    Activity activity;
    BuildRentalNotification buildRentalNotification;

    CustomerDao customerDao;
    Customer customer;

    InternetChecker internetChecker;

    TextView reservationDate;

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
                                        String price, FragmentManager fragmentManager,
                                        Activity activity) {
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
        this.activity = activity;
        this.internetChecker = new InternetChecker();
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
        reservationDate = view.findViewById(R.id.txtDetailReservationDate);
        Button pickDate = view.findViewById(R.id.btnPickDate);
        Button reserve = view.findViewById(R.id.btnReserverCar);

        buildRentalNotification = (BuildRentalNotification) getContext();

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

        customerDao = ((MainActivity) activity).db.customerDao();

        dateRangePicker.addOnPositiveButtonClickListener(v -> {
            from = new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date(dateRangePicker.getSelection().first));
            to = new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date(dateRangePicker.getSelection().second));

            String selectedReservationDate = to + " - " + from;
            reservationDate.setText(selectedReservationDate);
        });

        reserve.setOnClickListener(v -> {
            if (internetChecker.isOnline(getActivity())) {
                if (validateDate()) {
                    submitReservation();
                } else {
                    internetChecker.networkErrorDialog(getActivity(),
                            "Selecteer alstublieft een datum.");
                }
            } else {
                internetChecker.networkErrorDialog(getActivity(),
                        "U moet verbonden zijn met het internet om te kunnen reserveren.");
            }
        });

        return builder.create();
    }

    public boolean validateDate() {
        if (reservationDate.getText().equals("yyyy-mm-dd - yyyy-mm-dd")) {
            return false;
        }

        return true;
    }

    public void submitReservation() {
        ApiCalls apiCalls = new ApiCalls();
        new Thread(() -> {
            customer = customerDao.getFirstCustomer();
            final int rentalCode = new Random().nextInt(999999999);
            Rental rental = new Rental(0, String.valueOf(rentalCode), 0.0, 0.0,
                    from, to, RentalState.ACTIVE, carId, customer.getId());
            apiCalls.sendCarReservation(customer.getAuthToken(), rental, new ApiCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    try {
                        if ((int) jsonArray.get(0) == 201) {
                            buildRentalNotification.ScheduleRentalNotification(rentalCode, from);
                            dismiss();
                        } else {
                            Log.d("AutoMaatApp", "oop");
                        }
                    } catch (Exception e) {
                        Log.d("AutoMaatApp", e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IOException e) {
                    Log.d("AutoMaatApp", e.toString());
                    e.printStackTrace();
                }
            });
        }).start();

    }
}
