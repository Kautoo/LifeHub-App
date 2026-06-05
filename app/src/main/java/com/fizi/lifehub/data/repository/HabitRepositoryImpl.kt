package com.fizi.lifehub.data.repository

import com.fizi.lifehub.data.local.dao.HabitDao
import com.fizi.lifehub.data.local.dao.HabitLogDao
import com.fizi.lifehub.data.local.entity.HabitEntity
import com.fizi.lifehub.data.local.entity.HabitLogEntity
import com.fizi.lifehub.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val habitLogDao: HabitLogDao
) : HabitRepository {

    override fun getAllHabits(): Flow<List<HabitEntity>> = habitDao.getAllHabits()

    override suspend fun getHabitById(id: Long): HabitEntity? = habitDao.getHabitById(id)

    override suspend fun insertHabit(habit: HabitEntity): Long = habitDao.insertHabit(habit)

    override suspend fun updateHabit(habit: HabitEntity) = habitDao.updateHabit(habit)

    override suspend fun deleteHabit(habit: HabitEntity) {
        habitLogDao.deleteAllLogsForHabit(habit.id)
        habitDao.deleteHabit(habit)
    }

    override fun getLogsForHabit(habitId: Long): Flow<List<HabitLogEntity>> =
        habitLogDao.getLogsForHabit(habitId)

    override suspend fun getLogForDate(habitId: Long, date: String): HabitLogEntity? =
        habitLogDao.getLogForDate(habitId, date)

    override fun isCompletedOnDate(habitId: Long, date: String): Flow<Boolean> =
        habitLogDao.isCompletedOnDate(habitId, date)

    override fun getCompletionCount(habitId: Long): Flow<Int> =
        habitLogDao.getCompletionCount(habitId)

    override suspend fun toggleHabitForDate(habitId: Long, date: String) {
        val existing = habitLogDao.getLogForDate(habitId, date)
        if (existing != null) {
            habitLogDao.deleteLog(existing)
        } else {
            habitLogDao.insertLog(
                HabitLogEntity(
                    habitId = habitId,
                    date = date,
                    isCompleted = true
                )
            )
            // Update streak
            val habit = habitDao.getHabitById(habitId)
            habit?.let {
                val newStreak = it.streak + 1
                val bestStreak = maxOf(it.bestStreak, newStreak)
                habitDao.updateHabit(it.copy(streak = newStreak, bestStreak = bestStreak))
            }
        }
    }

    override suspend fun deleteAllLogsForHabit(habitId: Long) =
        habitLogDao.deleteAllLogsForHabit(habitId)
}
