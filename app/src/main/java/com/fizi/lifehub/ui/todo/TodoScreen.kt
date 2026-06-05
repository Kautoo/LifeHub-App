package com.fizi.lifehub.ui.todo

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.data.local.entity.TodoEntity
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel = hiltViewModel()) {
    val todos by viewModel.todos.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableIntStateOf(1) }

    val pendingTodos = todos.filter { !it.isDone }
    val completedTodos = todos.filter { it.isDone }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Primary.copy(alpha = 0.08f), Color.Transparent)
                            )
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        "My Tasks",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatPill(
                            label = "pending",
                            value = "${pendingTodos.size}",
                            icon = "📋",
                            color = Primary
                        )
                        if (completedTodos.isNotEmpty()) {
                            StatPill(
                                label = "done",
                                value = "${completedTodos.size}",
                                icon = "✅",
                                color = IncomeColor
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                GradientFAB(
                    onClick = { showAddDialog = true },
                    icon = Icons.Default.Add,
                    gradient = GradientPrimary
                )
            }
        ) { padding ->
            if (todos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(
                        emoji = "📝",
                        title = "No tasks yet!",
                        subtitle = "Tap + to add your first task"
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (pendingTodos.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "In Progress",
                                subtitle = "${pendingTodos.size} tasks"
                            )
                        }
                        items(pendingTodos, key = { it.id }) { todo ->
                            ModernTodoItem(
                                todo = todo,
                                onToggle = { viewModel.toggleDone(todo) },
                                onDelete = { viewModel.delete(todo) }
                            )
                        }
                    }

                    if (completedTodos.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            SectionHeader(
                                title = "Completed",
                                subtitle = "${completedTodos.size} tasks",
                                action = {
                                    TextButton(onClick = { viewModel.clearCompleted() }) {
                                        Text("Clear All", color = Error)
                                    }
                                }
                            )
                        }
                        items(completedTodos, key = { it.id }) { todo ->
                            ModernTodoItem(
                                todo = todo,
                                onToggle = { viewModel.toggleDone(todo) },
                                onDelete = { viewModel.delete(todo) }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }
            }
        }
    }

    // Add Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false; newTitle = ""; newDescription = ""; selectedPriority = 1
            },
            title = {
                Text("✨ New Task", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("What needs to be done?") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = newDescription,
                        onValueChange = { newDescription = it },
                        label = { Text("Details (optional)") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    Text("Priority", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(1 to "Low", 2 to "Med", 3 to "High").forEach { (value, label) ->
                            FilterChip(
                                selected = selectedPriority == value,
                                onClick = { selectedPriority = value },
                                label = { Text(label) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = when (value) {
                                        1 -> IncomeColor.copy(alpha = 0.15f)
                                        2 -> Tertiary.copy(alpha = 0.15f)
                                        else -> Error.copy(alpha = 0.15f)
                                    }
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            viewModel.addTodo(newTitle.trim(), newDescription.trim(), selectedPriority)
                            showAddDialog = false; newTitle = ""; newDescription = ""; selectedPriority = 1
                        }
                    },
                    enabled = newTitle.isNotBlank(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) { Text("Add Task") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false; newTitle = ""; newDescription = ""; selectedPriority = 1
                }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun ModernTodoItem(todo: TodoEntity, onToggle: () -> Unit, onDelete: () -> Unit) {
    val priorityColor = when (todo.priority) { 3 -> Error; 2 -> Tertiary; else -> IncomeColor }
    val scale by animateFloatAsState(
        targetValue = if (todo.isDone) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow), label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (todo.isDone) 0.dp else 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = priorityColor.copy(alpha = 0.08f),
                spotColor = priorityColor.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (todo.isDone)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Priority bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .background(priorityColor, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (todo.isDone) FontWeight.Normal else FontWeight.SemiBold,
                    textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
                    color = if (todo.isDone) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                if (todo.description.isNotBlank()) {
                    Text(
                        text = todo.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
