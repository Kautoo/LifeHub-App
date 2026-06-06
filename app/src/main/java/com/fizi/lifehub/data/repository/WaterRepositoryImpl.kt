package com.fizi.lifehub.data.repository

import com.fizi.lifehub.data.local.dao.WaterDao
import com.fizi.lifehub.data.local.entity.WaterLogEntity
import com.fizi.lifehub.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WaterRepositoryImpl @Inject constructor(
    private val dao: WaterDao
) : WaterRepository {
    override fun getAll() = dao.getAll()
    override fun getByDate(date: String) = dao.getByDate(date)
    override fun getFromDate(fromDate: String) = dao.getFromDate(fromDate)
    override fun getDaysGoalMet(fromDate: String) = dao.getDaysGoalMet(fromDate)
    override suspend fun insert(log: WaterLogEntity) = dao.insert(log)
    override suspend fun update(log: WaterLogEntity) = dao.update(log)
    override suspend fun delete(log: WaterLogEntity) = dao.delete(log)
}
