package com.fizi.lifehub.ui.budget

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.data.local.entity.BudgetCategory
import com.fizi.lifehub.data.local.entity.TransactionType
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

private val categoryIcons = mapOf(
    BudgetCategory.FOOD to "🍔", BudgetCategory.TRANSPORT to "🚗",
    BudgetCategory.ENTERTAINMENT to "🎮", BudgetCategory.EDUCATION to "📚",
    BudgetCategory.SHOPPING to "🛍️", BudgetCategory.BILLS to "📄", BudgetCategory.SAVINGS to "🏦",
    BudgetCategory.SALARY to "💰", BudgetCategory.OTHER to "📦"
)

private val categoryColors = mapOf(
    BudgetCategory.FOOD to Color(0xFFFF8A65),
    BudgetCategory.TRANSPORT to Color(0xFF42A5F5),
    BudgetCategory.ENTERTAINMENT to Color(0xFFAB47BC),
    BudgetCategory.EDUCATION to Color(0xFF66BB6A),
    BudgetCategory.SHOPPING to Color(0xFFFFB300),
    BudgetCategory.BILLS to Color(0xFFEF5350),
    BudgetCategory.SAVINGS to Color(0xFF26C6DA),
    BudgetCategory.SALARY to Color(0xFF4ADE80),
    BudgetCategory.OTHER to Color(0xFF918FA1)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(viewModel: BudgetViewModel = hiltViewModel()) {
    val transactions by viewModel.transactions.collectAsState()
    val balance by viewModel.balance.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newAmount by remember { mutableStateOf("") }
    var newType by remember { mutableStateOf(TransactionType.INCOME) }
    var newCategory by remember { mutableStateOf(BudgetCategory.OTHER) }

    val dateFormat = remember { SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault()) }

    Box(modifier = Modifier.fillMaxSize()) {
        DecorativeBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                FadeInOnAppear(delayMs = 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            "💰 Budget",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "Track your finances",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            floatingActionButton = {
                SlideInFromBottom(delayMs = 400) {
                    GradientFAB(onClick = { showDialog = true }, icon = Icons.Default.Add, gradient = GradientForest)
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ─── Balance Card ───
                item {
                    FadeInOnAppear(delayMs = 100) {
                        BalanceCard(
                            balance = balance,
                            totalIncome = totalIncome,
                            totalExpense = totalExpense
                        )
                    }
                }

                // ─── Transactions Section ───
                if (transactions.isNotEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 200) {
                            SectionHeader(title = "Transactions", subtitle = "${transactions.size} records")
                        }
                    }
                    items(transactions.take(30), key = { it.id }) { tx ->
                        FadeInOnAppear(delayMs = 250) {
                            TransactionItem(
                                title = tx.title,
                                amount = tx.amount,
                                type = tx.type,
                                category = tx.category,
                                date = dateFormat.format(Date(tx.date)),
                                onDelete = { viewModel.delete(tx) }
                            )
                        }
                    }
                }

                if (transactions.isEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 200) {
                            EmptyState(
                                emoji = "💰",
                                title = "No transactions yet",
                                subtitle = "Tap + to add income or expense"
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; newTitle = ""; newAmount = "" },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BouncyEmoji("💰", size = 28)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Transaction", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newTitle, onValueChange = { newTitle = it },
                        label = { Text("Description") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = newAmount, onValueChange = { newAmount = it },
                        label = { Text("Amount (RM)") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                    Text("Type", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TransactionType.entries.forEach { type ->
                            val isSelected = newType == type
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) {
                                            if (type == TransactionType.INCOME) IncomeColor.copy(alpha = 0.15f) else ExpenseColor.copy(alpha = 0.15f)
                                        } else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .clickable { newType = type }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    if (type == TransactionType.INCOME) "📈 Income" else "📉 Expense",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) {
                                        if (type == TransactionType.INCOME) IncomeColor else ExpenseColor
                                    } else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    Text("Category", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        BudgetCategory.entries.take(5).forEach { cat ->
                            val isSelected = newCategory == cat
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { newCategory = cat }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) { Text(categoryIcons[cat] ?: "📦", fontSize = 16.sp) }
                        }
                    }
                }
            },
            confirmButton = {
                BouncyButton(
                    onClick = {
                        val amount = newAmount.toDoubleOrNull()
                        if (newTitle.isNotBlank() && amount != null && amount > 0) {
                            viewModel.addTransaction(newTitle.trim(), amount, newType, newCategory)
                            showDialog = false; newTitle = ""; newAmount = ""
                        }
                    },
                    enabled = newTitle.isNotBlank() && newAmount.toDoubleOrNull() != null,
                    gradient = GradientForest
                ) { Text("Add", color = Color.White, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showDialog = false; newTitle = ""; newAmount = "" }) { Text("Cancel") } }
        )
    }
}

// ─── Balance Card (Stitch style) ───
@Composable
private fun BalanceCard(
    balance: Double,
    totalIncome: Double,
    totalExpense: Double
) {
    GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradient = Brush.linearGradient(listOf(Color(0xFF00897B), Color(0xFF00695C))),
        cornerRadius = 22.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Total Balance",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "RM ${"%.2f".format(balance)}",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            // Income / Expense row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Income
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📈", fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Income",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "RM ${"%.0f".format(totalIncome)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(50.dp)
                        .background(Color.White.copy(alpha = 0.25f))
                )
                // Expense
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📉", fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Expense",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "RM ${"%.0f".format(totalExpense)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// ─── Transaction Item ───
@Composable
private fun TransactionItem(
    title: String,
    amount: Double,
    type: TransactionType,
    category: BudgetCategory,
    date: String,
    onDelete: () -> Unit
) {
    val isIncome = type == TransactionType.INCOME
    val catColor = categoryColors[category] ?: Outline

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category icon circle
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(catColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(categoryIcons[category] ?: "📦", fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Title + category + date
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        category.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "  ·  $date",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            // Amount
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${if (isIncome) "+" else "-"}RM ${"%.2f".format(amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isIncome) IncomeColor else ExpenseColor
                )
            }
        }
    }
}
