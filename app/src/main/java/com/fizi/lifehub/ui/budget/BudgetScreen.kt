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
import androidx.compose.ui.draw.shadow
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
    BudgetCategory.SHOPPING to "🛍️", BudgetCategory.BILLS to "📄",
    BudgetCategory.SAVINGS to "🏦", BudgetCategory.SALARY to "💰",
    BudgetCategory.OTHER to "📦"
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

    val dateFormat = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }

    Box(modifier = Modifier.fillMaxSize()) {
        DecorativeBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                FadeInOnAppear(delayMs = 0) {
                    Box(modifier = Modifier.fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(Color(0xFF00897B).copy(alpha = 0.08f), Color.Transparent)))
                        .statusBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp)
                    ) { Text("💰 Budget", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.ExtraBold) }
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    FadeInOnAppear(delayMs = 100) {
                        GradientGlassCard(
                            modifier = Modifier.fillMaxWidth().pressEffect(),
                            gradient = Brush.linearGradient(listOf(Color(0xFF00897B), Color(0xFF00695C)))
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Balance", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("RM ${"%.2f".format(balance)}", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        BouncyEmoji("📈", size = 24)
                                        Text("RM ${"%.0f".format(totalIncome)}", fontWeight = FontWeight.Bold, color = Color.White)
                                        Text("Income", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                                    }
                                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.White.copy(alpha = 0.3f)))
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        BouncyEmoji("📉", size = 24)
                                        Text("RM ${"%.0f".format(totalExpense)}", fontWeight = FontWeight.Bold, color = Color.White)
                                        Text("Expense", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                                    }
                                }
                            }
                        }
                    }
                }

                if (transactions.isNotEmpty()) {
                    item { FadeInOnAppear(delayMs = 200) { SectionHeader(title = "Transactions", subtitle = "${transactions.size} records") } }
                    items(transactions.take(20), key = { it.id }) { tx ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) { kotlinx.coroutines.delay(300); visible = true }
                        AnimatedVisibility(visible = visible,
                            enter = fadeIn(tween(400)) + slideInHorizontally(spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow), initialOffsetX = { it / 5 })
                        ) {
                            GlassCard(modifier = Modifier.fillMaxWidth().pressEffect()) {
                                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(40.dp).clip(CircleShape)
                                        .background(if (tx.type == TransactionType.INCOME) IncomeColor.copy(alpha = 0.15f) else ExpenseColor.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) { Text(categoryIcons[tx.category] ?: "📦", fontSize = 20.sp) }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(tx.title, fontWeight = FontWeight.SemiBold)
                                        Text(tx.category.name.lowercase().replaceFirstChar { it.uppercase() },
                                            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("${if (tx.type == TransactionType.INCOME) "+" else "-"}RM ${"%.2f".format(tx.amount)}",
                                            fontWeight = FontWeight.Bold,
                                            color = if (tx.type == TransactionType.INCOME) IncomeColor else ExpenseColor)
                                        Text(dateFormat.format(Date(tx.date)), style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    IconButton(onClick = { viewModel.delete(tx) }, modifier = Modifier.size(28.dp)) {
                                        Icon(Icons.Default.Delete, "Delete", modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }

                if (transactions.isEmpty()) {
                    item { FadeInOnAppear(delayMs = 200) { EmptyState(emoji = "💰", title = "No transactions yet", subtitle = "Tap + to add income or expense") } }
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
                    BouncyEmoji("💰", size = 28); Spacer(modifier = Modifier.width(8.dp))
                    Text("New Transaction", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = newTitle, onValueChange = { newTitle = it },
                        label = { Text("Description") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                    OutlinedTextField(value = newAmount, onValueChange = { newAmount = it },
                        label = { Text("Amount (RM)") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                    Text("Type", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TransactionType.entries.forEach { type ->
                            val isSelected = newType == type
                            Box(modifier = Modifier.clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) { if (type == TransactionType.INCOME) IncomeColor.copy(alpha = 0.15f) else ExpenseColor.copy(alpha = 0.15f) } else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { newType = type }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(if (type == TransactionType.INCOME) "📈 Income" else "📉 Expense",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) { if (type == TransactionType.INCOME) IncomeColor else ExpenseColor } else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    Text("Category", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        BudgetCategory.entries.take(5).forEach { cat ->
                            val isSelected = newCategory == cat
                            Box(modifier = Modifier.clip(RoundedCornerShape(10.dp))
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
