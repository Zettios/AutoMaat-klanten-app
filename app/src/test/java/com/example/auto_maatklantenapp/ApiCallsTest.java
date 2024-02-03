package com.example.auto_maatklantenapp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.example.auto_maatklantenapp.accident.AccidentRapport;
import com.example.auto_maatklantenapp.classes.Rental;
import com.example.auto_maatklantenapp.helper_classes.ApiCalls;
import com.example.auto_maatklantenapp.helper_classes.RentalState;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ApiCallsTest {
    ApiCalls apiCalls;

    @Before
    public void initMocks() {
        apiCalls = new ApiCalls();
    }

    @Test
    public void createRentalPostObject() {
        Rental rentalData = Mockito.mock(Rental.class);
        when(rentalData.getCode()).thenReturn("001");
        when(rentalData.getLongitude()).thenReturn(0.0);
        when(rentalData.getLatitude()).thenReturn(0.0);
        when(rentalData.getFromDate()).thenReturn("2024-01-01");
        when(rentalData.getToDate()).thenReturn("2024-02-01");
        when(rentalData.getState()).thenReturn(RentalState.ACTIVE);
        when(rentalData.getCarId()).thenReturn(1);
        when(rentalData.getCustomerId()).thenReturn(1);

        JSONObject returnData = apiCalls.createRentalPostObject(rentalData);
        String expected = "{\"fromDate\":\"2024-01-01\",\"code\":\"001\",\"car\":{\"id\":1},\"latitude\":0,\"toDate\":\"2024-02-01\",\"state\":\"ACTIVE\",\"longitude\":0,\"customer\":{\"id\":1}}";
        assertEquals(expected, returnData.toString());
    }

    @Test
    public void createAccidentRapportObject() {
        AccidentRapport accidentRapport = Mockito.mock(AccidentRapport.class);
        when(accidentRapport.getCode()).thenReturn("001");
        when(accidentRapport.getOdoMeter()).thenReturn(50000);
        when(accidentRapport.getResult()).thenReturn("");
        when(accidentRapport.getPhoto()).thenReturn("");
        when(accidentRapport.getPhotoContentType()).thenReturn("image/jpeg");
        when(accidentRapport.getCompleted()).thenReturn("");
        when(accidentRapport.getCarId()).thenReturn(1);
        when(accidentRapport.getRentalId()).thenReturn(1);

        JSONObject returnData = apiCalls.createAccidentRapportObject(accidentRapport);
        String expected = "{\"result\":\"\",\"code\":\"001\",\"odometer\":50000,\"car\":{\"id\":1},\"photo\":\"\",\"completed\":\"\",\"photoContentType\":\"image\\/jpeg\",\"rental\":{\"id\":1}}";
        assertEquals(expected, returnData.toString());
    }
}
