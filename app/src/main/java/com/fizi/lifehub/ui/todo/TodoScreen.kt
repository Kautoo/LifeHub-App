package com.fizi.lifehub.ui.todo

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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*

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

    Box(modifier = Modifier.fillMaxSize()) {
        DecorativeBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                FadeInOnAppear(delayMs = 0) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .background(Brush.verticalGradient(listOf(Primary.copy(alpha = 0.08f), Color.Transparent)))
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("✅ Tasks", style = MaterialTheme.typography.displayLarge,
                                    fontWeight = FontWeight.ExtraBold)
                                Text("${pendingTasks.size} pending • ${completedTasks.size} done",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (todos.isNotEmpty()) {
                                val progress = completedTasks.size.toFloat() / todos.size
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(56.dp)) {
                                    CircularProgressIndicator(
                                        progress = { progress }, modifier = Modifier.fillMaxSize(),
                                        strokeWidth = 5.dp, color = HabitGreen,
                                        trackColor = HabitGreen.copy(alpha = 0.1f)
                                    )
                                    Text("${(progress * 100).toInt()}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold, color = HabitGreen)
                                }
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                SlideInFromBottom(delayMs = 400) {
                    GradientFAB(onClick = { showDialog = true }, icon = Icons.Default.Add, gradient = GradientPrimary)
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (todos.isEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 200) {
                            EmptyState(emoji = "✨", title = "All clear!", subtitle = "Tap + to add your first task")
                        }
                    }
                }

                if (pendingTasks.isNotEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 100) {
                            SectionHeader(title = "To Do", subtitle = "${pendingTasks.size} tasks")
                        }
                    }
                    items(pendingTasks, key = { it.id }) { task ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) { kotlinx.coroutines.delay(150); visible = true }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(400)) + slideInHorizontally(
                                spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                                initialOffsetX = { it / 5 }
                            )
                        ) {
                            TaskCard(
                                title = task.title, description = task.description,
                                priority = task.priority, isCompleted = false,
                                onToggle = { viewModel.toggleDone(task) },
                                onDelete = { viewModel.delete(task) }
                            )
                        }
                    }
                }

                if (completedTasks.isNotEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 300) {
                            SectionHeader(title = "Completed", subtitle = "${completedTasks.size} done")
                        }
                    }
                    items(completedTasks, key = { it.id }) { task ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) { kotlinx.coroutines.delay(400); visible = true }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(300)) + slideInVertically(
                                spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                                initialOffsetY = { it / 6 }
                            )
                        ) {
                            TaskCard(
                                title = task.title, description = task.description,
                                priority = task.priority, isCompleted = true,
                                onToggle = { viewModel.toggleDone(task) },
                                onDelete = { viewModel.delete(task) }
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showDialog) {
        AddTaskDialog(
            title = newTitle, desc = newDesc, priority = newPriority,
            onTitleChange = { newTitle = it }, onDescChange = { newDesc = it },
            onPriorityChange = { newPriority = it },
            onDismiss = { showDialog = false; newTitle = ""; newDesc = ""; newPriority = 1 },
            onConfirm = {
                if (newTitle.isNotBlank()) {
                    viewModel.addTodo(newTitle.trim(), newDesc.trim(), newPriority)
                    showDialog = false; newTitle = ""; newDesc = ""; newPriority = 1
                }
            }
        )
    }
}

@Composable
fun TaskCard(
    title: String, description: String, priority: Int,
    isCompleted: Boolean, onToggle: () -> Unit, onDelete: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f, animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "taskScale"
    )
    val priorityColor = when (priority) {
        3 -> Error.copy(alpha = 0.8f); 2 -> Amber500; else -> HabitGreen
    }
    val priorityLabel = when (priority) {
        3 -> "🔴 High"; 2 -> "🟡 Medium"; else -> "🟢 Low"
    }

    Card(
        modifier = Modifier.fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(if (isCompleted) 2.dp else 6.dp, RoundedCornerShape(18.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f), spotColor = Color.Black.copy(alpha = 0.04f)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.width(4.dp).height(48.dp)
                .background(priorityColor, RoundedCornerShape(2.dp)))
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape)
                    .background(if (isCompleted) HabitGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(targetState = isCompleted,
                    transitionSpec = { scaleIn(spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)) togetherWith scaleOut(tween(150)) },
                    label = "check"
                ) { completed -> Text(if (completed) "✅" else "⬜", fontSize = 18.sp) }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
                    color = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface)
                if (description.isNotBlank()) {
                    Text(description, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else null)
                }
                Text(priorityLabel, style = MaterialTheme.typography.labelSmall, color = priorityColor)
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Delete, "Delete", modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun AddTaskDialog(
    title: String, desc: String, priority: Int,
    onTitleChange: (String) -> Unit, onDescChange: (String) -> Unit, onPriorityChange: (Int) -> Unit,
    onDismiss: () -> Unit, onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BouncyEmoji("✨", size = 28); Spacer(modifier = Modifier.width(8.dp))
                Text("New Task", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(value = title, onValueChange = onTitleChange,
                    label = { Text("What needs to be done?") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                OutlinedTextField(value = desc, onValueChange = onDescChange,
                    label = { Text("Details (optional)") }, maxLines = 3,
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                Text("Priority", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(1 to "🟢 Low", 2 to "🟡 Med", 3 to "🔴 High").forEach { (p, label) ->
                        val isSelected = priority == p
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { onPriorityChange(p) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(label, style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Primary else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        },
        confirmButton = {
            BouncyButton(onClick = onConfirm, enabled = title.isNotBlank(), gradient = GradientPrimary) {
                Text("Add Task", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
