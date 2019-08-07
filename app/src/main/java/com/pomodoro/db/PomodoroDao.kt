package com.pomodoro.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pomodoro.model.Pomodoro

@Dao
interface PomodoroDao {
    @Query("SELECT * FROM pomodoro")
    fun getAll(): LiveData<List<Pomodoro>>

    @Insert
    fun insertPodomodo(vararg pomodoro: Pomodoro)

}