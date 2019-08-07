package com.pomodoro.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pomodoro.model.Pomodoro
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(Pomodoro::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pomodoroDao(): PomodoroDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "pomodoro"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}