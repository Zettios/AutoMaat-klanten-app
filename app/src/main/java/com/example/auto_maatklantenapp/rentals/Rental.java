package com.example.auto_maatklantenapp.rentals;

import com.example.auto_maatklantenapp.Car;
import com.example.auto_maatklantenapp.customer.Customer;

import java.time.LocalDate;

public class Rental {
    String code;
    Double longitude;
    Double latitude;
    LocalDate fromDate;
    LocalDate toDate;
    RentalState state;
    Car car;
    Customer customer;

    public Rental(String code, Double longitude, Double latitude, LocalDate fromDate, LocalDate toDate, RentalState state, Car car, Customer customer){
        this.code = code;
        this.longitude = longitude;
        this.latitude = latitude;
        this. fromDate = fromDate;
        this.toDate = toDate;
        this.state = state;
        this.car = car;
        this.customer = customer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
