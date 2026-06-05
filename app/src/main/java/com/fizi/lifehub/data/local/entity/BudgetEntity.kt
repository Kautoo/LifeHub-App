package com.fizi.lifehub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType {
    INCOME, EXPENSE
}

enum class BudgetCategory {
    FOOD, TRANSPORT, ENTERTAINMENT, EDUCATION, SHOPPING, BILLS, SAVINGS, SALARY, OTHER
}

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val category: BudgetCategory,
    val note: String = "",
    val date: Long = System.currentTimeMillis()
)
