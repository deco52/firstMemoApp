package com.example.firstmemoapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.firstmemoapp.service.dao.TaskDao
import com.example.firstmemoapp.service.database.TaskRoomDatabase
import com.example.firstmemoapp.service.model.Task
import com.example.firstmemoapp.service.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    lateinit var taskDao: TaskDao

    val allTasks: LiveData<List<Task>>

    init {
//        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
//        repository =
//            WordRepository(wordsDao)
//        allWords = repository.allWords

        taskDao = TaskRoomDatabase.getDatabase(application, viewModelScope).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasks
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun showAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.showAll()
    }

    fun insert(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(task)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun deleteTask(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(id)
    }

}