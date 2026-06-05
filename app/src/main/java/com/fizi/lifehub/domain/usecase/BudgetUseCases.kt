package com.fizi.lifehub.domain.usecase

import com.fizi.lifehub.data.local.entity.BudgetEntity
import com.fizi.lifehub.data.local.entity.TransactionType
import com.fizi.lifehub.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    operator fun invoke(): Flow<List<BudgetEntity>> = repository.getAllTransactions()
}

class GetTotalIncomeUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    operator fun invoke(): Flow<Double> = repository.getTotalIncome()
}

class GetTotalExpenseUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    operator fun invoke(): Flow<Double> = repository.getTotalExpense()
}

class InsertTransactionUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(transaction: BudgetEntity): Long =
        repository.insertTransaction(transaction)
}

class DeleteTransactionUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(transaction: BudgetEntity) =
        repository.deleteTransaction(transaction)
}
