package com.example.firstmemoapp.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp

@Entity(tableName = "task_table")
class Task(
    @ColumnInfo(name = "task_id") @PrimaryKey(autoGenerate = true) val task_id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "details") val text: String?,
    /** タスクの状況
    　- 0：未達成(期限前)     白
    　- 1：未達成（期限切れ）    赤
    　- 2：達成済み    緑
    　- 4：保留中    黄色
     */
    @ColumnInfo(name = "status") val status: Int,
    @ColumnInfo(name = "last_status") val last_status: Int,
    // タスク優先度
    @ColumnInfo(name = "type", defaultValue = "2") val type: Int,
    // リマインド通知を行うかどうか
    @ColumnInfo(name = "notification", defaultValue = "0") val notification: Int,
    // タスク完了の期限　これを過ぎていたらリマインドを発動する
    @ColumnInfo(name = "period_time") val period_time: Timestamp,
    @ColumnInfo(name = "register_time") val register_time: Timestamp,
    @ColumnInfo(name = "update_time") val update_time: Timestamp
) : Serializable