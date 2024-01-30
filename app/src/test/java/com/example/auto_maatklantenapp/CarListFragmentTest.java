package com.example.auto_maatklantenapp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import com.example.auto_maatklantenapp.classes.Car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(RobolectricTestRunner.class)
public class CarListFragmentTest {

    CarListFragment av;

    @Before
    public void initMocks() {
        av = new CarListFragment();
        av.cars = new ArrayList<>();
        av.allCars = new ArrayList<>();
        av.filteredCars = new ArrayList<>();
        av.merkArray = new ArrayList<>();
        av.modelArray = new ArrayList<>();
        av.brandstofArray = new ArrayList<>();
        av.bodyArray = new ArrayList<>();
        av.maxSeats = new AtomicInteger();
        av.maxPrice = new AtomicInteger();
    }


    @Test
    public void formCarDataTest() throws JSONException {
        JSONArray data = Mockito.mock(JSONArray.class);
        when(data.length()).thenReturn(3);

        JSONObject data1 = Mockito.mock(JSONObject.class);
        when(data1.getString("brand")).thenReturn("Toyota");

        JSONObject data2 = Mockito.mock(JSONObject.class);
        when(data2.getString("brand")).thenReturn("Honda");

        JSONObject data3 = Mockito.mock(JSONObject.class);
        when(data3.getString("brand")).thenReturn("Ford");

        when(data.getJSONObject(0)).thenReturn(data1);
        when(data.getJSONObject(1)).thenReturn(data2);
        when(data.getJSONObject(2)).thenReturn(data3);

        av.formCarData(data);

        assertEquals("Toyota", av.allCars.get(0).getBrand());
        assertEquals("Honda", av.allCars.get(1).getBrand());
        assertEquals("Ford", av.allCars.get(2).getBrand());
    }

    @Test
    public void populateFilterDataArraysTest() {
        Car car1 = Mockito.mock(Car.class);
        when(car1.getBrand()).thenReturn("Toyota");
        when(car1.getModel()).thenReturn("Camry");
        when(car1.getFuel()).thenReturn("GASOLINE");
        when(car1.getBody()).thenReturn("SEDAN");
        when(car1.getNrOfSeats()).thenReturn(5);
        when(car1.getPrice()).thenReturn(100);

        Car car2 = Mockito.mock(Car.class);
        when(car2.getBrand()).thenReturn("BMW");
        when(car2.getModel()).thenReturn("3 Series");
        when(car2.getFuel()).thenReturn("HYBRID");
        when(car2.getBody()).thenReturn("SUV");
        when(car2.getNrOfSeats()).thenReturn(10);
        when(car2.getPrice()).thenReturn(105);

        av.populateFilterDataArrays(car1);
        av.populateFilterDataArrays(car2);

        assertEquals("Toyota", av.merkArray.get(0));
        assertEquals("BMW", av.merkArray.get(1));

        assertEquals("Camry", av.modelArray.get(0));
        assertEquals("3 Series", av.modelArray.get(1));

        assertEquals("GASOLINE", av.brandstofArray.get(0));
        assertEquals("HYBRID", av.brandstofArray.get(1));

        assertEquals("SEDAN", av.bodyArray.get(0));
        assertEquals("SUV", av.bodyArray.get(1));

        assertEquals(10, av.maxSeats.get());
        assertEquals(105, av.maxPrice.get());
    }

    @Test
    public void handleFilterDataTest() {
        CarListFragment av = new CarListFragment();
        av.cars = new ArrayList<>();
        av.allCars = new ArrayList<>();
        av.filteredCars = new ArrayList<>();

        Car car1 = Mockito.mock(Car.class);
        when(car1.getBrand()).thenReturn("Toyota");
        when(car1.getModel()).thenReturn("Camry");
        when(car1.getFuel()).thenReturn("GASOLINE");
        when(car1.getBody()).thenReturn("SEDAN");
        when(car1.getNrOfSeats()).thenReturn(5);
        when(car1.getPrice()).thenReturn(100);

        Car car2 = Mockito.mock(Car.class);
        when(car2.getBrand()).thenReturn("BMW");
        when(car2.getModel()).thenReturn("3 Series");
        when(car2.getFuel()).thenReturn("HYBRID");
        when(car2.getBody()).thenReturn("SUV");
        when(car2.getNrOfSeats()).thenReturn(10);
        when(car2.getPrice()).thenReturn(105);

        av.allCars.add(car1);
        av.allCars.add(car2);
        av.cars.add(car1);
        av.cars.add(car2);

        av.carListAdapter =  new CarListAdapter(av.cars);

        Bundle bundle = Mockito.mock(Bundle.class);
        when(bundle.getString("merk")).thenReturn("Toyota");
        when(bundle.getString("model")).thenReturn("Camry");
        when(bundle.getString("brandstof")).thenReturn("GASOLINE");
        when(bundle.getString("body")).thenReturn("SEDAN");
        when(bundle.getString("amountOfSeats")).thenReturn("5");
        when(bundle.getString("maxPrice")).thenReturn("100");

        av.handleFilterData(bundle);

        assertEquals("Toyota", av.filteredCars.get(0).getBrand());
        assertEquals("Camry", av.filteredCars.get(0).getModel());
        assertEquals("GASOLINE", av.filteredCars.get(0).getFuel());
        assertEquals("SEDAN", av.filteredCars.get(0).getBody());
        assertEquals(5, av.filteredCars.get(0).getNrOfSeats());
        assertEquals(100, av.filteredCars.get(0).getPrice());
    }
}