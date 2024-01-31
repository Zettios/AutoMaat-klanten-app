package com.example.auto_maatklantenapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.auto_maatklantenapp.classes.Customer;

import java.util.List;

@Dao
public interface CustomerDao {
    @Query("SELECT * FROM customer WHERE id IN (:customerId)")
    List<Customer> loadAllByIds(int[] customerId);

    @Query("SELECT * FROM customer WHERE id = :customerId")
    Customer getCustomer(int customerId);

    @Insert(entity = Customer.class)
    void insertCustomer(Customer customers);

    @Delete
    void delete(Customer customer);

    @Query("DELETE FROM customer")
    void deleteAll();
}
