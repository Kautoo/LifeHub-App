package com.fizi.lifehub.data.local.converter

import androidx.room.TypeConverter
import com.fizi.lifehub.data.local.entity.BudgetCategory
import com.fizi.lifehub.data.local.entity.HabitFrequency
import com.fizi.lifehub.data.local.entity.TransactionType

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
}
