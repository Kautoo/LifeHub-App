package com.fizi.lifehub.ui.habits

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.data.local.entity.HabitEntity
import com.fizi.lifehub.data.local.entity.HabitFrequency
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

private val emojiIcons = listOf(
    "⭐", "💪", "📚", "🏃", "💧", "🧘", "🎵", "✍️",
    "🥗", "😴", "🧹", "💻", "🎯", "🌱", "🎮", "🙏"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(viewModel: HabitViewModel = hiltViewModel()) {
    val habits by viewModel.habits.collectAsState()
    val completionStates by viewModel.completionStates.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("⭐") }
    var selectedFrequency by remember { mutableStateOf(HabitFrequency.DAILY) }

    val dayName = remember { SimpleDateFormat("EEEE", Locale.getDefault()).format(Date()) }
    val dateStr = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()) }
    val completedToday = habits.count { completionStates[it.id] == true }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(HabitGreen.copy(alpha = 0.06f), Color.Transparent)
                            )
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        "Habits",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "$dayName • $dateStr",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            floatingActionButton = {
                GradientFAB(
                    onClick = { showAddDialog = true },
                    icon = Icons.Default.Add,
                    gradient = GradientForest
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Progress Card
                if (habits.isNotEmpty()) {
                    item {
                        val progress = completedToday.toFloat() / habits.size

                        Card(
                            modifier = Modifier.fillMaxWidth().shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(24.dp),
                                ambientColor = HabitGreen.copy(alpha = 0.15f),
                                spotColor = HabitGreen.copy(alpha = 0.15f)
                            ),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.linearGradient(listOf(HabitGreen, Teal700))
                                    )
                                    .padding(28.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                                ) {
                                    // Progress Ring
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.size(90.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            progress = { progress },
                                            modifier = Modifier.fillMaxSize(),
                                            strokeWidth = 8.dp,
                                            color = Color.White,
                                            trackColor = Color.White.copy(alpha = 0.2f)
                                        )
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                "$completedToday/${habits.size}",
                                                style = MaterialTheme.typography.headlineMedium,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White
                                            )
                                            Text(
                                                "done",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White.copy(alpha = 0.7f)
                                            )
                                        }
                                    }

                                    Column {
                                        Text(
                                            if (completedToday == habits.size) "🎉 Amazing!"
                                            else "Keep Going!",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            if (completedToday == habits.size) "All habits completed!"
                                            else "${habits.size - completedToday} more to go",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    SectionHeader(
                        title = "Today's Habits",
                        subtitle = "${habits.size} habits"
                    )
                }

                if (habits.isEmpty()) {
                    item {
                        EmptyState(
                            emoji = "🎯",
                            title = "No habits yet",
                            subtitle = "Tap + to start building habits!"
                        )
                    }
                }

                items(habits, key = { it.id }) { habit ->
                    ModernHabitItem(
                        habit = habit,
                        isCompleted = completionStates[habit.id] ?: false,
                        onToggle = { viewModel.toggleToday(habit.id) },
                        onDelete = { viewModel.delete(habit) }
                    )
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false; newName = ""; selectedIcon = "⭐"
                selectedFrequency = HabitFrequency.DAILY
            },
            title = { Text("🎯 New Habit", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = newName, onValueChange = { newName = it },
                        label = { Text("Habit name") },
                        placeholder = { Text("e.g., Drink water, Read 30 min") },
                        singleLine = true, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    Text("Icon", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(emojiIcons) { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (selectedIcon == emoji) HabitGreen.copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .clickable { selectedIcon = emoji },
                                contentAlignment = Alignment.Center
                            ) { Text(emoji, fontSize = 22.sp) }
                        }
                    }
                    Text("Frequency", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HabitFrequency.entries.forEach { freq ->
                            FilterChip(
                                selected = selectedFrequency == freq,
                                onClick = { selectedFrequency = freq },
                                label = { Text(if (freq == HabitFrequency.DAILY) "📅 Daily" else "📆 Weekly") },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank()) {
                            viewModel.addHabit(newName.trim(), selectedIcon, selectedFrequency)
                            showAddDialog = false; newName = ""; selectedIcon = "⭐"
                            selectedFrequency = HabitFrequency.DAILY
                        }
                    },
                    enabled = newName.isNotBlank(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HabitGreen)
                ) { Text("Create Habit") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false; newName = ""; selectedIcon = "⭐"
                    selectedFrequency = HabitFrequency.DAILY
                }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun ModernHabitItem(
    habit: HabitEntity, isCompleted: Boolean, onToggle: () -> Unit, onDelete: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isCompleted) 1.02f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow), label = "scale"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isCompleted) HabitGreen.copy(alpha = 0.08f)
        else MaterialTheme.colorScheme.surface,
        animationSpec = tween(300), label = "color"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (isCompleted) 0.dp else 4.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = HabitGreen.copy(alpha = 0.06f),
                spotColor = HabitGreen.copy(alpha = 0.06f)
            )
            .clickable { onToggle() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EmojiCircle(
                emoji = habit.icon, size = 50.dp,
                backgroundColor = if (isCompleted) HabitGreen.copy(alpha = 0.15f)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    habit.name, style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (habit.frequency == HabitFrequency.DAILY) "📅 Daily" else "📆 Weekly",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (habit.streak > 0) {
                        Text(
                            " • 🔥 ${habit.streak}",
                            style = MaterialTheme.typography.bodySmall, color = HabitStreak
                        )
                    }
                    if (habit.bestStreak > 0) {
                        Text(
                            " • Best: ${habit.bestStreak}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) HabitGreen else MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Text("✓", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Delete, "Delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
            }
        }
    }
}
