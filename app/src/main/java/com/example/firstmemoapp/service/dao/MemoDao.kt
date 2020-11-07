package com.example.firstmemoapp.service.dao

import androidx.room.Dao

@Dao
interface MemoDao {

    // 昇順にソートして取得
    // LiveData記述はリアルタイムな内容を反映させるための物
//    @Query("SELECT * from memo_table ORDER BY title ASC")
//    fun getAlphabetizedWords(): LiveData<List<Memo>>
//
//    // 更新時の競合を排除する設定
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(memo: Memo)
//
//    @Query("DELETE FROM memo_table")
//    suspend fun deleteAll()
//
//    @Query("DELETE FROM memo_table where id = :id")
//    suspend fun deleteMemo(id: Int)
}