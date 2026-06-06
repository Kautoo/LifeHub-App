package com.fizi.lifehub.data.repository

import com.fizi.lifehub.data.local.dao.JournalDao
import com.fizi.lifehub.data.local.entity.JournalEntryEntity
import com.fizi.lifehub.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JournalRepositoryImpl @Inject constructor(
    private val dao: JournalDao
) : JournalRepository {
    override fun getAll() = dao.getAll()
    override fun getByDate(date: String) = dao.getByDate(date)
    override fun search(query: String) = dao.search(query)
    override fun getCount() = dao.getCount()
    override fun getCountByMood(mood: String) = dao.getCountByMood(mood)
    override suspend fun insert(entry: JournalEntryEntity) = dao.insert(entry)
    override suspend fun update(entry: JournalEntryEntity) = dao.update(entry)
    override suspend fun delete(entry: JournalEntryEntity) = dao.delete(entry)
}
