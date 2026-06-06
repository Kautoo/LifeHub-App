package com.fizi.lifehub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fizi.lifehub.data.local.entity.HabitEntity
import com.fizi.lifehub.data.local.entity.TodoEntity
import com.fizi.lifehub.domain.repository.BudgetRepository
import com.fizi.lifehub.domain.repository.HabitRepository
import com.fizi.lifehub.domain.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val budgetRepository: BudgetRepository,
    private val habitRepository: HabitRepository
) : ViewModel() {

    val todos: StateFlow<List<TodoEntity>> = todoRepository.getAllTodos().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val totalIncome: StateFlow<Double> = budgetRepository.getTotalIncome().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
    )

    val totalExpense: StateFlow<Double> = budgetRepository.getTotalExpense().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
    )

    val habits: StateFlow<List<HabitEntity>> = habitRepository.getAllHabits().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val completedHabits: StateFlow<Int> = habits.flatMapLatest { habitList ->
        if (habitList.isEmpty()) flowOf(0)
        else {
            val flows = habitList.map { habit ->
                habitRepository.isCompletedOnDate(habit.id, today)
            }
            combine(flows) { results -> results.count { it } }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}
