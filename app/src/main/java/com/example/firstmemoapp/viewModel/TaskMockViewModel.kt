package com.example.firstmemoapp.viewModel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firstmemoapp.google.Event
import com.example.firstmemoapp.service.dao.TaskDao
import com.example.firstmemoapp.service.database.TaskRoomDatabase
import com.example.firstmemoapp.service.model.Memo
import com.example.firstmemoapp.service.model.Task
import com.example.firstmemoapp.service.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDateTime

class TaskMockViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    lateinit var taskDao: TaskDao
    private lateinit var task: Task
    val allTasks: LiveData<List<Task>>

    // 画面遷移用のイベント定義
    val onTransit = MutableLiveData<Event<String>>()

    init {
        taskDao = TaskRoomDatabase.getDatabase(application, viewModelScope).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasks
    }

        // EditTextの値
        var title: MutableLiveData<String> = MutableLiveData<String>()
        var text: MutableLiveData<String> = MutableLiveData<String>()


        fun onClickUpdateButton() {
            Log.i("kinoshita", "Update:${task.task_id}")

            var task = Memo(task.task_id, title.value ?: "", text.value ?: "", "YYYYMMDD")

            viewModelScope.launch(Dispatchers.Main) {
                // repository.update(task)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun onClickInsertButton() {
//            var now = LocalDateTime.now().toString()
//            var sampleTask = Task(0,"てすとの","サンプルだお！",
//                0, 0, 2, 0,
//                Timestamp.valueOf(now), Timestamp.valueOf(now), Timestamp.valueOf(now))

            var now = Timestamp(System.currentTimeMillis())
            var sampleTask = Task(0,"てすとの","サンプルだお！",
                0, 0, 2, 0,
                now,now,now)

            Log.i("kinoshita", "sampleTask:{$sampleTask}")


            Log.i("kinoshita", "Insert:${sampleTask.text}")
            Log.i("kinoshita", "Insert:${sampleTask.register_time}")
            Log.i("kinoshita", "Insert:${sampleTask.register_time.year}")

            viewModelScope.launch(Dispatchers.Main) {
                //repository.insert(sampleTask)

                var logId = repository.insertIdDebug(sampleTask)
                Log.i("kinoshita", "Insert  IDLOG:${logId}")


                Log.i("kinoshita", "Insert:${sampleTask.title}")
                Log.i("kinoshita", "AllTask:${repository.allTasks}")
                Log.i("kinoshita", "AllTask:${repository}")
                Log.i("kinoshita", "ID:${sampleTask.task_id}")

                repository.showAll()

                Log.i("kinoshita", "select:${repository.select(logId.toInt()-10).register_time}")

//                for(task in repository.allTasks.value!!) {
//                    Log.i("kinoshita", "showAll: " + task.task_id + " " + task.register_time)
//
//                }

            }
//            // 成功：リストへ遷移
            //onTransit.value = Event("onTransit")
        }

        fun onClickDeleteButton() {
            Log.i("kinoshita", "Delete:${task.task_id}")

            // 削除確認ダイアログ

            // 削除処理
            viewModelScope.launch(Dispatchers.Main) {
                // repository.deleteMemo(memo.id)
            }
            // 成功：リストへ遷移
            onTransit.value = Event("onTransit")

        }
//    fun setMemo(memo: Memo) {
//        this.memo = memo
//        Log.i("kinoshita", memo.id.toString())
//    }
}