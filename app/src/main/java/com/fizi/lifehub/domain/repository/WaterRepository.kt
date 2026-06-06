package com.fizi.lifehub.domain.repository

import com.fizi.lifehub.data.local.entity.WaterLogEntity
import kotlinx.coroutines.flow.Flow

interface WaterRepository {
    fun getAll(): Flow<List<WaterLogEntity>>
    fun getByDate(date: String): Flow<WaterLogEntity?>
    fun getFromDate(fromDate: String): Flow<List<WaterLogEntity>>
    fun getDaysGoalMet(fromDate: String): Flow<Int>
    suspend fun insert(log: WaterLogEntity): Long
    suspend fun update(log: WaterLogEntity)
    suspend fun delete(log: WaterLogEntity)
}
