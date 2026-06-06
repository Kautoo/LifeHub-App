package com.fizi.lifehub.data.local.dao

import androidx.room.*
import com.fizi.lifehub.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY date ASC, time ASC")
    fun getAll(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE date = :date ORDER BY time ASC")
    fun getByDate(date: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE date >= :fromDate ORDER BY date ASC, time ASC LIMIT :limit")
    fun getUpcoming(fromDate: String, limit: Int = 10): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE date >= :fromDate AND date <= :toDate ORDER BY date ASC, time ASC")
    fun getInRange(fromDate: String, toDate: String): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity): Long

    @Update
    suspend fun update(event: EventEntity)

    @Delete
    suspend fun delete(event: EventEntity)
}
