package com.fizi.lifehub.ui.notes

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel = hiltViewModel()) {
    val notes by viewModel.notes.collectAsState()
    val filteredNotes by viewModel.filteredNotes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }

    val dateFormat = remember { SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault()) }
    val noteColorMap = remember {
        listOf(
            Color(0xFF8781FF), Color(0xFF00D2FF), Color(0xFFF16161),
            Color(0xFFFFB3B0), Color(0xFF4ADE80), Color(0xFFFFB300),
            Color(0xFFA5E7FF), Color(0xFF918FA1)
        )
    }

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
                            "📝 Notes",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "${notes.size} notes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        // Stitch-style search bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.updateSearch(it) },
                            placeholder = { Text("Search notes...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search, "Search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(28.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = SurfaceContainerHigh,
                                unfocusedContainerColor = SurfaceContainerHigh,
                                focusedBorderColor = Primary.copy(alpha = 0.5f),
                                unfocusedBorderColor = Color.Transparent
                            )
                        )
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    val colorIndex = (note.color.coerceIn(0, noteColorMap.size - 1))
                    val noteColor = noteColorMap[colorIndex]

                    SwipeToDeleteNote(
                        onDelete = { viewModel.delete(note) }
                    ) {
                        FadeInOnAppear(delayMs = 100) {
                            NoteCard(
                                title = note.title,
                                content = note.content,
                                time = dateFormat.format(Date(note.updatedAt)),
                                noteColor = noteColor
                            )
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
                    BouncyEmoji("📝", size = 28)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Note", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newTitle, onValueChange = { newTitle = it },
                        label = { Text("Title") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = newContent, onValueChange = { newContent = it },
                        label = { Text("Content") }, minLines = 4, maxLines = 8,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
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

// ─── Stitch Note Card ───
@Composable
private fun NoteCard(
    title: String,
    content: String,
    time: String,
    noteColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceContainerHigh
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    // Color dot indicator
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(noteColor)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
                Text(
                    time,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ─── Swipe-to-delete wrapper ───
@Composable
private fun SwipeToDeleteNote(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    var offsetX by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = with(density) { 120.dp.toPx() }
    val deleteThreshold = with(density) { 200.dp.toPx() }

    Box(modifier = Modifier.fillMaxWidth()) {
        // Delete background revealed on swipe
        AnimatedVisibility(
            visible = offsetX < -20f,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
                    .background(ExpenseColor.copy(alpha = 0.15f))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete, "Delete",
                    tint = ExpenseColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX < -deleteThreshold) {
                                onDelete()
                            }
                            offsetX = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX = (offsetX + dragAmount).coerceIn(-deleteThreshold, 0f)
                        }
                    )
                }
        ) {
            content()
        }
    }
}
