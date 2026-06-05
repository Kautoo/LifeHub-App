package com.fizi.lifehub.domain.usecase

import com.fizi.lifehub.data.local.entity.HabitEntity
import com.fizi.lifehub.data.local.entity.HabitLogEntity
import com.fizi.lifehub.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllHabitsUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(): Flow<List<HabitEntity>> = repository.getAllHabits()
}

class InsertHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: HabitEntity): Long = repository.insertHabit(habit)
}

class UpdateHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: HabitEntity) = repository.updateHabit(habit)
}

class DeleteHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: HabitEntity) = repository.deleteHabit(habit)
}

class ToggleHabitForDateUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitId: Long, date: String) =
        repository.toggleHabitForDate(habitId, date)
}

class IsHabitCompletedUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(habitId: Long, date: String): Flow<Boolean> =
        repository.isCompletedOnDate(habitId, date)
}

class GetHabitCompletionCountUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(habitId: Long): Flow<Int> = repository.getCompletionCount(habitId)
}
