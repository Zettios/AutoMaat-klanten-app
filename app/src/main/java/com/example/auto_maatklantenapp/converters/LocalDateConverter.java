package com.example.auto_maatklantenapp.converters;

import android.os.Build;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class LocalDateConverter {
    @TypeConverter
    public LocalDate fromTimestamp(String value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return value != null ? LocalDate.parse(value) : null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public String dateToTimestamp(LocalDate date) {
        return date != null ? date.toString() : null;
    }
}
