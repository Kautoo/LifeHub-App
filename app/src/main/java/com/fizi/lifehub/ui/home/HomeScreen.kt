package com.fizi.lifehub.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.data.local.entity.TodoEntity
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToTodo: () -> Unit = {},
    onNavigateToNotes: () -> Unit = {},
    onNavigateToBudget: () -> Unit = {},
    onNavigateToHabits: () -> Unit = {}
) {
    val todos by viewModel.todos.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val habits by viewModel.habits.collectAsState()
    val completedHabits by viewModel.completedHabits.collectAsState()

    val balance = totalIncome - totalExpense
    val pendingTodos = todos.count { !it.isDone }
    val completedTodos = todos.count { it.isDone }
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    val greeting = when {
        hour < 12 -> "Good Morning ☀️"
        hour < 17 -> "Good Afternoon 🌤️"
        hour < 21 -> "Good Evening 🌙"
        else -> "Good Night 🌟"
    }

    val motivationalQuote = remember {
        listOf(
            "Small steps every day lead to big changes 💪",
            "You're doing great, keep going! ✨",
            "Progress, not perfection 🎯",
            "Today is a fresh start 🌱",
            "Believe in yourself! 🚀",
            "Consistency is key 🔑",
            "One task at a time 📝"
        ).random()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Decorative background circles
        DecorativeBackground()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // ─── Hero Header ───
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Primary.copy(alpha = 0.12f),
                                    Primary.copy(alpha = 0.04f),
                                    Color.Transparent
                                )
                            )
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Column {
                        Text(
                            greeting,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            motivationalQuote,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // ─── Quick Stats Row ───
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        QuickStatCard(
                            emoji = "📋",
                            value = "$pendingTodos",
                            label = "Tasks Pending",
                            gradient = GradientPrimary,
                            onClick = onNavigateToTodo
                        )
                    }
                    item {
                        QuickStatCard(
                            emoji = "✅",
                            value = "$completedTodos",
                            label = "Completed",
                            gradient = GradientForest,
                            onClick = onNavigateToTodo
                        )
                    }
                    item {
                        QuickStatCard(
                            emoji = "🎯",
                            value = "$completedHabits/${habits.size}",
                            label = "Habits Today",
                            gradient = GradientPurple,
                            onClick = onNavigateToHabits
                        )
                    }
                    item {
                        QuickStatCard(
                            emoji = "💰",
                            value = "RM ${String.format("%.0f", balance)}",
                            label = "Balance",
                            gradient = if (balance >= 0) GradientOcean else GradientSunset,
                            onClick = onNavigateToBudget
                        )
                    }
                }
            }

            // ─── Today's Overview ───
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    SectionHeader(title = "Today's Overview", subtitle = "Stay on track")
                }
            }

            // ─── Feature Cards ───
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FeatureCard(
                        title = "Todo",
                        emoji = "✅",
                        subtitle = "$pendingTodos pending",
                        gradient = GradientPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToTodo
                    )
                    FeatureCard(
                        title = "Notes",
                        emoji = "📝",
                        subtitle = "Quick capture",
                        gradient = GradientPurple,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToNotes
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FeatureCard(
                        title = "Budget",
                        emoji = "💰",
                        subtitle = "RM ${String.format("%.0f", balance)} balance",
                        gradient = GradientSunset,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToBudget
                    )
                    FeatureCard(
                        title = "Habits",
                        emoji = "🎯",
                        subtitle = "$completedHabits/${habits.size} done",
                        gradient = GradientForest,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToHabits
                    )
                }
            }

            // ─── Recent Tasks ───
            if (todos.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        SectionHeader(
                            title = "Recent Tasks",
                            subtitle = "${todos.size} total",
                            action = {
                                TextButton(onClick = onNavigateToTodo) {
                                    Text("See All", color = Primary)
                                }
                            }
                        )
                    }
                }

                items(todos.take(5)) { todo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (todo.isDone) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                else MaterialTheme.colorScheme.surface
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    when (todo.priority) { 3 -> Error; 2 -> Tertiary; else -> IncomeColor },
                                    CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            if (todo.isDone) "✅" else "⬜",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            todo.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (todo.isDone) FontWeight.Normal else FontWeight.Medium,
                            color = if (todo.isDone) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // ─── Motivation Card ───
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(listOf(Secondary, Purple700))
                            )
                            .padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("💪", fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "LifeHub",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Your life, organized.\nOne hub at a time.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// ─── Decorative Background ───
@Composable
fun DecorativeBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top-right circle
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 250.dp, y = (-50).dp)
                .background(
                    Primary.copy(alpha = 0.04f),
                    CircleShape
                )
        )
        // Bottom-left circle
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = 600.dp)
                .background(
                    Secondary.copy(alpha = 0.03f),
                    CircleShape
                )
        )
        // Middle-right small circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(x = 320.dp, y = 400.dp)
                .background(
                    Tertiary.copy(alpha = 0.04f),
                    CircleShape
                )
        )
    }
}

// ─── Quick Stat Card ───
@Composable
fun QuickStatCard(
    emoji: String,
    value: String,
    label: String,
    gradient: Brush,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .shadow(6.dp, RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(16.dp)
        ) {
            Column {
                Text(emoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// ─── Feature Card ───
@Composable
fun FeatureCard(
    title: String,
    emoji: String,
    subtitle: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(emoji, fontSize = 28.sp)
                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
