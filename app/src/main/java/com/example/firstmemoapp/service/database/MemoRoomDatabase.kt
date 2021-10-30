package com.example.firstmemoapp.service.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.firstmemoapp.service.dao.MemoDao
import com.example.firstmemoapp.service.model.Memo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Memo::class), version =2, exportSchema = false)
abstract class MemoRoomDatabase : RoomDatabase() {

    abstract fun memoDao(): MemoDao

    private class MemoDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    Log.d("kinoshita", "insert sample: ")
                    var memoDao = database.memoDao()

                    var sampleMemo = Memo(0,"入っていて","くれお！","202020202020")
                    memoDao.insert(sampleMemo)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MemoRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): MemoRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MemoRoomDatabase::class.java,
                        "memo_database"
                    )
                        .addCallback(
                            MemoDatabaseCallback(
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