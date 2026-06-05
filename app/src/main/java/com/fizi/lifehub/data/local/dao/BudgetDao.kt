package com.fizi.lifehub.data.local.dao

import androidx.room.*
import com.fizi.lifehub.data.local.entity.BudgetEntity
import com.fizi.lifehub.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budget ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budget WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: TransactionType): Flow<List<BudgetEntity>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM budget WHERE type = :type")
    fun getTotalByType(type: TransactionType): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM budget WHERE type = 'INCOME'")
    fun getTotalIncome(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM budget WHERE type = 'EXPENSE'")
    fun getTotalExpense(): Flow<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: BudgetEntity): Long

    @Update
    suspend fun updateTransaction(transaction: BudgetEntity)

    @Delete
    suspend fun deleteTransaction(transaction: BudgetEntity)
}
