package com.fizi.lifehub.domain.repository

import com.fizi.lifehub.data.local.entity.JournalEntryEntity
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun getAll(): Flow<List<JournalEntryEntity>>
    fun getByDate(date: String): Flow<JournalEntryEntity?>
    fun search(query: String): Flow<List<JournalEntryEntity>>
    fun getCount(): Flow<Int>
    fun getCountByMood(mood: String): Flow<Int>
    suspend fun insert(entry: JournalEntryEntity): Long
    suspend fun update(entry: JournalEntryEntity)
    suspend fun delete(entry: JournalEntryEntity)
}
