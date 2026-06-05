package com.fizi.lifehub.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fizi.lifehub.data.local.entity.BudgetCategory
import com.fizi.lifehub.data.local.entity.BudgetEntity
import com.fizi.lifehub.data.local.entity.TransactionType
import com.fizi.lifehub.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getAllTransactions: GetAllTransactionsUseCase,
    private val getTotalIncome: GetTotalIncomeUseCase,
    private val getTotalExpense: GetTotalExpenseUseCase,
    private val insertTransaction: InsertTransactionUseCase,
    private val deleteTransaction: DeleteTransactionUseCase
) : ViewModel() {

    val transactions = getAllTransactions().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val totalIncome = getTotalIncome().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    val totalExpense = getTotalExpense().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    val balance = combine(totalIncome, totalExpense) { income, expense ->
        income - expense
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addTransaction(
        title: String,
        amount: Double,
        type: TransactionType,
        category: BudgetCategory,
        note: String = ""
    ) {
        viewModelScope.launch {
            insertTransaction(
                BudgetEntity(
                    title = title,
                    amount = amount,
                    type = type,
                    category = category,
                    note = note
                )
            )
        }
    }

    fun delete(transaction: BudgetEntity) {
        viewModelScope.launch { deleteTransaction(transaction) }
    }
}
