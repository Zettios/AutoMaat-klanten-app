package com.example.auto_maatklantenapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.auto_maatklantenapp.classes.Car;
import com.example.auto_maatklantenapp.classes.Rental;

import java.util.List;

@Dao
public interface RentalDao {
    @Query("SELECT * FROM rental")
    List<Rental> getAll();

    @Query("SELECT * FROM rental WHERE id IN (:rentalIds)")
    List<Rental> loadAllByIds(int[] rentalIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Rental> Rental);

    @Delete
    void delete(Rental rental);

    @Query("DELETE FROM rental")
    void deleteAll();
}
