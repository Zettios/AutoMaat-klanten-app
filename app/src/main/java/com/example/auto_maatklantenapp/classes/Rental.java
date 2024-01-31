package com.example.auto_maatklantenapp.classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.auto_maatklantenapp.helper_classes.RentalState;

import java.time.LocalDate;

@Entity
public class Rental {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int uid;

    @ColumnInfo(name = "code")
    public String code;

    @ColumnInfo(name = "longitude")
    public Double longitude;

    @ColumnInfo(name = "latitude")
    public Double latitude;

    @ColumnInfo(name = "fromDate")
    public LocalDate fromDate;

    @ColumnInfo(name = "toDate")
    public LocalDate toDate;

    @ColumnInfo(name = "state")
    public RentalState state;

    @ColumnInfo(name = "car")
    public int carId;

    @ColumnInfo(name = "customer")
    public int customerId;

    public Rental(String code, Double longitude, Double latitude, LocalDate fromDate, LocalDate toDate, RentalState state, int carId, int customerId){
        this.code = code;
        this.longitude = longitude;
        this.latitude = latitude;
        this. fromDate = fromDate;
        this.toDate = toDate;
        this.state = state;
        this.carId = carId;
        this.customerId = customerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
