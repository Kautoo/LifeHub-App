package com.fizi.lifehub.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fizi.lifehub.data.local.entity.EventEntity
import com.fizi.lifehub.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val today = dateFormat.format(Date())

    val allEvents: StateFlow<List<EventEntity>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val upcomingEvents: StateFlow<List<EventEntity>> = repository.getUpcoming(today, 10)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedDate = MutableStateFlow(today)
    val selectedDate: StateFlow<String> = _selectedDate

    val eventsForDate: StateFlow<List<EventEntity>> = _selectedDate.flatMapLatest { date ->
        repository.getByDate(date)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun addEvent(title: String, description: String, date: String, time: String, color: Long, isAllDay: Boolean) {
        viewModelScope.launch {
            repository.insert(
                EventEntity(
                    title = title,
                    description = description,
                    date = date,
                    time = time,
                    color = color,
                    isAllDay = isAllDay
                )
            )
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch { repository.delete(event) }
    }

    fun getDaysUntil(dateStr: String): Long {
        return try {
            val target = dateFormat.parse(dateStr)
            val now = dateFormat.parse(today)
            if (target != null && now != null) {
                ((target.time - now.time) / (1000 * 60 * 60 * 24))
            } else 0
        } catch (e: Exception) { 0 }
    }
}
