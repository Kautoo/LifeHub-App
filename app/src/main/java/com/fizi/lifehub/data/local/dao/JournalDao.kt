package com.fizi.lifehub.data.local.dao

import androidx.room.*
import com.fizi.lifehub.data.local.entity.JournalEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY date DESC, createdAt DESC")
    fun getAll(): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM journal_entries WHERE date = :date LIMIT 1")
    fun getByDate(date: String): Flow<JournalEntryEntity?>

    @Query("SELECT * FROM journal_entries WHERE content LIKE '%' || :query || '%' ORDER BY date DESC")
    fun search(query: String): Flow<List<JournalEntryEntity>>

    @Query("SELECT COUNT(*) FROM journal_entries")
    fun getCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM journal_entries WHERE mood = :mood")
    fun getCountByMood(mood: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntryEntity): Long

    @Update
    suspend fun update(entry: JournalEntryEntity)

    @Delete
    suspend fun delete(entry: JournalEntryEntity)
}
