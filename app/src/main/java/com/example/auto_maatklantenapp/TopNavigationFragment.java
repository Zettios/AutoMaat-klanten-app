package com.example.auto_maatklantenapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.auto_maatklantenapp.helper_classes.NavSelection;
import com.example.auto_maatklantenapp.listeners.OnNavSelectionListener;

public class TopNavigationFragment extends Fragment {
    OnNavSelectionListener onNavSelectionListener;
    ImageButton userBtn;

    public TopNavigationFragment() {
        super(R.layout.fragment_top_navigation);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onNavSelectionListener = (OnNavSelectionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnNavSelectionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_navigation, container, false);
        defineVariables(getActivity(), view);
        return view;
    }

    private void defineVariables(FragmentActivity activity, View view) {
        userBtn = view.findViewById(R.id.userBtn);

        //Should bring us to the logout fragment. Note: I'm programming blind :)
        userBtn.setOnClickListener(v -> {
            onNavSelectionListener.OnNavSelection(NavSelection.LOGOUT.getNumVal());
        });
    }
}