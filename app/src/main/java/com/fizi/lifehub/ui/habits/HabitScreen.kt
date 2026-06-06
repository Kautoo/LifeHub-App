package com.fizi.lifehub.ui.habits

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.data.local.entity.HabitFrequency
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(viewModel: HabitViewModel = hiltViewModel()) {
    val habits by viewModel.habits.collectAsState()
    val completionStates by viewModel.completionStates.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newIcon by remember { mutableStateOf("💪") }
    var newFrequency by remember { mutableStateOf(HabitFrequency.DAILY) }

    val completedToday = habits.count { completionStates[it.id] == true }
    val totalHabits = habits.size
    val progress = if (totalHabits > 0) completedToday.toFloat() / totalHabits else 0f

    val habitIcons = listOf("💪", "📚", "🏃", "💧", "🧘", "🎵", "✍️", "🌅", "💤", "🍎", "🎯", "🙏")

    Box(modifier = Modifier.fillMaxSize()) {
        DecorativeBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                FadeInOnAppear(delayMs = 0) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .background(Brush.verticalGradient(listOf(HabitGreen.copy(alpha = 0.08f), Color.Transparent)))
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("🎯 Habits", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.ExtraBold)
                                Text("$completedToday of $totalHabits today",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(60.dp)) {
                                CircularProgressIndicator(
                                    progress = { progress }, modifier = Modifier.fillMaxSize(),
                                    strokeWidth = 6.dp, color = HabitGreen, trackColor = HabitGreen.copy(alpha = 0.1f)
                                )
                                Text("${(progress * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold, color = HabitGreen)
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                SlideInFromBottom(delayMs = 400) {
                    GradientFAB(onClick = { showDialog = true }, icon = Icons.Default.Add,
                        gradient = Brush.linearGradient(listOf(HabitGreen, Color(0xFF00C853))))
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (habits.isEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 200) {
                            EmptyState(emoji = "🎯", title = "No habits yet", subtitle = "Start building good habits!")
                        }
                    }
                }

                if (habits.isNotEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 100) {
                            GradientGlassCard(
                                modifier = Modifier.fillMaxWidth().pressEffect(),
                                gradient = Brush.linearGradient(listOf(HabitGreen.copy(alpha = 0.9f), Color(0xFF00C853).copy(alpha = 0.7f)))
                            ) {
                                Row(modifier = Modifier.fillMaxWidth().padding(20.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        BouncyEmoji("🎯", size = 32)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("$completedToday", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                        Text("Done", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
                                    }
                                    Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color.White.copy(alpha = 0.3f)))
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        BouncyEmoji("📋", size = 32)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("$totalHabits", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                        Text("Total", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
                                    }
                                    Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color.White.copy(alpha = 0.3f)))
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        val msg = when { progress >= 1.0f -> "🏆"; progress >= 0.5f -> "💪"; else -> "🌱" }
                                        BouncyEmoji(msg, size = 32)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(when { progress >= 1.0f -> "Perfect!"; progress >= 0.5f -> "Great!"; else -> "Start!" },
                                            style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }

                items(habits, key = { it.id }) { habit ->
                    val isCompleted = completionStates[habit.id] == true
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { kotlinx.coroutines.delay(200); visible = true }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(400)) + slideInHorizontally(
                            spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow), initialOffsetX = { it / 5 }
                        )
                    ) {
                        var isPressed by remember { mutableStateOf(false) }
                        val scale by animateFloatAsState(
                            targetValue = if (isPressed) 0.96f else 1f,
                            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium), label = "habitScale"
                        )
                        GlassCard(
                            modifier = Modifier.fillMaxWidth()
                                .graphicsLayer { scaleX = scale; scaleY = scale }
                                .clickable { isPressed = true; viewModel.toggleToday(habit.id) }
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(48.dp).clip(CircleShape)
                                    .background(if (isCompleted) HabitGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AnimatedContent(targetState = isCompleted,
                                        transitionSpec = { scaleIn(spring(Spring.DampingRatioMediumBouncy)) togetherWith scaleOut(tween(150)) },
                                        label = "habitCheck"
                                    ) { completed -> Text(if (completed) "✅" else habit.icon, fontSize = 24.sp) }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(habit.name, fontWeight = FontWeight.SemiBold,
                                        color = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface)
                                    Text(habit.frequency.name.lowercase().replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                IconButton(onClick = { viewModel.delete(habit) }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.Delete, "Delete", modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                        LaunchedEffect(isPressed) { if (isPressed) { kotlinx.coroutines.delay(150); isPressed = false } }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; newName = ""; newIcon = "💪"; newFrequency = HabitFrequency.DAILY },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BouncyEmoji("🎯", size = 28); Spacer(modifier = Modifier.width(8.dp))
                    Text("New Habit", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(value = newName, onValueChange = { newName = it },
                        label = { Text("Habit name") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                    Text("Icon", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        habitIcons.take(6).forEach { icon ->
                            Box(modifier = Modifier.size(40.dp).clip(CircleShape)
                                .background(if (newIcon == icon) Primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { newIcon = icon },
                                contentAlignment = Alignment.Center
                            ) { Text(icon, fontSize = 20.sp) }
                        }
                    }
                    Text("Frequency", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HabitFrequency.entries.forEach { freq ->
                            val isSelected = newFrequency == freq
                            Box(modifier = Modifier.clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { newFrequency = freq }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(freq.name.lowercase().replaceFirstChar { it.uppercase() },
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Primary else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                BouncyButton(
                    onClick = {
                        if (newName.isNotBlank()) {
                            viewModel.addHabit(newName.trim(), newIcon, newFrequency)
                            showDialog = false; newName = ""; newIcon = "💪"; newFrequency = HabitFrequency.DAILY
                        }
                    },
                    enabled = newName.isNotBlank(),
                    gradient = Brush.linearGradient(listOf(HabitGreen, Color(0xFF00C853)))
                ) { Text("Create", color = Color.White, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; newName = ""; newIcon = "💪"; newFrequency = HabitFrequency.DAILY }) { Text("Cancel") }
            }
        )
    }
}
