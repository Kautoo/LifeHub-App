package com.fizi.lifehub.di

import com.fizi.lifehub.data.repository.*
import com.fizi.lifehub.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindTodoRepository(impl: TodoRepositoryImpl): TodoRepository
    @Binds @Singleton abstract fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository
    @Binds @Singleton abstract fun bindBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository
    @Binds @Singleton abstract fun bindHabitRepository(impl: HabitRepositoryImpl): HabitRepository
    @Binds @Singleton abstract fun bindPomodoroRepository(impl: PomodoroRepositoryImpl): PomodoroRepository
    @Binds @Singleton abstract fun bindJournalRepository(impl: JournalRepositoryImpl): JournalRepository
    @Binds @Singleton abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository
    @Binds @Singleton abstract fun bindGoalRepository(impl: GoalRepositoryImpl): GoalRepository
    @Binds @Singleton abstract fun bindWaterRepository(impl: WaterRepositoryImpl): WaterRepository
}
