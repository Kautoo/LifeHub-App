package com.fizi.lifehub.ui.todo

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

// ═══════════════════════════════════════════
// ✅ TodoScreen — Google Stitch Design
// ═══════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel = hiltViewModel()) {
    val todos by viewModel.todos.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var newPriority by remember { mutableIntStateOf(1) }

    val pendingTasks = todos.filter { !it.isDone }
    val completedTasks = todos.filter { it.isDone }
    val completionProgress = if (todos.isNotEmpty()) {
        completedTasks.size.toFloat() / todos.size
    } else 0f

    Box(modifier = Modifier.fillMaxSize()) {
        DecorativeBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                // ── Stitch-style Header ──
                FadeInOnAppear(delayMs = 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Primary.copy(alpha = 0.06f),
                                        Color.Transparent
                                    )
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
                                // Title — Space Grotesk Bold (displaySmall)
                                Text(
                                    "Tasks",
                                    style = MaterialTheme.typography.displaySmall.copy(
                                        fontFamily = SpaceGrotesk,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                // Subtitle
                                Text(
                                    "${pendingTasks.size} tasks remaining today",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // ── Circular Progress Indicator ──
                            if (todos.isNotEmpty()) {
                                StitchProgressRing(
                                    progress = completionProgress,
                                    size = 56.dp,
                                    strokeWidth = 5.dp
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                // ── Stitch Gradient FAB ──
                SlideInFromBottom(delayMs = 400) {
                    GradientFAB(
                        onClick = { showDialog = true },
                        icon = Icons.Default.Add,
                        gradient = GradientFab
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // ── Empty State ──
                if (todos.isEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 200) {
                            EmptyState(
                                emoji = "✨",
                                title = "All clear!",
                                subtitle = "Tap + to add your first task"
                            )
                        }
                    }
                }

                // ── To Do Section ──
                if (pendingTasks.isNotEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 100) {
                            StitchSectionHeader(
                                title = "To Do",
                                count = pendingTasks.size,
                                color = Primary
                            )
                        }
                    }
                    items(pendingTasks, key = { it.id }) { task ->
                        StitchTaskCard(
                            title = task.title,
                            description = task.description,
                            priority = task.priority,
                            createdAt = task.createdAt,
                            isCompleted = false,
                            onToggle = { viewModel.toggleDone(task) },
                            onDelete = { viewModel.delete(task) }
                        )
                    }
                }

                // ── Completed Section ──
                if (completedTasks.isNotEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 300) {
                            StitchSectionHeader(
                                title = "Completed",
                                count = completedTasks.size,
                                color = HabitGreen
                            )
                        }
                    }
                    items(completedTasks, key = { it.id }) { task ->
                        StitchTaskCard(
                            title = task.title,
                            description = task.description,
                            priority = task.priority,
                            createdAt = task.createdAt,
                            isCompleted = true,
                            onToggle = { viewModel.toggleDone(task) },
                            onDelete = { viewModel.delete(task) }
                        )
                    }
                }

                // Bottom spacer for FAB
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    // ── Add Task Dialog ──
    if (showDialog) {
        StitchAddTaskDialog(
            title = newTitle,
            desc = newDesc,
            priority = newPriority,
            onTitleChange = { newTitle = it },
            onDescChange = { newDesc = it },
            onPriorityChange = { newPriority = it },
            onDismiss = {
                showDialog = false
                newTitle = ""
                newDesc = ""
                newPriority = 1
            },
            onConfirm = {
                if (newTitle.isNotBlank()) {
                    viewModel.addTodo(newTitle.trim(), newDesc.trim(), newPriority)
                    showDialog = false
                    newTitle = ""
                    newDesc = ""
                    newPriority = 1
                }
            }
        )
    }
}

// ═══════════════════════════════════════════
// 📊 Stitch Progress Ring
// ═══════════════════════════════════════════

@Composable
private fun StitchProgressRing(
    progress: Float,
    size: androidx.compose.ui.unit.Dp = 56.dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 5.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800, easing = EaseInOutCubic),
        label = "progressAnim"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = strokeWidth,
            color = Primary,
            trackColor = Primary.copy(alpha = 0.12f)
        )
        Text(
            "${(animatedProgress * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            color = Primary
        )
    }
}

