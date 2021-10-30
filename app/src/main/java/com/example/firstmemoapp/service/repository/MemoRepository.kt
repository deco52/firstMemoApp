package com.example.firstmemoapp.service.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.firstmemoapp.service.dao.MemoDao
import com.example.firstmemoapp.service.model.Memo

class MemoRepository(private val memoDao: MemoDao) {

    val allMemos: LiveData<List<Memo>> = memoDao.getAllMemos()

    fun showAll() {
        if ( allMemos.value != null) {
            Log.d("kinoshita", allMemos.value!!.size.toString())
        }
    }

    suspend fun insert(memo: Memo) {
        Log.d("kinoshita", "insert click: ")
        memoDao.insert(memo)
    }

    suspend fun deleteAll() {
        memoDao.deleteAll()
    }

    suspend fun deleteMemo(memoId: Int) {
        memoDao.deleteMemo(memoId)
    }
}