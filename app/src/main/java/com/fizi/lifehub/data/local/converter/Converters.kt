package com.fizi.lifehub.data.local.converter

import androidx.room.TypeConverter
import com.fizi.lifehub.data.local.entity.*

class Converters {
    @TypeConverter
    fun fromTransactionType(value: TransactionType): String = value.name
    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)

    @TypeConverter
    fun fromBudgetCategory(value: BudgetCategory): String = value.name
    @TypeConverter
    fun toBudgetCategory(value: String): BudgetCategory = BudgetCategory.valueOf(value)

    @TypeConverter
    fun fromHabitFrequency(value: HabitFrequency): String = value.name
    @TypeConverter
    fun toHabitFrequency(value: String): HabitFrequency = HabitFrequency.valueOf(value)

    @TypeConverter
    fun fromMood(value: Mood): String = value.name
    @TypeConverter
    fun toMood(value: String): Mood = Mood.valueOf(value)

    @TypeConverter
    fun fromGoalStatus(value: GoalStatus): String = value.name
    @TypeConverter
    fun toGoalStatus(value: String): GoalStatus = GoalStatus.valueOf(value)
}
