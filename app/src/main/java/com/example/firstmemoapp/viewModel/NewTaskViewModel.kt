package com.example.firstmemoapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firstmemoapp.google.Event
import com.example.firstmemoapp.service.dao.TaskDao
import com.example.firstmemoapp.service.database.TaskRoomDatabase
import com.example.firstmemoapp.service.model.Task
import com.example.firstmemoapp.service.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp

class NewTaskViewModel(application: Application) : AndroidViewModel(application) {
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


    //    TODO:20211123
//    Taskでinsert,select,delete,updateを実装　動作確認済み
//
//    次回TODO：
//    　- TaskでRoom一連クラス作成 > mockUIのmock(ガワ)だけ作成済み　中身はまだ
//        TODO: 20211126 ここまで↑
//    　- TaskListにボタンの口を作成 > mockクラスでTaskのDB操作の基礎確認 最新をselect,　同じViewで　ボタンを切り替えて確認
//    　　　　　　削除したらTaskListに戻る
//    　- Task用のView,ViewModelを作成
//    　- (タイマーや時間による見た目の変化の作成　これは12月以降　もしくは↑と平行)
//    fun onClickUpdateButton() {
//        Log.i("kinoshita", "Update:${task.task_id}")
//
//        var task = Task(task.task_id, title.value ?: "", text.value ?: "", "YYYYMMDD")
//
//        viewModelScope.launch(Dispatchers.Main) {
//            repository.update(task)
//        }
//    }

    fun onClickInsertButton() {
        var now = Timestamp(System.currentTimeMillis())
        var insertTask = Task(
            0, title.value ?: "", text.value ?: "",
            task.status, task.last_status, task.type, task.notification,
            task.period_time, now, now
        )

        viewModelScope.launch(Dispatchers.Main) {
            repository.insert(insertTask)
        }
        // 成功：リストへ遷移
        onTransit.value = Event("onTransit")
    }

//    fun onClickDeleteButton() {
//        Log.i("kinoshita", "Delete:${task.task_id}")
//
//        // 削除確認ダイアログ
//
//        // 削除処理
//        viewModelScope.launch(Dispatchers.Main) {
//            repository.delete(task.task_id)
//        }
//        // 成功：リストへ遷移
//        onTransit.value = Event("onTransit")
//    }
//
    fun setTask(task: Task) {
        this.task = task
        Log.i("kinoshita", task.task_id.toString())
    }

}