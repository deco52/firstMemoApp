package com.example.firstmemoapp.viewModel

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firstmemoapp.google.Event
import com.example.firstmemoapp.service.dao.TaskDao
import com.example.firstmemoapp.service.database.TaskRoomDatabase
import com.example.firstmemoapp.service.model.Task
import com.example.firstmemoapp.service.model.TaskStatus
import com.example.firstmemoapp.service.repository.TaskRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp

class EditTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    var taskDao: TaskDao = TaskRoomDatabase.getDatabase(application, viewModelScope).taskDao()
    private lateinit var task: Task

    // 画面遷移用のイベント定義
    val onTransit = MutableLiveData<Event<String>>()

    init {
        repository = TaskRepository(taskDao)
    }

    // EditTextの値
    var title: MutableLiveData<String> = MutableLiveData<String>()
    var text: MutableLiveData<String> = MutableLiveData<String>()

    // LiveDataの残骸
    private val _titleHint =
        MutableLiveData<String>().also { mutableLiveData ->
            mutableLiveData.value = "Title…"
        }
    val titleHint: LiveData<String>
        get() = _titleHint

    private val _textHint =
        MutableLiveData<String>().also { mutableLiveData ->
            mutableLiveData.value = "Text…"
        }
    val textHint: LiveData<String>
        get() = _textHint

    fun updateTitle(isBlank: Boolean) {
        if (!isBlank) {
            _titleHint.value
        } else {
            _titleHint.value = "Title…"
        }
    }

    fun updateText(isBlank: Boolean) {
        if (!isBlank) {
            _titleHint.value = ""
        } else {
            _titleHint.value = "Text…"
        }
    }

    fun onClickUpdateButton() {
        Log.i("kinoshita", "Update:${task.task_id}")
        var now = Timestamp(System.currentTimeMillis())

        var testTask = Task(
            task.task_id, "aaaa", "bbbbb", 0,
            0, 0, 0, now, now, now
        )

        var updateTask = Task(
            task.task_id,
            title.value.toString(),
            text.value.toString(),
            task.status,
            task.status,
            task.type,
            task.notification,
            task.period_time,
            task.register_time,
            now
        )
        viewModelScope.launch(Dispatchers.Main) {
//            repository.update(updateTask)
            repository.update(updateTask)
        }
        // 成功：リストへ遷移
        onTransit.value = Event("onTransit")
    }

    fun onClickDeleteButton() {
        Log.i("kinoshita", "Delete:${task.task_id}")

        // 削除処理
        viewModelScope.launch(Dispatchers.Main) {
            repository.delete(task.task_id)
        }

//        // 削除確認ダイアログ
//            AlertDialog.Builder(application)
//                .setTitle("確認")
//                .setMessage("「" + task.title + "」削除をしますか？")
//                .setPositiveButton("はい") { dialog, which ->
//                    var tmpTask = task
//
//                    // 削除処理
//                    viewModelScope.launch(Dispatchers.Main) {
//                        repository.delete(task.task_id)
//                    }
////                    Snackbar
////                        .make(this, "「" + task.title + "」を削除しました", Snackbar.LENGTH_SHORT)
////                        .setAction("元に戻す") {
////
////                        }
////                        .show()
//                }
//                .setNegativeButton("キャンセル") { dialog, which ->
//
//                }
//                .show()
        // 成功：リストへ遷移
        onTransit.value = Event("onTransit")
    }

    fun setTask(task: Task) {
        this.task = task
        Log.i("kinoshita", task.task_id.toString())
    }

}