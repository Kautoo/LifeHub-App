package com.fizi.lifehub.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fizi.lifehub.data.local.entity.GoalEntity
import com.fizi.lifehub.data.local.entity.GoalStatus
import com.fizi.lifehub.domain.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val repository: GoalRepository
) : ViewModel() {

    val activeGoals: StateFlow<List<GoalEntity>> = repository.getActive()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedGoals: StateFlow<List<GoalEntity>> = repository.getCompleted()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addGoal(title: String, description: String, emoji: String, targetDate: String) {
        viewModelScope.launch {
            repository.insert(
                GoalEntity(title = title, description = description, emoji = emoji, targetDate = targetDate)
            )
        }
    }

    fun updateProgress(goal: GoalEntity, progress: Int) {
        viewModelScope.launch {
            repository.update(
                goal.copy(
                    progress = progress,
                    status = if (progress >= 100) GoalStatus.COMPLETED else goal.status
                )
            )
        }
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch { repository.delete(goal) }
    }
}
