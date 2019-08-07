package com.pomodoro.viewModel

import android.app.Application
import android.text.BoringLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pomodoro.db.AppDatabase
import com.pomodoro.model.Pomodoro
import com.pomodoro.db.PomodoroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PomodoroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PomodoroRepository
    val allPomodoro: LiveData<List<Pomodoro>>
    val isPomodoroPlaying: MutableLiveData<Boolean> = MutableLiveData()

    init {
        val pomodoroDao = AppDatabase.getDatabase(application, viewModelScope).pomodoroDao()
        repository = PomodoroRepository(pomodoroDao)
        allPomodoro = repository.allPomodoro
    }

    fun insert(pomodoro: Pomodoro) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(pomodoro)
    }

    fun setIsPlaying(isPlaying: Boolean) {
        isPomodoroPlaying.postValue(!isPlaying)
    }
}