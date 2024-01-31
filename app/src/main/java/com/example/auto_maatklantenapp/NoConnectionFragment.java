package com.example.auto_maatklantenapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;

public class NoConnectionFragment extends DialogFragment {
    public static NoConnectionFragment newInstance() {
        NoConnectionFragment fragment = new NoConnectionFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.no_connection_fragment, null);
        builder.setView(view);

        return builder.create();
    }
}
