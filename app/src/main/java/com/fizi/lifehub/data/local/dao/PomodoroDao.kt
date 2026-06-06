package com.fizi.lifehub.data.local.dao

import androidx.room.*
import com.fizi.lifehub.data.local.entity.PomodoroSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroDao {
    @Query("SELECT * FROM pomodoro_sessions ORDER BY startTime DESC")
    fun getAll(): Flow<List<PomodoroSessionEntity>>

    @Query("SELECT * FROM pomodoro_sessions WHERE completed = 1 ORDER BY startTime DESC")
    fun getCompleted(): Flow<List<PomodoroSessionEntity>>

    @Query("SELECT COUNT(*) FROM pomodoro_sessions WHERE completed = 1 AND date(startTime/1000, 'unixepoch') = :date")
    fun getCountForDate(date: String): Flow<Int>

    @Query("SELECT SUM(durationMinutes) FROM pomodoro_sessions WHERE completed = 1 AND date(startTime/1000, 'unixepoch') = :date")
    fun getTotalMinutesForDate(date: String): Flow<Int?>

    @Query("SELECT SUM(durationMinutes) FROM pomodoro_sessions WHERE completed = 1 AND startTime >= :weekStart")
    fun getTotalMinutesForWeek(weekStart: Long): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: PomodoroSessionEntity): Long

    @Update
    suspend fun update(session: PomodoroSessionEntity)

    @Delete
    suspend fun delete(session: PomodoroSessionEntity)
}
