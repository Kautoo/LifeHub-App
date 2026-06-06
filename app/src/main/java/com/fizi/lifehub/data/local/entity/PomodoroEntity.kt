package com.fizi.lifehub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoro_sessions")
data class PomodoroSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = 0,
    val durationMinutes: Int = 25,
    val breakMinutes: Int = 5,
    val completed: Boolean = false,
    val label: String = ""
)
