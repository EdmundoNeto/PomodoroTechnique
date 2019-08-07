package com.pomodoro.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class Pomodoro(

        @PrimaryKey(autoGenerate = true) var uid: Int = 0,
        @ColumnInfo(name = "total_time") var totalTime: String? = "",
        @ColumnInfo(name = "date_time") var dateTime: String? = "",
        @ColumnInfo(name= "state") var state: Int? = -1
)