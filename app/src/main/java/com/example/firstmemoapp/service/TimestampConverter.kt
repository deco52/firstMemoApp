package com.example.firstmemoapp.service

import androidx.room.TypeConverter
import java.sql.Date
import java.sql.Timestamp

class TimestampConverter {
    // Long型をDate型に変換
    @TypeConverter
    fun fromDate(value: Long?): Timestamp? {
        return if (value == null) null else Timestamp(value)
    }

    // Date型をLong型に変換
    @TypeConverter
    fun dateToLong(timestamp: Timestamp?): Long? {
        return timestamp?.let { it.time }
    }
}