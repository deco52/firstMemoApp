package com.example.firstmemoapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.firstmemoapp.service.dao.MemoDao
import com.example.firstmemoapp.service.database.MemoRoomDatabase
import com.example.firstmemoapp.service.database.WordRoomDatabase
import com.example.firstmemoapp.service.model.Memo
import com.example.firstmemoapp.service.model.Word
import com.example.firstmemoapp.service.repository.MemoRepository
import com.example.firstmemoapp.service.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MemoRepository
    lateinit var memoDao: MemoDao

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allMemos: LiveData<List<Memo>>

    init {
//        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
//        repository =
//            WordRepository(wordsDao)
//        allWords = repository.allWords

        memoDao = MemoRoomDatabase.getDatabase(application, viewModelScope).memoDao()
        repository = MemoRepository(memoDao)
        allMemos = repository.allMemos
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun showAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.showAll()
    }

    fun insert(memo: Memo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(memo)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun deleteMemo(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMemo(id)
    }

}