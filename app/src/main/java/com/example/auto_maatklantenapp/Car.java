package com.example.auto_maatklantenapp;

public class Car {

    String brand;
    String model;
    String fuel;
    String options;
    String licensePlate;
    int engineSize;
    int modelYear;
    String since;
    int price;
    int nrOfSeats;
    String body;
    String inspections;
    String repairs;

    String rentals;

    public Car(String brand, String model, String fuel, String options, String licensePlate, int engineSize, int modelYear, String since, int price, int nrOfSeats, String body, String inspections, String repairs, String rentals) {
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
        this.inspections = inspections;
        this.repairs = repairs;
        this.rentals = rentals;
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

    public String getInspections() {
        return inspections;
    }

    public void setInspections(String inspections) {
        this.inspections = inspections;
    }

    public String getRepairs() {
        return repairs;
    }

    public void setRepairs(String repairs) {
        this.repairs = repairs;
    }

    public String getRentals() {
        return rentals;
    }

    public void setRentals(String rentals) {
        this.rentals = rentals;
    }
}
