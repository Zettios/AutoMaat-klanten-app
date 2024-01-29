package com.example.auto_maatklantenapp.classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Car {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int uid;

    @ColumnInfo(name = "brand")
    public String brand;

    @ColumnInfo(name = "model")
    public String model;

    @ColumnInfo(name = "fuel")
    public String fuel;

    @ColumnInfo(name = "options")
    public String options;

    @ColumnInfo(name = "licensePlate")
    public String licensePlate;

    @ColumnInfo(name = "engineSize")
    public int engineSize;

    @ColumnInfo(name = "modelYear")
    public int modelYear;

    @ColumnInfo(name = "since")
    public String since;

    @ColumnInfo(name = "price")
    public int price;

    @ColumnInfo(name = "nrOfSeats")
    public int nrOfSeats;

    @ColumnInfo(name = "body")
    public String body;

    @ColumnInfo(name = "longitude")
    public float longitude;

    @ColumnInfo(name = "latitude")
    public float latitude;

    @ColumnInfo(name = "inspections")
    public String inspections;

    @ColumnInfo(name = "repairs")
    public String repairs;

    @ColumnInfo(name = "rentals")
    public String rentals;

    public Car(String brand, String model, String fuel, String options, String licensePlate,
               int engineSize, int modelYear, String since, int price, int nrOfSeats, String body,
               float longitude, float latitude) {
        this.brand = brand;
        this.model = model;
        this.fuel = fuel;
        this.options = options;
        this.licensePlate = licensePlate;
        this.engineSize = engineSize;
        this.modelYear = modelYear;
        this.since = since;
        this.price = price;
        this.nrOfSeats = nrOfSeats;
        this.body = body;
        this.longitude = longitude;
        this.latitude = latitude;
        this.inspections = null;
        this.repairs = null;
        this.rentals = null;
    }

    public Car(int uId, String brand, String model, String fuel, String options, String licensePlate,
               int engineSize, int modelYear, String since, int price, int nrOfSeats, String body,
               float longitude, float latitude) {
        this.uid = uId;
        this.brand = brand;
        this.model = model;
        this.fuel = fuel;
        this.options = options;
        this.licensePlate = licensePlate;
        this.engineSize = engineSize;
        this.modelYear = modelYear;
        this.since = since;
        this.price = price;
        this.nrOfSeats = nrOfSeats;
        this.body = body;
        this.longitude = longitude;
        this.latitude = latitude;
        this.inspections = null;
        this.repairs = null;
        this.rentals = null;
    }

    public int getUid() {
        return uid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(int engineSize) {
        this.engineSize = engineSize;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNrOfSeats() {
        return nrOfSeats;
    }

    public void setNrOfSeats(int nrOfSeats) {
        this.nrOfSeats = nrOfSeats;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


}
