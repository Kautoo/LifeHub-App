package com.fizi.lifehub.ui.notes

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
import androidx.compose.material.icons.filled.Search
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
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel = hiltViewModel()) {
    val notes by viewModel.notes.collectAsState()
    val filteredNotes by viewModel.filteredNotes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }

    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    Box(modifier = Modifier.fillMaxSize()) {
        DecorativeBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                FadeInOnAppear(delayMs = 0) {
                    Box(modifier = Modifier.fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(Color(0xFF7C4DFF).copy(alpha = 0.08f), Color.Transparent)))
                        .statusBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Column {
                            Text("📝 Notes", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.ExtraBold)
                            Text("${notes.size} notes", style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = searchQuery, onValueChange = { viewModel.updateSearch(it) },
                                placeholder = { Text("Search notes...") },
                                leadingIcon = { Icon(Icons.Default.Search, "Search") },
                                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                SlideInFromBottom(delayMs = 400) {
                    GradientFAB(onClick = { showDialog = true }, icon = Icons.Default.Add, gradient = GradientPurple)
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (filteredNotes.isEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 200) {
                            EmptyState(
                                emoji = "📝",
                                title = if (searchQuery.isBlank()) "No notes yet" else "No results",
                                subtitle = if (searchQuery.isBlank()) "Tap + to write your first note" else "Try different keywords"
                            )
                        }
                    }
                }

                items(filteredNotes, key = { it.id }) { note ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { kotlinx.coroutines.delay(100); visible = true }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(400)) + slideInVertically(
                            spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow), initialOffsetY = { it / 5 }
                        )
                    ) {
                        GlassCard(modifier = Modifier.fillMaxWidth().pressEffect()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(note.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    IconButton(onClick = { viewModel.delete(note) }, modifier = Modifier.size(32.dp)) {
                                        Icon(Icons.Default.Delete, "Delete", modifier = Modifier.size(18.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(note.content, style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 3)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(dateFormat.format(Date(note.updatedAt)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; newTitle = ""; newContent = "" },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BouncyEmoji("📝", size = 28); Spacer(modifier = Modifier.width(8.dp))
                    Text("New Note", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = newTitle, onValueChange = { newTitle = it },
                        label = { Text("Title") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                    OutlinedTextField(value = newContent, onValueChange = { newContent = it },
                        label = { Text("Content") }, minLines = 4, maxLines = 8,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                }
            },
            confirmButton = {
                BouncyButton(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            viewModel.addNote(newTitle.trim(), newContent.trim())
                            showDialog = false; newTitle = ""; newContent = ""
                        }
                    },
                    enabled = newTitle.isNotBlank(), gradient = GradientPurple
                ) { Text("Save", color = Color.White, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showDialog = false; newTitle = ""; newContent = "" }) { Text("Cancel") } }
        )
    }
}
