package com.fizi.lifehub.data.local.dao

import androidx.room.*
import com.fizi.lifehub.data.local.entity.WaterLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Query("SELECT * FROM water_logs ORDER BY date DESC")
    fun getAll(): Flow<List<WaterLogEntity>>

    @Query("SELECT * FROM water_logs WHERE date = :date LIMIT 1")
    fun getByDate(date: String): Flow<WaterLogEntity?>

    @Query("SELECT * FROM water_logs WHERE date >= :fromDate ORDER BY date ASC")
    fun getFromDate(fromDate: String): Flow<List<WaterLogEntity>>

    @Query("SELECT COUNT(*) FROM water_logs WHERE glasses >= target AND date >= :fromDate")
    fun getDaysGoalMet(fromDate: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: WaterLogEntity): Long

    @Update
    suspend fun update(log: WaterLogEntity)

    @Delete
    suspend fun delete(log: WaterLogEntity)

    @Query("DELETE FROM water_logs WHERE date = :date")
    suspend fun deleteByDate(date: String)
}
