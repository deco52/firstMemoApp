package com.example.firstmemoapp.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo_table")
class Memo(
    @PrimaryKey(autoGenerate = true) val memoId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "date") val date: String
)