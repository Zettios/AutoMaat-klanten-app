package com.example.auto_maatklantenapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class CarFilterDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_fragment_car_filters, null);
        builder.setView(view);

        Spinner merk = (Spinner) view.findViewById(R.id.spMerk);
        Spinner model = (Spinner) view.findViewById(R.id.spModel);
        Spinner brandstof = (Spinner) view.findViewById(R.id.spBrandstof);
        Spinner body = (Spinner) view.findViewById(R.id.spBodyType);

        Button cancel = (Button) view.findViewById(R.id.btnCancelFilters);

        cancel.setOnClickListener(v -> dismiss());

        List<String> merkArray = new ArrayList<>();
        merkArray.add("Volvo");
        merkArray.add("Volkswagen");

        List<String> modelArray = new ArrayList<>();
        modelArray.add("Volvo XC60");
        modelArray.add("Volkswagen Polo");

        List<String> brandstofArray = new ArrayList<>();
        brandstofArray.add("Elektrisch");
        brandstofArray.add("Gas");

        List<String> bodyArray = new ArrayList<>();
        bodyArray.add("Small");
        bodyArray.add("Medium");
        bodyArray.add("Big");

        ArrayAdapter<String> dataAdapterMerk = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, merkArray);
        ArrayAdapter<String> dataAdapterModel = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, modelArray);
        ArrayAdapter<String> dataAdapterBrandstof = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, brandstofArray);
        ArrayAdapter<String> dataAdapterBody = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, bodyArray);

        dataAdapterMerk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterBrandstof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterBody.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        merk.setAdapter(dataAdapterMerk);
        model.setAdapter(dataAdapterModel);
        brandstof.setAdapter(dataAdapterBrandstof);
        body.setAdapter(dataAdapterBody);

        return builder.create();
    }
}

