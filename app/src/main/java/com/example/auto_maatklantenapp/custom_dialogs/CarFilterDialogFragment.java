package com.example.auto_maatklantenapp.custom_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.example.auto_maatklantenapp.R;

import java.util.ArrayList;

public class CarFilterDialogFragment extends DialogFragment {

    int maximumSeats = 4;
    int maximumPrice = 100;

    public static CarFilterDialogFragment newInstance(
                                                ArrayList<String> merk, ArrayList<String> model,
                                                ArrayList<String> brandstof, ArrayList<String> body,
                                                int maxStoelen, int maxPrice) {
        CarFilterDialogFragment fragment = new CarFilterDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("merk", merk);
        bundle.putStringArrayList("model", model);
        bundle.putStringArrayList("brandstof", brandstof);
        bundle.putStringArrayList("body", body);
        bundle.putInt("maxStoelen", maxStoelen);
        bundle.putInt("maxPrice", maxPrice);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_fragment_car_filters, null);
        builder.setView(view);

        Spinner merk = view.findViewById(R.id.spMerk);
        Spinner model = view.findViewById(R.id.spModel);
        Spinner brandstof = view.findViewById(R.id.spBrandstof);
        Spinner body = view.findViewById(R.id.spBodyType);
        EditText amountOfSeats = view.findViewById(R.id.etAantalStoelen);
        EditText maxPrice = view.findViewById(R.id.etMaxPrice);

        Button cancel = view.findViewById(R.id.btnCancelFilters);
        Button apply = view.findViewById(R.id.btnApplyFilters);

        amountOfSeats.setText(String.valueOf(maximumSeats));
        maxPrice.setText(String.valueOf(maximumPrice));

        cancel.setOnClickListener(v -> dismiss());
        apply.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putString("merk", merk.getSelectedItem().toString());
            result.putString("model", model.getSelectedItem().toString());
            result.putString("brandstof", brandstof.getSelectedItem().toString());
            result.putString("body", body.getSelectedItem().toString());
            result.putString("amountOfSeats", amountOfSeats.getText().toString());
            result.putString("maxPrice", maxPrice.getText().toString());
            getParentFragmentManager().setFragmentResult("filterData", result);
            dismiss();
        });

        if (getArguments() != null) {
            maximumPrice = getArguments().getInt("maxPrice");
            maximumSeats = getArguments().getInt("maxStoelen");
            maxPrice.setText(String.valueOf(maximumPrice));
            amountOfSeats.setText(String.valueOf(maximumSeats));

            populateSpinners(merk, model, brandstof, body);
        }

        return builder.create();
    }

    public void populateSpinners(Spinner merk, Spinner model, Spinner brandstof, Spinner body){
        ArrayList<String> merkArrayList = getArguments().getStringArrayList("merk");
        ArrayList<String> modelArrayList = getArguments().getStringArrayList("model");
        ArrayList<String> brandstofArrayList = getArguments().getStringArrayList("brandstof");
        ArrayList<String> bodyArrayList = getArguments().getStringArrayList("body");

        if (merkArrayList != null && modelArrayList != null &&
                brandstofArrayList != null && bodyArrayList != null) {
            ArrayAdapter<String> dataAdapterMerk = new ArrayAdapter<>(
                    requireActivity(), android.R.layout.simple_spinner_item, merkArrayList);
            ArrayAdapter<String> dataAdapterModel = new ArrayAdapter<>(
                    requireActivity(), android.R.layout.simple_spinner_item, modelArrayList);
            ArrayAdapter<String> dataAdapterBrandstof = new ArrayAdapter<>(
                    requireActivity(), android.R.layout.simple_spinner_item, brandstofArrayList);
            ArrayAdapter<String> dataAdapterBody = new ArrayAdapter<>(
                    requireActivity(), android.R.layout.simple_spinner_item, bodyArrayList);

            dataAdapterMerk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapterBrandstof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapterBody.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            merk.setAdapter(dataAdapterMerk);
            model.setAdapter(dataAdapterModel);
            brandstof.setAdapter(dataAdapterBrandstof);
            body.setAdapter(dataAdapterBody);
        }
    }
}

