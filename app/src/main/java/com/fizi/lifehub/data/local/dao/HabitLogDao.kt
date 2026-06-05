package com.fizi.lifehub.data.local.dao

import androidx.room.*
import com.fizi.lifehub.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitLogDao {

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY date DESC")
    fun getLogsForHabit(habitId: Long): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getLogForDate(habitId: Long, date: String): HabitLogEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM habit_logs WHERE habitId = :habitId AND date = :date AND isCompleted = 1)")
    fun isCompletedOnDate(habitId: Long, date: String): Flow<Boolean>

    @Query("SELECT COUNT(*) FROM habit_logs WHERE habitId = :habitId AND isCompleted = 1")
    fun getCompletionCount(habitId: Long): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM habit_logs 
        WHERE habitId = :habitId AND isCompleted = 1 AND date <= :toDate
        ORDER BY date DESC
    """)
    suspend fun getStreakUpTo(habitId: Long, toDate: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLogEntity): Long

    @Delete
    suspend fun deleteLog(log: HabitLogEntity)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId")
    suspend fun deleteAllLogsForHabit(habitId: Long)
}
