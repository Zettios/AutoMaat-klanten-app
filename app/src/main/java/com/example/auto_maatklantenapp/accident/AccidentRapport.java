package com.example.auto_maatklantenapp.accident;

import com.example.auto_maatklantenapp.classes.Car;

public class AccidentRapport {

    String code;
    int odoMeter;
    String result;
    String photo;
    String photoContentType = "image/jpeg";
    String completed;
    String photos = null;
    String repairs = null;
    int carId = 0;
    String employee = null;
    int rentalId = 0;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOdoMeter() {
        return odoMeter;
    }

    public void setOdoMeter(int odoMeter) {
        this.odoMeter = odoMeter;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getPhotos() {
        return photos;
    }

    public String getRepairs() {
        return repairs;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getEmployee() {
        return employee;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }
}
