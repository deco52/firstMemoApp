package com.example.firstmemoapp.service.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.firstmemoapp.service.dao.TaskDao
import com.example.firstmemoapp.service.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    var allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun select(task_id: Int): Task {
        return taskDao.select(task_id)
    }

    fun showAll() {
//        allTasks = taskDao.getAllTasks()
//        for(task in allTasks.value!!) {
//            Log.i("kinoshita", "showAll: " + task.task_id + " " + task.register_time)
//
//        }
    }

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun insertIdDebug(task: Task) :Long{
        return taskDao.insertIdDebug(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }

    suspend fun delete(task_id: Int) {
        taskDao.delete(task_id)
    }
}