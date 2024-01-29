package com.example.auto_maatklantenapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.auto_maatklantenapp.classes.Car;
import com.example.auto_maatklantenapp.dao.CarDao;

@Database(entities = {Car.class}, version = 2)

public abstract class AutoMaatDatabase extends RoomDatabase {
    public abstract CarDao carDao();
}
