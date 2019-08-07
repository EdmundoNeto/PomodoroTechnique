package com.pomodoro.ui.main.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pomodoro.R
import kotlinx.android.synthetic.main.fragment_new_pomodoro.*
import java.util.concurrent.TimeUnit
import com.pomodoro.model.Pomodoro
import com.pomodoro.utils.*
import com.pomodoro.viewModel.PomodoroViewModel
import android.R.attr.duration
import androidx.lifecycle.Observer
import com.pomodoro.notification.Notification
import java.text.SimpleDateFormat


class NewPomodoroFragment : Fragment() {

    private lateinit var pomodoroViewModel: PomodoroViewModel
    private lateinit var countDownTimer: CountDownTimer
    private var countDownTimerPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_pomodoro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCountDownTime()

        fab.setOnClickListener { pomodoroViewModel.setIsPlaying(countDownTimerPlaying) }
    }

    private fun setupCountDownTime(){
        countDownTimer = object : CountDownTimer(POMODORO_MILISECONDS, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {

                val remaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

                tvTimeRemaining.text = "${formattedMinutes(remaining)}:${formattedSeconds(remaining)}"
            }
            override fun onFinish() {
                pomodoroViewModel.insert(returnPomodoroToDb(tvTimeRemaining.text.toString(),
                        PomodoroState.FINISHED.value))

                pomodoroViewModel.setIsPlaying(countDownTimerPlaying)

                sendUserNotification()
            }
        }
    }

    private fun sendUserNotification() {
        val notification = Notification(context!!)
        notification.notifyUser()
    }

    private fun observeViewModel() {
        pomodoroViewModel = ViewModelProviders.of(this).get(PomodoroViewModel::class.java)

        pomodoroViewModel.isPomodoroPlaying.observe(this, Observer {
            countDownTimerPlaying = it

            shouldSaveStoppedPomodoro(it, tvTimeRemaining.text.toString())

            setupView(it, countDownTimer)
        })
    }

    private fun setupView(isPlaying: Boolean, countDownTimer: CountDownTimer) {
        fab.setState(isPlaying)
        countDownTimer.setState(isPlaying)
        tvTimeRemaining.setStateColor(isPlaying)
        tvTimeRemaining.text = getString(R.string.initial_time_state)
    }


    private fun shouldSaveStoppedPomodoro(isPlaying: Boolean, finalTime: String) {
        if(!isPlaying) pomodoroViewModel.insert(returnPomodoroToDb(finalTime, PomodoroState.STOPPED.value))
    }

    private fun returnPomodoroToDb(finalTime: String, state: Int): Pomodoro {
        val pomodoro = Pomodoro()

        pomodoro.totalTime = totalTimeStoppedPomodoro(finalTime)
        pomodoro.state = state
        pomodoro.dateTime = getCurrentDateTime()

        return pomodoro
    }

    companion object {

        private const val POMODORO_MILISECONDS = 1500000L
        private const val COUNTDOWN_INTERVAL = 1000L

        @JvmStatic
        fun newInstance() = NewPomodoroFragment()
    }
}