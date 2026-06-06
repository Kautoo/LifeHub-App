package com.fizi.lifehub.domain.repository

import com.fizi.lifehub.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getAll(): Flow<List<EventEntity>>
    fun getByDate(date: String): Flow<List<EventEntity>>
    fun getUpcoming(fromDate: String, limit: Int = 10): Flow<List<EventEntity>>
    fun getInRange(fromDate: String, toDate: String): Flow<List<EventEntity>>
    suspend fun insert(event: EventEntity): Long
    suspend fun update(event: EventEntity)
    suspend fun delete(event: EventEntity)
}
