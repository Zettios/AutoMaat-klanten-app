package com.example.auto_maatklantenapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class AutoLijstFragment extends Fragment {

    private RecyclerView recyclerView;

    public AutoLijstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_lijst, container, false);

        List<Car> cars = new ArrayList<>();
        cars.add(new Car("test1"));
        cars.add(new Car("test2"));
        cars.add(new Car("test3"));
        cars.add(new Car("test4"));
        cars.add(new Car("test5"));
        cars.add(new Car("test6"));
        cars.add(new Car("test7"));
        cars.add(new Car("test8"));
        cars.add(new Car("test9"));
        cars.add(new Car("test10"));
        cars.add(new Car("test11"));
        cars.add(new Car("test12"));
        cars.add(new Car("test13"));
        cars.add(new Car("test14"));
        cars.add(new Car("test15"));
        cars.add(new Car("test16"));
        cars.add(new Car("test17"));
        cars.add(new Car("test18"));
        cars.add(new Car("test19"));
        cars.add(new Car("test20"));
        cars.add(new Car("test21"));


        recyclerView = view.findViewById(R.id.rvAutoLijst);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new CarListAdapter(cars));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}