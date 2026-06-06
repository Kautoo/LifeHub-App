package com.fizi.lifehub.domain.repository

import com.fizi.lifehub.data.local.entity.GoalEntity
import com.fizi.lifehub.data.local.entity.GoalStatus
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getAll(): Flow<List<GoalEntity>>
    fun getActive(): Flow<List<GoalEntity>>
    fun getCompleted(): Flow<List<GoalEntity>>
    fun getCountByStatus(status: GoalStatus): Flow<Int>
    suspend fun insert(goal: GoalEntity): Long
    suspend fun update(goal: GoalEntity)
    suspend fun delete(goal: GoalEntity)
}
