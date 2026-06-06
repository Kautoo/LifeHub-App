package com.fizi.lifehub.ui.habits

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
import com.fizi.lifehub.data.local.entity.HabitFrequency
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(viewModel: HabitViewModel = hiltViewModel()) {
    val habits by viewModel.habits.collectAsState()
    val completionStates by viewModel.completionStates.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newIcon by remember { mutableStateOf("💪") }
    var newFrequency by remember { mutableStateOf(HabitFrequency.DAILY) }

    val completedToday = habits.count { completionStates[it.id] == true }
    val totalHabits = habits.size
    val progress = if (totalHabits > 0) completedToday.toFloat() / totalHabits else 0f

    val habitIcons = listOf("💪", "📚", "🏃", "💧", "🧘", "🎵", "✍️", "🌅", "💤", "🍎", "🎯", "🙏")

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
                            "🎯 Habits",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "$completedToday of $totalHabits completed today",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            floatingActionButton = {
                SlideInFromBottom(delayMs = 400) {
                    GradientFAB(
                        onClick = { showDialog = true }, icon = Icons.Default.Add,
                        gradient = Brush.linearGradient(listOf(HabitGreen, Color(0xFF00C853)))
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (habits.isEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 200) {
                            EmptyState(emoji = "🎯", title = "No habits yet", subtitle = "Start building good habits!")
                        }
                    }
                }

                // ─── Daily Progress Card ───
                if (habits.isNotEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 100) {
                            DailyProgressCard(
                                completed = completedToday,
                                total = totalHabits,
                                progress = progress
                            )
                        }
                    }
                }

                // ─── Habit Cards ───
                items(habits, key = { it.id }) { habit ->
                    val isCompleted = completionStates[habit.id] == true
                    FadeInOnAppear(delayMs = 150) {
                        HabitCard(
                            icon = habit.icon,
                            name = habit.name,
                            frequency = habit.frequency,
                            streak = habit.streak,
                            isCompleted = isCompleted,
                            onToggle = { viewModel.toggleToday(habit.id) },
                            onDelete = { viewModel.delete(habit) }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; newName = ""; newIcon = "💪"; newFrequency = HabitFrequency.DAILY },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BouncyEmoji("🎯", size = 28)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Habit", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = newName, onValueChange = { newName = it },
                        label = { Text("Habit name") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                    Text("Icon", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        habitIcons.take(6).forEach { icon ->
                            Box(
                                modifier = Modifier.size(40.dp).clip(CircleShape)
                                    .background(if (newIcon == icon) Primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { newIcon = icon },
                                contentAlignment = Alignment.Center
                            ) { Text(icon, fontSize = 20.sp) }
                        }
                    }
                    Text("Frequency", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HabitFrequency.entries.forEach { freq ->
                            val isSelected = newFrequency == freq
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { newFrequency = freq }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    freq.name.lowercase().replaceFirstChar { it.uppercase() },
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                BouncyButton(
                    onClick = {
                        if (newName.isNotBlank()) {
                            viewModel.addHabit(newName.trim(), newIcon, newFrequency)
                            showDialog = false; newName = ""; newIcon = "💪"; newFrequency = HabitFrequency.DAILY
                        }
                    },
                    enabled = newName.isNotBlank(),
                    gradient = Brush.linearGradient(listOf(HabitGreen, Color(0xFF00C853)))
                ) { Text("Create", color = Color.White, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; newName = ""; newIcon = "💪"; newFrequency = HabitFrequency.DAILY }) { Text("Cancel") }
            }
        )
    }
}

// ─── Daily Progress Card ───
@Composable
private fun DailyProgressCard(
    completed: Int,
    total: Int,
    progress: Float
) {
    GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradient = Brush.linearGradient(
            listOf(HabitGreen.copy(alpha = 0.85f), Color(0xFF00C853).copy(alpha = 0.65f))
        ),
        cornerRadius = 20.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Today's Progress",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "$completed of $total habits",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(52.dp)) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 5.dp,
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )
                    Text(
                        "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                )
            }
        }
    }
}

// ─── Habit Card ───
@Composable
private fun HabitCard(
    icon: String,
    name: String,
    frequency: HabitFrequency,
    streak: Int,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) HabitGreen.copy(alpha = 0.08f) else SurfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji icon circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) HabitGreen.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Name + streak + frequency
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        frequency.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (streak > 0) {
                        Text(
                            "  ·  🔥 $streak streak",
                            style = MaterialTheme.typography.bodySmall,
                            color = HabitStreak,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Completion toggle checkbox
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = HabitGreen,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            )
        }
    }
}
