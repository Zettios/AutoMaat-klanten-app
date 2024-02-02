package com.example.auto_maatklantenapp.classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Customer {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "systemId")
    public int systemId;

    @ColumnInfo(name = "nr")
    public int nr;

    @ColumnInfo(name = "login")
    public String login;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "firstName")
    public String firstName;

    @ColumnInfo(name = "lastName")
    public String lastName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "persistence")
    public Boolean persistence;

    @ColumnInfo(name = "authToken")
    public String authToken;

    public Customer() {};

    public Customer(int id, int systemId, int nr, String login, String password, String firstName, String lastName, String email, Boolean persistence,  String authToken){
        this.id = id;
        this.systemId = systemId;
        this.nr = nr;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.persistence = persistence;
        this.authToken = authToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getPersistence() {
        return persistence;
    }

    public void setPersistence(Boolean persistence) {
        this.persistence = persistence;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", persistence=" + persistence +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}
