package com.example.auto_maatklantenapp.customer;

import java.time.LocalDate;

public class Customer {
    int nr;
    String lastName;
    String firstName;
    LocalDate from;

    public Customer(int nr, String lastName, String firstName, LocalDate from ){
        this.nr = nr;
        this.lastName = lastName;
        this.firstName = firstName;
        this.from = from;
    }
}
