package com.pomodoro.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.pomodoro.model.Pomodoro

class PomodoroRepository(private val pomodoroDao: PomodoroDao) {

    val allPomodoro: LiveData<List<Pomodoro>> = pomodoroDao.getAll()

    @WorkerThread
    suspend fun insert(pomodoro: Pomodoro) {
        pomodoroDao.insertPodomodo(pomodoro)
    }
}