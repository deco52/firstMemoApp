package com.example.firstmemoapp.service.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.firstmemoapp.service.model.Word

@Dao
interface WordDao {

    // 昇順にソートして取得
    // LiveData記述はリアルタイムな内容を反映させるための物
    @Query("SELECT * from word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): LiveData<List<Word>>

    // 更新時の競合を排除する設定
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()

    @Query("DELETE FROM word_table where id = :id")
    suspend fun deleteWord(id: Int)
}