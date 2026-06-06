package com.fizi.lifehub.ui.goals

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Flag
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

// ═══════════════════════════════════════════
// 🎯 GoalScreen — Google Stitch Design
// ═══════════════════════════════════════════

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
                FadeInOnAppear(delayMs = 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Primary.copy(alpha = 0.06f), Color.Transparent)
                                )
                            )
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Goals",
                                    style = MaterialTheme.typography.displaySmall.copy(
                                        fontFamily = SpaceGrotesk,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "${activeGoals.size} active · ${completedGoals.size} completed",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.Flag,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                SlideInFromBottom(delayMs = 400) {
                    GradientFAB(
                        onClick = { showAddDialog = true },
                        icon = Icons.Default.Add,
                        gradient = GradientFab
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (activeGoals.isEmpty() && completedGoals.isEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 200) {
                            EmptyState(emoji = "🎯", title = "No goals yet", subtitle = "Set your first goal!")
                        }
                    }
                }

                if (activeGoals.isNotEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 100) {
                            StitchGoalSectionHeader(title = "Active Goals", count = activeGoals.size, color = Primary)
                        }
                    }
                    items(activeGoals, key = { it.id }) { goal ->
                        FadeInOnAppear(delayMs = 150) {
                            StitchGoalItem(
                                goal = goal,
                                onProgressChange = { viewModel.updateProgress(goal, it) },
                                onDelete = { viewModel.deleteGoal(goal) }
                            )
                        }
                    }
                }

                if (completedGoals.isNotEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 300) {
                            StitchGoalSectionHeader(title = "Completed", count = completedGoals.size, color = HabitGreen)
                        }
                    }
                    items(completedGoals, key = { it.id }) { goal ->
                        FadeInOnAppear(delayMs = 350) {
                            StitchGoalItem(
                                goal = goal,
                                onProgressChange = {},
                                onDelete = { viewModel.deleteGoal(goal) }
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showAddDialog) {
        StitchGoalDialog(
            newTitle = newTitle,
            newDesc = newDesc,
            newEmoji = newEmoji,
            newTargetDate = newTargetDate,
            onTitleChange = { newTitle = it },
            onDescChange = { newDesc = it },
            onEmojiChange = { newEmoji = it },
            onTargetDateChange = { newTargetDate = it },
            onDismiss = {
                showAddDialog = false; newTitle = ""; newDesc = ""; newEmoji = "🎯"; newTargetDate = ""
            },
            onConfirm = {
                if (newTitle.isNotBlank()) {
                    viewModel.addGoal(newTitle.trim(), newDesc.trim(), newEmoji, newTargetDate.trim())
                    showAddDialog = false; newTitle = ""; newDesc = ""; newEmoji = "🎯"; newTargetDate = ""
                }
            }
        )
    }
}

// ═══════════════════════════════════════════
// 📋 Section Header
// ═══════════════════════════════════════════

@Composable
private fun StitchGoalSectionHeader(
    title: String,
    count: Int,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color.copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                count.toString(),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
    }
}

// ═══════════════════════════════════════════
// 🃏 Stitch Goal Item
// ═══════════════════════════════════════════

@Composable
fun StitchGoalItem(
    goal: GoalEntity,
    onProgressChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceContainerLow.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(goal.emoji, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        goal.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (goal.description.isNotBlank()) {
                        Text(
                            goal.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (goal.targetDate.isNotBlank()) {
                        Text(
                            "Target: ${goal.targetDate}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Primary
                        )
                    }
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Delete, "Delete",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(goal.progress / 100f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (goal.progress >= 100) Brush.linearGradient(listOf(HabitGreen, HabitGreen.copy(alpha = 0.7f)))
                            else Brush.linearGradient(listOf(PrimaryContainer, SecondaryContainer))
                        )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${goal.progress}%",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (goal.progress >= 100) HabitGreen else Primary
                )
                if (goal.progress < 100) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(25, 50, 75, 100).forEach { pct ->
                            Surface(
                                onClick = { onProgressChange(pct) },
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    "$pct%",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        "Done!",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = HabitGreen
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════
// 💬 Stitch Goal Dialog
// ═══════════════════════════════════════════

@Composable
private fun StitchGoalDialog(
    newTitle: String,
    newDesc: String,
    newEmoji: String,
    newTargetDate: String,
    onTitleChange: (String) -> Unit,
    onDescChange: (String) -> Unit,
    onEmojiChange: (String) -> Unit,
    onTargetDateChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceContainer,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                "New Goal",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = newTitle, onValueChange = onTitleChange,
                    label = { Text("Goal title") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary
                    )
                )
                Text(
                    "Icon",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(goalEmojis) { emoji ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (newEmoji == emoji) Primary.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                                .then(
                                    if (newEmoji == emoji) Modifier.border(2.dp, Primary.copy(alpha = 0.5f), CircleShape)
                                    else Modifier
                                )
                                .clickable { onEmojiChange(emoji) },
                            contentAlignment = Alignment.Center
                        ) { Text(emoji, fontSize = 18.sp) }
                    }
                }
                OutlinedTextField(
                    value = newTargetDate, onValueChange = onTargetDateChange,
                    label = { Text("Target date (YYYY-MM-DD)") }, singleLine = true,
                    placeholder = { Text("2026-09-01") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary
                    )
                )
                OutlinedTextField(
                    value = newDesc, onValueChange = onDescChange,
                    label = { Text("Description (optional)") }, maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = newTitle.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer)
            ) {
                Text("Create Goal", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
