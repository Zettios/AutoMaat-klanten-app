package com.example.auto_maatklantenapp.accident;

public class AccidentRapport {

    String code;
    int odoMeter;
    String result;
    String photo;
    String photoContentType = "image/jpeg";
    String completed;
    String photos = null;
    String repairs = null;
    String cars = null;
    String employee = null;
    String rental = null;


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

    public String getCars() {
        return cars;
    }

    public String getEmployee() {
        return employee;
    }

    public String getRental() {
        return rental;
    }
}
