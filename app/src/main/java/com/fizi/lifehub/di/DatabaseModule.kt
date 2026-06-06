package com.fizi.lifehub.di

import android.content.Context
import androidx.room.Room
import com.fizi.lifehub.data.local.dao.*
import com.fizi.lifehub.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "lifehub.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides fun provideTodoDao(db: AppDatabase): TodoDao = db.todoDao()
    @Provides fun provideNoteDao(db: AppDatabase): NoteDao = db.noteDao()
    @Provides fun provideBudgetDao(db: AppDatabase): BudgetDao = db.budgetDao()
    @Provides fun provideHabitDao(db: AppDatabase): HabitDao = db.habitDao()
    @Provides fun provideHabitLogDao(db: AppDatabase): HabitLogDao = db.habitLogDao()
    @Provides fun providePomodoroDao(db: AppDatabase): PomodoroDao = db.pomodoroDao()
    @Provides fun provideJournalDao(db: AppDatabase): JournalDao = db.journalDao()
    @Provides fun provideEventDao(db: AppDatabase): EventDao = db.eventDao()
    @Provides fun provideGoalDao(db: AppDatabase): GoalDao = db.goalDao()
    @Provides fun provideWaterDao(db: AppDatabase): WaterDao = db.waterDao()
}
