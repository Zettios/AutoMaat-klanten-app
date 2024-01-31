package com.example.auto_maatklantenapp.classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Customer {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int nr;

    @ColumnInfo(name = "firstName")
    public String firstName;

    @ColumnInfo(name = "lastName")
    public String lastName;

    @ColumnInfo(name = "persistence")
    public Boolean persistence;

    @ColumnInfo(name = "authToken")
    public String authToken;

    public Customer(int nr, String firstName, String lastName, Boolean persistence,  String authToken){
        this.nr = nr;
        this.firstName = firstName;
        this.lastName = lastName;
        this.persistence = persistence;
        this.authToken = authToken;
    }
}
