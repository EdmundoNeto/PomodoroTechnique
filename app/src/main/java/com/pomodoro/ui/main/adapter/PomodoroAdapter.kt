package com.pomodoro.ui.main.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pomodoro.R
import com.pomodoro.model.Pomodoro
import com.pomodoro.utils.finishedPomodoroDate
import com.pomodoro.utils.setStateText
import kotlinx.android.synthetic.main.item_history_pomodoro.view.*
import java.text.SimpleDateFormat
import java.util.*

class PomodoroAdapter(private val days: LinkedHashMap<Int, Date>, private val pomodoros: LinkedHashMap<Int, Pomodoro>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val POMODORO_VIEW_TYPE = 1
    private val DATE_VIEW_TYPE = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            DATE_VIEW_TYPE -> DateViewHolder(inflater.inflate(R.layout.date_history_pomodoro, parent, false))
            else -> PomodoroViewHolder(inflater.inflate(R.layout.item_history_pomodoro, parent, false))
        }
    }

    override fun getItemCount(): Int = days.size + pomodoros.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DateViewHolder -> days[position]?.let { holder.bind(it) }
            is PomodoroViewHolder -> pomodoros[position]?.let { holder.bind(it) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            days.containsKey(position) -> DATE_VIEW_TYPE
            pomodoros.containsKey(position) -> POMODORO_VIEW_TYPE
            else -> -1
        }
    }

    inner class PomodoroViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(pomodoro: Pomodoro) = with(itemView) {

            pomodoro.totalTime?.let { tvTotalTime.text = pomodoro.totalTime }
            pomodoro.state?.let { tvStatePomodoro.setStateText(it) }
            pomodoro.dateTime?.let { tvDateFinishedPomodoro.text =  finishedPomodoroDate(it) }
        }
    }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(pomodoroDate: Date) = with(itemView) {

            tvDateFinishedPomodoro.text = DateUtils.getRelativeTimeSpanString(pomodoroDate.time, Date().time, DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE)
        }

    }

}