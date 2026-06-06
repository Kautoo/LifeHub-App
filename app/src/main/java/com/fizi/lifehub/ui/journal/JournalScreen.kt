package com.fizi.lifehub.ui.journal

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
import androidx.compose.material.icons.outlined.MenuBook
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

// ═══════════════════════════════════════════
// 📓 JournalScreen — Google Stitch Design
// ═══════════════════════════════════════════

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
                                    "Journal",
                                    style = MaterialTheme.typography.displaySmall.copy(
                                        fontFamily = SpaceGrotesk,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "$entryCount entries",
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
                                    Icons.Outlined.MenuBook,
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
                if (entries.isEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 200) {
                            EmptyState(
                                emoji = "📓",
                                title = "Start journaling",
                                subtitle = "Write your first entry!"
                            )
                        }
                    }
                }

                items(entries, key = { it.id }) { entry ->
                    FadeInOnAppear(delayMs = 150) {
                        StitchJournalEntryItem(entry) { viewModel.deleteEntry(entry) }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showAddDialog) {
        StitchJournalDialog(
            newContent = newContent,
            selectedMood = selectedMood,
            onContentChange = { newContent = it },
            onMoodChange = { selectedMood = it },
            onDismiss = {
                showAddDialog = false; newContent = ""; selectedMood = Mood.NEUTRAL
            },
            onConfirm = {
                if (newContent.isNotBlank()) {
                    viewModel.addEntry(newContent.trim(), selectedMood)
                    showAddDialog = false; newContent = ""; selectedMood = Mood.NEUTRAL
                }
            }
        )
    }
}

// ═══════════════════════════════════════════
// 🃏 Stitch Journal Entry Item
// ═══════════════════════════════════════════

@Composable
fun StitchJournalEntryItem(entry: JournalEntryEntity, onDelete: () -> Unit) {
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
                    Text(moodEmojis[entry.mood] ?: "😐", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        entry.mood.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        entry.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Delete, "Delete",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                entry.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ═══════════════════════════════════════════
// 💬 Stitch Journal Dialog
// ═══════════════════════════════════════════

@Composable
private fun StitchJournalDialog(
    newContent: String,
    selectedMood: Mood,
    onContentChange: (String) -> Unit,
    onMoodChange: (Mood) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceContainer,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                "New Entry",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "How are you feeling?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Mood.entries.forEach { mood ->
                        val isSelected = selectedMood == mood
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) Primary.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                                .then(
                                    if (isSelected) Modifier.border(2.dp, Primary.copy(alpha = 0.5f), CircleShape)
                                    else Modifier
                                )
                                .clickable { onMoodChange(mood) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(moodEmojis[mood] ?: "😐", fontSize = 22.sp)
                        }
                    }
                }
                OutlinedTextField(
                    value = newContent, onValueChange = onContentChange,
                    label = { Text("What's on your mind?") },
                    placeholder = { Text("Today I...") },
                    minLines = 4, maxLines = 8,
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
                enabled = newContent.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer)
            ) {
                Text("Save", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
