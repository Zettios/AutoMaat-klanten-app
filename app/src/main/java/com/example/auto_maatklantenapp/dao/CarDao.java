package com.example.auto_maatklantenapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.auto_maatklantenapp.classes.Car;

import java.util.List;

@Dao
public interface CarDao {
    @Query("SELECT * FROM car")
    List<Car> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Car> cars);

    @Query("DELETE FROM car")
    void deleteAll();
}
