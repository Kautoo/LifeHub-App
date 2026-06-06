package com.fizi.lifehub.ui.journal

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
import com.fizi.lifehub.data.local.entity.JournalEntryEntity
import com.fizi.lifehub.data.local.entity.Mood
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*

private val moodEmojis = mapOf(
    Mood.HAPPY to "😊", Mood.NEUTRAL to "😐", Mood.SAD to "😢",
    Mood.ANGRY to "😠", Mood.EXCITED to "🤩"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(viewModel: JournalViewModel = hiltViewModel()) {
    val entries by viewModel.entries.collectAsState()
    val entryCount by viewModel.entryCount.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newContent by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf(Mood.NEUTRAL) }

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
                                listOf(Color(0xFF7C4DFF).copy(alpha = 0.06f), Color.Transparent)
                            )
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        "📓 Journal",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "$entryCount entries",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            floatingActionButton = {
                GradientFAB(
                    onClick = { showAddDialog = true },
                    icon = Icons.Default.Add,
                    gradient = GradientPurple
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (entries.isEmpty()) {
                    item {
                        EmptyState(
                            emoji = "📓",
                            title = "Start journaling",
                            subtitle = "Write your first entry!"
                        )
                    }
                }

                items(entries, key = { it.id }) { entry ->
                    JournalEntryItem(entry) { viewModel.deleteEntry(entry) }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false; newContent = ""; selectedMood = Mood.NEUTRAL },
            title = { Text("📝 New Entry", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("How are you feeling?", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Mood.entries.forEach { mood ->
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (selectedMood == mood) Primary.copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .clickable { selectedMood = mood },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(moodEmojis[mood] ?: "😐", fontSize = 24.sp)
                            }
                        }
                    }
                    OutlinedTextField(
                        value = newContent, onValueChange = { newContent = it },
                        label = { Text("What's on your mind?") },
                        placeholder = { Text("Today I...") },
                        minLines = 4, maxLines = 8,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newContent.isNotBlank()) {
                            viewModel.addEntry(newContent.trim(), selectedMood)
                            showAddDialog = false; newContent = ""; selectedMood = Mood.NEUTRAL
                        }
                    },
                    enabled = newContent.isNotBlank(),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false; newContent = ""; selectedMood = Mood.NEUTRAL
                }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun JournalEntryItem(entry: JournalEntryEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(moodEmojis[entry.mood] ?: "😐", fontSize = 28.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(entry.date, style = MaterialTheme.typography.labelMedium, color = Primary)
                    Text(
                        entry.mood.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Delete, "Delete", modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(entry.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
