package com.example.firstmemoapp.service.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.firstmemoapp.service.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * from task_table ORDER BY task_id ASC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * from task_table where task_id = :task_id")
    suspend fun select(task_id: Int): Task

    // 更新時の競合を排除する設定
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    // デバッグ用
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIdDebug(task: Task): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("DELETE FROM task_table where task_id = :task_id")
    suspend fun delete(task_id: Int)
}