package com.pomodoro.ui.main.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pomodoro.R
import com.pomodoro.model.Pomodoro
import com.pomodoro.ui.main.adapter.PomodoroAdapter
import com.pomodoro.utils.setSortedByDate
import com.pomodoro.utils.setVisibility
import com.pomodoro.utils.stringToDate
import com.pomodoro.viewModel.PomodoroViewModel
import kotlinx.android.synthetic.main.fragment_history_pomodoro.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HistoryPomodoroFragment : Fragment() {

    private lateinit var pomodoroViewModel: PomodoroViewModel
    private lateinit var pomodoroAdapter: PomodoroAdapter

    private val days = LinkedHashMap<Int, Date>()
    private val pomodoros = LinkedHashMap<Int, Pomodoro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_pomodoro, container, false)
    }

    private fun observeViewModel() {
        pomodoroViewModel = ViewModelProviders.of(this).get(PomodoroViewModel::class.java)

        pomodoroViewModel.allPomodoro.observe(this, Observer { pomodoroList ->
            setupViewsVisbility(pomodoroList.isNotEmpty())

            if (pomodoroList.isNotEmpty()) setupHistoryList(pomodoroList)
        })
    }

    private fun setupViewsVisbility(listIsNotEmpty: Boolean) {
        rvHistoryPomodoro.setVisibility(listIsNotEmpty)
        tvEmmptyState.setVisibility(!listIsNotEmpty)
    }

    private fun setupHistoryList(pomodoro: List<Pomodoro>) {
        val groupedPomodoro = pomodoro.groupBy { stringToDate(it.dateTime!!) }
        var index = 0

        if (groupedPomodoro.isNotEmpty()) {
            for((key, value) in groupedPomodoro.setSortedByDate()) {

                val sortedValue = value.sortedByDescending { it.uid }

                days[index] = key
                index++

                sortedValue.forEachIndexed { _, v ->
                    pomodoros[index] = v
                    index++
                }
            }
        }

        setupRecycler()
    }

    private fun setupRecycler(){
        val mLayoutManager = LinearLayoutManager(activity)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        pomodoroAdapter = PomodoroAdapter(days, pomodoros)

        rvHistoryPomodoro.apply {
            adapter = pomodoroAdapter
            layoutManager = mLayoutManager
            setHasFixedSize(false)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = HistoryPomodoroFragment()
    }
}