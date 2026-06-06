package com.fizi.lifehub.domain.repository

import com.fizi.lifehub.data.local.entity.PomodoroSessionEntity
import kotlinx.coroutines.flow.Flow

interface PomodoroRepository {
    fun getAll(): Flow<List<PomodoroSessionEntity>>
    fun getCompleted(): Flow<List<PomodoroSessionEntity>>
    fun getCountForDate(date: String): Flow<Int>
    fun getTotalMinutesForDate(date: String): Flow<Int?>
    fun getTotalMinutesForWeek(weekStart: Long): Flow<Int?>
    suspend fun insert(session: PomodoroSessionEntity): Long
    suspend fun update(session: PomodoroSessionEntity)
    suspend fun delete(session: PomodoroSessionEntity)
}
