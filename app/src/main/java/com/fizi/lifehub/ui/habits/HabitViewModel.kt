package com.fizi.lifehub.ui.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fizi.lifehub.data.local.entity.HabitEntity
import com.fizi.lifehub.data.local.entity.HabitFrequency
import com.fizi.lifehub.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val getAllHabits: GetAllHabitsUseCase,
    private val insertHabit: InsertHabitUseCase,
    private val deleteHabit: DeleteHabitUseCase,
    private val toggleHabit: ToggleHabitForDateUseCase,
    private val isCompleted: IsHabitCompletedUseCase
) : ViewModel() {

    val habits = getAllHabits().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today: String = dateFormat.format(Date())

    // Track completion states for all habits
    private val _completionStates = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    val completionStates: StateFlow<Map<Long, Boolean>> = _completionStates

    init {
        // Observe habits and load completion states
        viewModelScope.launch {
            habits.collect { habitList ->
                habitList.forEach { habit ->
                    isCompleted(habit.id, today).collect { completed ->
                        _completionStates.update { it + (habit.id to completed) }
                    }
                }
            }
        }
    }

    fun addHabit(name: String, icon: String = "⭐", frequency: HabitFrequency = HabitFrequency.DAILY) {
        viewModelScope.launch {
            insertHabit(
                HabitEntity(
                    name = name,
                    icon = icon,
                    frequency = frequency
                )
            )
        }
    }

    fun toggleToday(habitId: Long) {
        viewModelScope.launch {
            toggleHabit(habitId, today)
        }
    }

    fun delete(habit: HabitEntity) {
        viewModelScope.launch { deleteHabit(habit) }
    }

    fun isCompletedToday(habitId: Long): Boolean {
        return _completionStates.value[habitId] ?: false
    }
}
