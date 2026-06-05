package com.fizi.lifehub.data.repository

import com.fizi.lifehub.data.local.dao.BudgetDao
import com.fizi.lifehub.data.local.entity.BudgetEntity
import com.fizi.lifehub.data.local.entity.TransactionType
import com.fizi.lifehub.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val dao: BudgetDao
) : BudgetRepository {

    override fun getAllTransactions(): Flow<List<BudgetEntity>> = dao.getAllTransactions()

    override fun getTransactionsByType(type: TransactionType): Flow<List<BudgetEntity>> =
        dao.getTransactionsByType(type)

    override fun getTotalIncome(): Flow<Double> = dao.getTotalIncome()

    override fun getTotalExpense(): Flow<Double> = dao.getTotalExpense()

    override suspend fun insertTransaction(transaction: BudgetEntity): Long =
        dao.insertTransaction(transaction)

    override suspend fun updateTransaction(transaction: BudgetEntity) =
        dao.updateTransaction(transaction)

    override suspend fun deleteTransaction(transaction: BudgetEntity) =
        dao.deleteTransaction(transaction)
}
