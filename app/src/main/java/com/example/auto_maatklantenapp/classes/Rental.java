package com.example.auto_maatklantenapp.classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.auto_maatklantenapp.helper_classes.RentalState;

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
    public String fromDate;

    @ColumnInfo(name = "toDate")
    public String toDate;

    @ColumnInfo(name = "state")
    public RentalState state;

    @ColumnInfo(name = "car")
    public int carId;

    @ColumnInfo(name = "customer")
    public int customerId;

    public Rental(int uid, String code, Double longitude, Double latitude, String fromDate, String toDate, RentalState state, int carId, int customerId){
        this.uid = uid;
        this.code = code;
        this.longitude = longitude;
        this.latitude = latitude;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.state = state;
        this.carId = carId;
        this.customerId = customerId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public RentalState getState() {
        return state;
    }

    public void setState(RentalState state) {
        this.state = state;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
