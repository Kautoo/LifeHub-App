package com.fizi.lifehub.data.repository

import com.fizi.lifehub.data.local.dao.GoalDao
import com.fizi.lifehub.data.local.entity.GoalEntity
import com.fizi.lifehub.data.local.entity.GoalStatus
import com.fizi.lifehub.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val dao: GoalDao
) : GoalRepository {
    override fun getAll() = dao.getAll()
    override fun getActive() = dao.getActive()
    override fun getCompleted() = dao.getCompleted()
    override fun getCountByStatus(status: GoalStatus) = dao.getCountByStatus(status)
    override suspend fun insert(goal: GoalEntity) = dao.insert(goal)
    override suspend fun update(goal: GoalEntity) = dao.update(goal)
    override suspend fun delete(goal: GoalEntity) = dao.delete(goal)
}
