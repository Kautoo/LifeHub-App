package com.fizi.lifehub.data.repository

import com.fizi.lifehub.data.local.dao.NoteDao
import com.fizi.lifehub.data.local.entity.NoteEntity
import com.fizi.lifehub.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<NoteEntity>> = dao.getAllNotes()

    override suspend fun getNoteById(id: Long): NoteEntity? = dao.getNoteById(id)

    override fun searchNotes(query: String): Flow<List<NoteEntity>> = dao.searchNotes(query)

    override suspend fun insertNote(note: NoteEntity): Long = dao.insertNote(note)

    override suspend fun updateNote(note: NoteEntity) = dao.updateNote(note)

    override suspend fun deleteNote(note: NoteEntity) = dao.deleteNote(note)
}
