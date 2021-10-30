package com.example.firstmemoapp.service.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.firstmemoapp.service.model.Memo

@Dao
interface MemoDao {

    @Query("SELECT * from memo_table ORDER BY id ASC")
    fun getAllMemos(): LiveData<List<Memo>>

    // 更新時の競合を排除する設定
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(memo: Memo)

    @Query("DELETE FROM memo_table")
    suspend fun deleteAll()

    @Query("DELETE FROM memo_table where id = :memoId")
    suspend fun deleteMemo(memoId: Int)
}