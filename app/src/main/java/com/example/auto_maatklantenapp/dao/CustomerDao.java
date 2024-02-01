package com.example.auto_maatklantenapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.auto_maatklantenapp.classes.Customer;

@Dao
public interface CustomerDao {
    @Query("SELECT * FROM customer WHERE id = :customerId")
    Customer getCustomer(int customerId);

    @Query("SELECT * FROM customer LIMIT 1")
    Customer getFirstCustomer();

    @Insert(entity = Customer.class)
    void insertCustomer(Customer customers);

    @Query("DELETE FROM customer")
    void deleteAll();
}
