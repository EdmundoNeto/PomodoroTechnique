package com.pomodoro.utils

import android.os.Build
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pomodoro.R
import com.pomodoro.model.Pomodoro
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val POMODORO_START_TIME = "25:00"
const val POMODORO_FINISHED_TIME = "00:00"
const val MMDDYYYY_WITH_TIME_PATTERN = "MM/dd/yyyy hh:mm:ss"
const val MMDDYYYY_WITHOUT_TIME_PATTERN = "MM/dd/yyyy"

fun FloatingActionButton.setState(isPlaying: Boolean) {
    this.setImageResource( if(isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play)
}

fun CountDownTimer.setState(start: Boolean) {
    if(start) this.start() else this.cancel()
}

fun TextView.setStateColor(isPlaying: Boolean) {
    val stateColor = if(isPlaying) R.color.colorRunningState else R.color.colorWaitingState

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) this.setTextColor(resources.getColor(stateColor, null))
    else this.setTextColor(resources.getColor(stateColor))
}

fun TextView.setStateText(value: Int){
    when(value) {
        PomodoroState.FINISHED.value -> setText(R.string.finished_state)
        PomodoroState.STOPPED.value -> setText(R.string.stopped_state)
        else -> text = ""
    }
}

fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat(MMDDYYYY_WITH_TIME_PATTERN)
    return sdf.format(Date())
}

fun formattedMinutes(mils: Long) : String {
    val minutes = TimeUnit.SECONDS.toMinutes(mils)
    return if(minutes < 10.0) "0$minutes" else minutes.toString()
}

fun formattedSeconds(mils: Long) : String {
    val seconds = TimeUnit.SECONDS.toSeconds(mils) - TimeUnit.SECONDS.toMinutes(mils) * 60
    return if(seconds < 10.0) "0$seconds" else seconds.toString()
}

fun totalTimeStoppedPomodoro(endTime: String): String {
    when (endTime) {
        POMODORO_FINISHED_TIME -> return POMODORO_START_TIME
        else -> {
            val parseFormat = SimpleDateFormat("mm:ss")
            val startTime = parseFormat.parse(POMODORO_START_TIME)
            val endedTime = parseFormat.parse(endTime)

            val mills = startTime.time - endedTime.time

            val millsToMinute = TimeUnit.MILLISECONDS.toMinutes(mills)
            val millsToSeconds = TimeUnit.MILLISECONDS.toSeconds(mills)

            val minutes = if(millsToMinute < 10) "0$millsToMinute" else "$millsToMinute"

            val seconds = if (millsToSeconds < 10) "0$millsToSeconds" else "$millsToSeconds"

            return "$minutes:$seconds"
        }
    }

}

fun stringToDate(dateString: String, hasHoursAndMinutes: Boolean = false): Date {
    val sdf = SimpleDateFormat(if(hasHoursAndMinutes) MMDDYYYY_WITH_TIME_PATTERN else MMDDYYYY_WITHOUT_TIME_PATTERN )
    return sdf.parse(dateString)
}


fun finishedPomodoroDate(dateString: String): String {
    val sdf = SimpleDateFormat(MMDDYYYY_WITH_TIME_PATTERN)
    val date = sdf.parse(dateString)
    val today = sdf.format(Date())

    val currentTime = sdf.parse(today)
    val startDate = date.time

    return DateUtils.getRelativeTimeSpanString(startDate, currentTime.time, DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString()
}

fun Map<Date, List<Pomodoro>>.setSortedByDate() = this.toSortedMap(compareByDescending { it })
