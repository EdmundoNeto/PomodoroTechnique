package com.pomodoro

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.pomodoro.ui.main.fragment.NewPomodoroFragment
import com.pomodoro.ui.main.adapter.SectionsPagerAdapter
import com.pomodoro.ui.main.fragment.HistoryPomodoroFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.addFragment(NewPomodoroFragment.newInstance())
        sectionsPagerAdapter.addFragment(HistoryPomodoroFragment.newInstance())
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }
}