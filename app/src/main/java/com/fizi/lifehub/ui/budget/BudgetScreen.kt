package com.fizi.lifehub.ui.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.data.local.entity.BudgetCategory
import com.fizi.lifehub.data.local.entity.BudgetEntity
import com.fizi.lifehub.data.local.entity.TransactionType
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(viewModel: BudgetViewModel = hiltViewModel()) {
    val transactions by viewModel.transactions.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val balance by viewModel.balance.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newAmount by remember { mutableStateOf("") }
    var newType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var newCategory by remember { mutableStateOf(BudgetCategory.OTHER) }
    var newNote by remember { mutableStateOf("") }
    val df = remember { DecimalFormat("#,##0.00") }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Tertiary.copy(alpha = 0.06f), Color.Transparent)
                            )
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        "Budget",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    if (transactions.isNotEmpty()) {
                        Text(
                            "${transactions.size} transactions",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            floatingActionButton = {
                GradientFAB(
                    onClick = { showAddDialog = true },
                    icon = Icons.Default.Add,
                    gradient = GradientSunset
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Balance Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = Primary.copy(alpha = 0.15f),
                            spotColor = Primary.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        listOf(Primary, Teal700, Primary.copy(alpha = 0.8f))
                                    )
                                )
                                .padding(28.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Total Balance",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "RM ${df.format(balance)}",
                                    style = MaterialTheme.typography.displayLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    fontSize = 36.sp
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Card(
                                            shape = RoundedCornerShape(14.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color.White.copy(alpha = 0.2f)
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text("↑", color = Color(0xFFB9F6CA), fontWeight = FontWeight.Bold)
                                                Text(
                                                    "RM ${df.format(totalIncome)}",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                        Text("Income", color = Color.White.copy(alpha = 0.7f),
                                            style = MaterialTheme.typography.labelSmall)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Card(
                                            shape = RoundedCornerShape(14.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color.White.copy(alpha = 0.2f)
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text("↓", color = Color(0xFFFFCDD2), fontWeight = FontWeight.Bold)
                                                Text(
                                                    "RM ${df.format(totalExpense)}",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                        Text("Expense", color = Color.White.copy(alpha = 0.7f),
                                            style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }
                }

                if (transactions.isEmpty()) {
                    item {
                        EmptyState(
                            emoji = "💰",
                            title = "No transactions yet",
                            subtitle = "Tap + to add your first transaction"
                        )
                    }
                }

                if (transactions.isNotEmpty()) {
                    item {
                        SectionHeader(title = "Transactions", subtitle = "${transactions.size} records")
                    }
                }

                items(transactions, key = { it.id }) { transaction ->
                    ModernTransactionItem(transaction, df) { viewModel.delete(transaction) }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false; newTitle = ""; newAmount = ""
                newType = TransactionType.EXPENSE; newCategory = BudgetCategory.OTHER; newNote = ""
            },
            title = { Text("💸 New Transaction", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TransactionType.entries.forEach { type ->
                            FilterChip(
                                selected = newType == type,
                                onClick = { newType = type },
                                label = { Text(if (type == TransactionType.INCOME) "💚 Income" else "❤️ Expense") },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = if (type == TransactionType.INCOME)
                                        IncomeColor.copy(alpha = 0.15f) else Error.copy(alpha = 0.15f)
                                )
                            )
                        }
                    }
                    OutlinedTextField(
                        value = newTitle, onValueChange = { newTitle = it },
                        label = { Text("Title") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = newAmount,
                        onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*$"))) newAmount = it },
                        label = { Text("Amount (RM)") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                    Text("Category", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(BudgetCategory.entries) { cat ->
                            FilterChip(
                                selected = newCategory == cat,
                                onClick = { newCategory = cat },
                                label = { Text(cat.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelSmall) },
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                    OutlinedTextField(
                        value = newNote, onValueChange = { newNote = it },
                        label = { Text("Note (optional)") }, maxLines = 2,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = newAmount.toDoubleOrNull()
                        if (newTitle.isNotBlank() && amount != null && amount > 0) {
                            viewModel.addTransaction(newTitle.trim(), amount, newType, newCategory, newNote.trim())
                            showAddDialog = false; newTitle = ""; newAmount = ""
                            newType = TransactionType.EXPENSE; newCategory = BudgetCategory.OTHER; newNote = ""
                        }
                    },
                    enabled = newTitle.isNotBlank() && newAmount.toDoubleOrNull() != null,
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false; newTitle = ""; newAmount = ""
                    newType = TransactionType.EXPENSE; newCategory = BudgetCategory.OTHER; newNote = ""
                }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun ModernTransactionItem(transaction: BudgetEntity, df: DecimalFormat, onDelete: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val isIncome = transaction.type == TransactionType.INCOME
    val categoryEmoji = when (transaction.category) {
        BudgetCategory.FOOD -> "🍔"; BudgetCategory.TRANSPORT -> "🚗"
        BudgetCategory.ENTERTAINMENT -> "🎮"; BudgetCategory.EDUCATION -> "📚"
        BudgetCategory.SHOPPING -> "🛍️"; BudgetCategory.BILLS -> "📱"
        BudgetCategory.SAVINGS -> "🏦"; BudgetCategory.SALARY -> "💵"
        BudgetCategory.OTHER -> "📋"
    }

    Card(
        modifier = Modifier.fillMaxWidth().shadow(
            elevation = 3.dp, shape = RoundedCornerShape(16.dp),
            ambientColor = Color.Black.copy(alpha = 0.05f),
            spotColor = Color.Black.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape)
                    .background(if (isIncome) IncomeColor.copy(alpha = 0.1f) else Error.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) { Text(categoryEmoji, fontSize = 20.sp) }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    "${transaction.category.name.lowercase().replaceFirstChar { it.uppercase() }} • ${dateFormat.format(Date(transaction.date))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "${if (isIncome) "+" else "-"}RM ${df.format(transaction.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isIncome) IncomeColor else Error
            )
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp))
            }
        }
    }
}
