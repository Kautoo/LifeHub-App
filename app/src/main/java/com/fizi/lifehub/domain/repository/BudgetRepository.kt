package com.fizi.lifehub.domain.repository

import com.fizi.lifehub.data.local.entity.BudgetEntity
import com.fizi.lifehub.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getAllTransactions(): Flow<List<BudgetEntity>>
    fun getTransactionsByType(type: TransactionType): Flow<List<BudgetEntity>>
    fun getTotalIncome(): Flow<Double>
    fun getTotalExpense(): Flow<Double>
    suspend fun insertTransaction(transaction: BudgetEntity): Long
    suspend fun updateTransaction(transaction: BudgetEntity)
    suspend fun deleteTransaction(transaction: BudgetEntity)
}