// ═══════════════════════════════════════════
// 📋 Stitch Section Header
// ═══════════════════════════════════════════

@Composable
private fun StitchSectionHeader(
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
        // Count badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color.copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                count.toString(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = color
            )
        }
    }
}

// ═══════════════════════════════════════════
// 🃏 Stitch Task Card
// ═══════════════════════════════════════════

@Composable
private fun StitchTaskCard(
    title: String,
    description: String,
    priority: Int,
    createdAt: Long,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(if (isCompleted) 400L else 150L)
        visible = true
    }

    // Priority config
    val (priorityColor, priorityLabel) = when (priority) {
        3 -> Error.copy(alpha = 0.9f) to "High"
        2 -> HabitStreak to "Medium"
        else -> HabitGreen to "Low"
    }

    // Time formatting
    val timeText = remember(createdAt) {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        sdf.format(Date(createdAt))
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)) + slideInHorizontally(
            spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
            initialOffsetX = { it / 5 }
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isCompleted) 2.dp else 6.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color.Black.copy(alpha = 0.04f),
                    spotColor = Color.Black.copy(alpha = 0.04f)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isCompleted)
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                else
                    MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                // ── Checkbox ──
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            if (isCompleted) HabitGreen.copy(alpha = 0.15f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { onToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = isCompleted,
                        transitionSpec = {
                            scaleIn(
                                spring(
                                    Spring.DampingRatioMediumBouncy,
                                    Spring.StiffnessLow
                                )
                            ) togetherWith scaleOut(tween(150))
                        },
                        label = "check"
                    ) { completed ->
                        Icon(
                            imageVector = if (completed) Icons.Outlined.CheckCircle
                            else Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = if (completed) "Completed" else "Mark complete",
                            modifier = Modifier.size(20.dp),
                            tint = if (completed) HabitGreen
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // ── Content ──
                Column(modifier = Modifier.weight(1f)) {
                    // Title row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = if (isCompleted)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else
                                MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (isCompleted) TextDecoration.LineThrough
                            else null,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        // Time
                        Text(
                            timeText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }

                    // Description
                    if (description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = if (isCompleted) TextDecoration.LineThrough
                            else null
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ── Bottom row: Priority chip + Delete ──
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Priority chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(priorityColor.copy(alpha = 0.12f))
                                .border(
                                    width = 1.dp,
                                    color = priorityColor.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                priorityLabel,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = priorityColor
                            )
                        }

                        // Delete button (subtle)
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete task",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════
// ➕ Stitch Add Task Dialog
// ═══════════════════════════════════════════

@Composable
private fun StitchAddTaskDialog(
    title: String,
    desc: String,
    priority: Int,
    onTitleChange: (String) -> Unit,
    onDescChange: (String) -> Unit,
    onPriorityChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BouncyEmoji("✨", size = 28)
                Text(
                    "New Task",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Title field
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("What needs to be done?") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary,
                        cursorColor = Primary
                    )
                )

                // Description field
                OutlinedTextField(
                    value = desc,
                    onValueChange = onDescChange,
                    label = { Text("Details (optional)") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary,
                        cursorColor = Primary
                    )
                )

                // ── Priority Selector ──
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Priority",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            Triple(1, "Low", HabitGreen),
                            Triple(2, "Medium", HabitStreak),
                            Triple(3, "High", Error.copy(alpha = 0.9f))
                        ).forEach { (p, label, color) ->
                            val isSelected = priority == p
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) color.copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    )
                                    .then(
                                        if (isSelected) Modifier.border(
                                            width = 1.5.dp,
                                            color = color.copy(alpha = 0.4f),
                                            shape = RoundedCornerShape(10.dp)
                                        ) else Modifier
                                    )
                                    .clickable { onPriorityChange(p) }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    label,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold
                                        else FontWeight.Normal
                                    ),
                                    color = if (isSelected) color
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            BouncyButton(
                onClick = onConfirm,
                enabled = title.isNotBlank(),
                gradient = GradientFab
            ) {
                Text(
                    "Add Task",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancel",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}
