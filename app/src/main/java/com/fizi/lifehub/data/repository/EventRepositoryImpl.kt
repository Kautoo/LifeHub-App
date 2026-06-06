package com.fizi.lifehub.data.repository

import com.fizi.lifehub.data.local.dao.EventDao
import com.fizi.lifehub.data.local.entity.EventEntity
import com.fizi.lifehub.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val dao: EventDao
) : EventRepository {
    override fun getAll() = dao.getAll()
    override fun getByDate(date: String) = dao.getByDate(date)
    override fun getUpcoming(fromDate: String, limit: Int) = dao.getUpcoming(fromDate, limit)
    override fun getInRange(fromDate: String, toDate: String) = dao.getInRange(fromDate, toDate)
    override suspend fun insert(event: EventEntity) = dao.insert(event)
    override suspend fun update(event: EventEntity) = dao.update(event)
    override suspend fun delete(event: EventEntity) = dao.delete(event)
}
