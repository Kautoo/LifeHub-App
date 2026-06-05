package com.fizi.lifehub.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fizi.lifehub.data.local.converter.Converters
import com.fizi.lifehub.data.local.dao.*
import com.fizi.lifehub.data.local.entity.*

@Database(
    entities = [
        TodoEntity::class,
        NoteEntity::class,
        BudgetEntity::class,
        HabitEntity::class,
        HabitLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun noteDao(): NoteDao
    abstract fun budgetDao(): BudgetDao
    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao
}
