package com.example.auto_maatklantenapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.auto_maatklantenapp.classes.Car;
import com.example.auto_maatklantenapp.classes.Customer;
import com.example.auto_maatklantenapp.classes.Rental;
import com.example.auto_maatklantenapp.converters.LocalDateConverter;
import com.example.auto_maatklantenapp.dao.CarDao;
import com.example.auto_maatklantenapp.dao.CustomerDao;
import com.example.auto_maatklantenapp.dao.RentalDao;

@Database(entities = {Car.class, Customer.class, Rental.class}, version = 8)
@TypeConverters(LocalDateConverter.class)
public abstract class AutoMaatDatabase extends RoomDatabase {
    public abstract CarDao carDao();
    public abstract CustomerDao customerDao();
    public abstract RentalDao rentalDao();
}
