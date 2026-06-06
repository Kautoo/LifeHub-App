package com.fizi.lifehub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fizi.lifehub.data.local.entity.TodoEntity
import com.fizi.lifehub.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class HomeStats(
    val pendingTasks: Int = 0,
    val completedTasks: Int = 0,
    val totalHabits: Int = 0,
    val balance: Double = 0.0,
    val streakDays: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTodos: GetAllTodosUseCase,
    private val getAllHabits: GetAllHabitsUseCase,
    private val getTotalIncome: GetTotalIncomeUseCase,
    private val getTotalExpense: GetTotalExpenseUseCase
) : ViewModel() {

    val recentTasks: StateFlow<List<TodoEntity>> = getAllTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val totalIncome = getTotalIncome()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val totalExpense = getTotalExpense()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val stats: StateFlow<HomeStats> = combine(
        getAllTodos(),
        getAllHabits(),
        totalIncome,
        totalExpense
    ) { tasks, habits, income, expense ->
        HomeStats(
            pendingTasks = tasks.count { !it.isDone },
            completedTasks = tasks.count { it.isDone },
            totalHabits = habits.size,
            balance = income - expense,
            streakDays = 0 // simplified
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeStats())

    // Placeholder for completed habits today
    val completedHabitsToday: StateFlow<Int> = flow { emit(0) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}
