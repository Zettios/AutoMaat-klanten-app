package com.example.auto_maatklantenapp;

import androidx.annotation.NonNull;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements OnNavSelectionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void GetDataFromCars(){
        OkHttpClient client = new OkHttpClient();

        String url = "https://measured-adder-concrete.ngrok-free.app/api/cars";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String myResponse = response.body().string();
                }

            }
        });
    }

    @Override
    public void OnNavSelection(int nav_id) {
        Log.d("Nav test", "Send id: " + nav_id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment;

        switch (nav_id) {
            case 1: fragment = new AutoLijstFragment(); break;
            case 2: fragment = new ReserveringenFragment(); break;
            case 3: fragment = new SchadeMeldingFragment(); break;
            case 4: fragment = new SupportFragment(); break;
            default: fragment = new AutoLijstFragment(); break;
        }

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fcvFragmentContainer, fragment, "");
        fragmentTransaction.commit();
    }
}