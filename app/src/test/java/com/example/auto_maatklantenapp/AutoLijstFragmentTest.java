package com.example.auto_maatklantenapp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoLijstFragmentTest {

    @Test
    public void populateFilterDataArrays() throws JSONException {
        AutoLijstFragment av = new AutoLijstFragment();
        av.merkArray = new ArrayList<>();
        av.modelArray = new ArrayList<>();
        av.brandstofArray = new ArrayList<>();
        av.bodyArray = new ArrayList<>();
        av.maxSeats = new AtomicInteger();
        av.maxPrice = new AtomicInteger();

        JSONObject car1 = Mockito.mock(JSONObject.class);
        when(car1.getString("brand")).thenReturn("Toyota");
        when(car1.getString("model")).thenReturn("Camry");
        when(car1.getString("fuel")).thenReturn("GASOLINE");
        when(car1.getString("body")).thenReturn("SEDAN");
        when(car1.getInt("nrOfSeats")).thenReturn(5);
        when(car1.getInt("price")).thenReturn(100);

        JSONObject car2 = Mockito.mock(JSONObject.class);
        when(car2.getString("brand")).thenReturn("BMW");
        when(car2.getString("model")).thenReturn("3 Series");
        when(car2.getString("fuel")).thenReturn("HYBRID");
        when(car2.getString("body")).thenReturn("SUV");
        when(car2.getInt("nrOfSeats")).thenReturn(10);
        when(car2.getInt("price")).thenReturn(105);

        av.populateFilterDataArrays(car1);
        av.populateFilterDataArrays(car2);

        System.out.println(av.merkArray);

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
}