package com.fizi.lifehub.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fizi.lifehub.data.local.entity.NoteEntity
import com.fizi.lifehub.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getAllNotes: GetAllNotesUseCase,
    private val insertNote: InsertNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase
) : ViewModel() {

    val notes = getAllNotes().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredNotes = combine(notes, _searchQuery) { notesList, query ->
        if (query.isBlank()) notesList
        else notesList.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    fun addNote(title: String, content: String, colorIndex: Int = 0) {
        viewModelScope.launch {
            insertNote(
                NoteEntity(
                    title = title,
                    content = content,
                    color = colorIndex
                )
            )
        }
    }

    fun update(note: NoteEntity) {
        viewModelScope.launch {
            updateNote(note.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    fun delete(note: NoteEntity) {
        viewModelScope.launch { deleteNote(note) }
    }
}
