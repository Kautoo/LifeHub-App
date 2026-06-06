package com.fizi.lifehub.ui.goals

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.data.local.entity.GoalEntity
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*

private val goalEmojis = listOf("🎯", "🎓", "💪", "📚", "💰", "🏃", "🌱", "✈️", "🎨", "🏆", "❤️", "🎮")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalScreen(viewModel: GoalViewModel = hiltViewModel()) {
    val activeGoals by viewModel.activeGoals.collectAsState()
    val completedGoals by viewModel.completedGoals.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var newEmoji by remember { mutableStateOf("🎯") }
    var newTargetDate by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        DecorativeBackground()

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
                        "🎯 Goals",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "${activeGoals.size} active • ${completedGoals.size} completed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (activeGoals.isEmpty() && completedGoals.isEmpty()) {
                    item {
                        EmptyState(emoji = "🎯", title = "No goals yet", subtitle = "Set your first goal!")
                    }
                }

                if (activeGoals.isNotEmpty()) {
                    item { SectionHeader(title = "Active Goals", subtitle = "${activeGoals.size} goals") }
                    items(activeGoals, key = { it.id }) { goal ->
                        GoalItem(goal, onProgressChange = { viewModel.updateProgress(goal, it) },
                            onDelete = { viewModel.deleteGoal(goal) })
                    }
                }

                if (completedGoals.isNotEmpty()) {
                    item { SectionHeader(title = "Completed 🏆", subtitle = "${completedGoals.size} achieved") }
                    items(completedGoals, key = { it.id }) { goal ->
                        GoalItem(goal, onProgressChange = {}, onDelete = { viewModel.deleteGoal(goal) })
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false; newTitle = ""; newDesc = ""; newEmoji = "🎯"; newTargetDate = "" },
            title = { Text("🎯 New Goal", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = newTitle, onValueChange = { newTitle = it },
                        label = { Text("Goal title") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                    Text("Icon", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(goalEmojis) { emoji ->
                            Box(
                                modifier = Modifier.size(40.dp).clip(CircleShape)
                                    .background(if (newEmoji == emoji) Primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { newEmoji = emoji },
                                contentAlignment = Alignment.Center
                            ) { Text(emoji, fontSize = 20.sp) }
                        }
                    }
                    OutlinedTextField(
                        value = newTargetDate, onValueChange = { newTargetDate = it },
                        label = { Text("Target date (YYYY-MM-DD)") }, singleLine = true,
                        placeholder = { Text("2026-09-01") },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = newDesc, onValueChange = { newDesc = it },
                        label = { Text("Description (optional)") }, maxLines = 3,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            viewModel.addGoal(newTitle.trim(), newDesc.trim(), newEmoji, newTargetDate.trim())
                            showAddDialog = false; newTitle = ""; newDesc = ""; newEmoji = "🎯"; newTargetDate = ""
                        }
                    },
                    enabled = newTitle.isNotBlank(),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Create Goal") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false; newTitle = ""; newDesc = ""; newEmoji = "🎯"; newTargetDate = ""
                }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun GoalItem(goal: GoalEntity, onProgressChange: (Int) -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(goal.emoji, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(goal.title, fontWeight = FontWeight.SemiBold)
                    if (goal.description.isNotBlank()) {
                        Text(goal.description, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (goal.targetDate.isNotBlank()) {
                        Text("🎯 ${goal.targetDate}", style = MaterialTheme.typography.labelSmall, color = Primary)
                    }
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Delete, "Delete", modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { goal.progress / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = if (goal.progress >= 100) HabitGreen else Primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${goal.progress}%", fontWeight = FontWeight.Bold, color = Primary)
                if (goal.progress < 100) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(25, 50, 75, 100).forEach { pct ->
                            TextButton(
                                onClick = { onProgressChange(pct) },
                                modifier = Modifier.height(28.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                            ) {
                                Text("$pct%", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                } else {
                    Text("✅ Done!", color = HabitGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
