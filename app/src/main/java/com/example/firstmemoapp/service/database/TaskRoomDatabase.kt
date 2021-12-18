package com.example.firstmemoapp.service.database

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.firstmemoapp.service.TimestampConverter
import com.example.firstmemoapp.service.dao.TaskDao
import com.example.firstmemoapp.service.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDateTime

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Task::class), version = 3, exportSchema = false)
@TypeConverters(TimestampConverter::class)
abstract class TaskRoomDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    // アプリ起動時処理
    private class TaskDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)

            // タスクDBのレコードが0件ならばサンプルタスクを設定する（5分後）
            INSTANCE?.let { database ->
                scope.launch {
                    Log.d("kinoshita", "insert sample: ")
                    var taskDao = database.taskDao()
                    var now = Timestamp(System.currentTimeMillis())
                    var sampleTask = Task(0,"てすとの","サンプルだお！",
                        0, 0, 2, 0,
                        now,now,now)
                    taskDao.insert(sampleTask)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TaskRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): TaskRoomDatabase {
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskRoomDatabase::class.java,
                        "task_database"
                    )
                        .addCallback(
                            TaskDatabaseCallback(
                                scope
                            )
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
        }
    }
}