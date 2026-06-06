package com.fizi.lifehub.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fizi.lifehub.data.local.entity.JournalEntryEntity
import com.fizi.lifehub.data.local.entity.Mood
import com.fizi.lifehub.domain.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val repository: JournalRepository
) : ViewModel() {

    val entries: StateFlow<List<JournalEntryEntity>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val entryCount: StateFlow<Int> = repository.getCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun addEntry(content: String, mood: Mood) {
        viewModelScope.launch {
            repository.insert(
                JournalEntryEntity(
                    date = today,
                    content = content,
                    mood = mood
                )
            )
        }
    }

    fun deleteEntry(entry: JournalEntryEntity) {
        viewModelScope.launch { repository.delete(entry) }
    }
}
