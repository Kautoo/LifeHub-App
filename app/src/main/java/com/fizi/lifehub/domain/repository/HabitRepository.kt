package com.fizi.lifehub.domain.repository

import com.fizi.lifehub.data.local.entity.HabitEntity
import com.fizi.lifehub.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabits(): Flow<List<HabitEntity>>
    suspend fun getHabitById(id: Long): HabitEntity?
    suspend fun insertHabit(habit: HabitEntity): Long
    suspend fun updateHabit(habit: HabitEntity)
    suspend fun deleteHabit(habit: HabitEntity)
    fun getLogsForHabit(habitId: Long): Flow<List<HabitLogEntity>>
    suspend fun getLogForDate(habitId: Long, date: String): HabitLogEntity?
    fun isCompletedOnDate(habitId: Long, date: String): Flow<Boolean>
    fun getCompletionCount(habitId: Long): Flow<Int>
    suspend fun toggleHabitForDate(habitId: Long, date: String)
    suspend fun deleteAllLogsForHabit(habitId: Long)
}
