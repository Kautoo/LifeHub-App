package com.fizi.lifehub.data.repository

import com.fizi.lifehub.data.local.dao.PomodoroDao
import com.fizi.lifehub.data.local.entity.PomodoroSessionEntity
import com.fizi.lifehub.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PomodoroRepositoryImpl @Inject constructor(
    private val dao: PomodoroDao
) : PomodoroRepository {
    override fun getAll() = dao.getAll()
    override fun getCompleted() = dao.getCompleted()
    override fun getCountForDate(date: String) = dao.getCountForDate(date)
    override fun getTotalMinutesForDate(date: String) = dao.getTotalMinutesForDate(date)
    override fun getTotalMinutesForWeek(weekStart: Long) = dao.getTotalMinutesForWeek(weekStart)
    override suspend fun insert(session: PomodoroSessionEntity) = dao.insert(session)
    override suspend fun update(session: PomodoroSessionEntity) = dao.update(session)
    override suspend fun delete(session: PomodoroSessionEntity) = dao.delete(session)
}
