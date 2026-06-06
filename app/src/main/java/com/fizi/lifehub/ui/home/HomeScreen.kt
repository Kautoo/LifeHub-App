package com.fizi.lifehub.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*
import java.util.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToTodo: () -> Unit = {},
    onNavigateToNotes: () -> Unit = {},
    onNavigateToBudget: () -> Unit = {},
    onNavigateToHabits: () -> Unit = {}
) {
    val stats by viewModel.stats.collectAsState()
    val recentTasks by viewModel.recentTasks.collectAsState()
    val completedHabits by viewModel.completedHabitsToday.collectAsState()

    val greeting = remember {
        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 5..11 -> "Good Morning ☀️"
            in 12..16 -> "Good Afternoon 🌤️"
            in 17..20 -> "Good Evening 🌅"
            else -> "Good Night 🌙"
        }
    }

    val motivationalQuotes = remember {
        listOf(
            "Small steps every day! 💪",
            "You're doing great! ✨",
            "Stay focused, stay strong! 🎯",
            "Today is your day! 🚀",
            "Keep pushing forward! 🔥",
            "Progress, not perfection! 🌟"
        )
    }
    val todayQuote = remember { motivationalQuotes.random() }

    Box(modifier = Modifier.fillMaxSize()) {
        DecorativeBackground()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // ─── Header ───
            item {
                FadeInOnAppear(delayMs = 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Primary.copy(alpha = 0.08f),
                                        Secondary.copy(alpha = 0.04f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = greeting,
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "Fizi",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .shadow(8.dp, CircleShape)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(listOf(Primary, Secondary))),
                                    contentAlignment = Alignment.Center
                                ) { Text("😎", fontSize = 26.sp) }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Primary.copy(alpha = 0.08f))
                            ) {
                                Text(
                                    text = todayQuote,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = Primary
                                )
                            }
                        }
                    }
                }
            }

            // ─── Quick Stats ───
            item {
                FadeInOnAppear(delayMs = 100) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(
                            listOf(
                                Triple("📋", "Pending", stats.pendingTasks.toString()),
                                Triple("✅", "Done", stats.completedTasks.toString()),
                                Triple("🎯", "Habits", stats.totalHabits.toString()),
                                Triple("💰", "Balance", "RM ${"%.0f".format(stats.balance)}")
                            )
                        ) { index, (emoji, label, value) ->
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) {
                                kotlinx.coroutines.delay(200L + index * 80)
                                visible = true
                            }
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn(tween(400)) + scaleIn(
                                    spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                                    initialScale = 0.7f
                                )
                            ) {
                                GlassCard(modifier = Modifier.width(100.dp).pressEffect()) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        BouncyEmoji(emoji, size = 28, delayMs = 300 + index * 80)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(value, style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.ExtraBold, color = Primary)
                                        Text(label, style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ─── Quick Access ───
            item {
                FadeInOnAppear(delayMs = 300) {
                    SectionHeader(title = "Quick Access", subtitle = "Jump right in")
                }
            }

            item {
                FadeInOnAppear(delayMs = 400) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(
                            listOf(
                                Triple("✅", "Todo", GradientPrimary) to onNavigateToTodo,
                                Triple("📝", "Notes", GradientPurple) to onNavigateToNotes,
                                Triple("💰", "Budget", GradientForest) to onNavigateToBudget,
                                Triple("🎯", "Habits", GradientSunset) to onNavigateToHabits
                            )
                        ) { index, (triple, onClick) ->
                            val (emoji, title, gradient) = triple
                            GradientGlassCard(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(120.dp)
                                    .pressEffect()
                                    .clickable { onClick() },
                                gradient = gradient
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        BouncyEmoji(emoji, size = 36, delayMs = 500 + index * 100)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(title, fontWeight = FontWeight.Bold,
                                            color = Color.White, fontSize = 15.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ─── Recent Tasks ───
            if (recentTasks.isNotEmpty()) {
                item {
                    FadeInOnAppear(delayMs = 500) {
                        SectionHeader(title = "Recent Tasks", subtitle = "${recentTasks.size} tasks")
                    }
                }

                itemsIndexed(recentTasks.take(5)) { index, task ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(600L + index * 80)
                        visible = true
                    }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(400)) + slideInHorizontally(
                            spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                            initialOffsetX = { it / 4 }
                        )
                    ) {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .pressEffect()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(36.dp).clip(CircleShape)
                                        .background(
                                            if (task.isDone) HabitGreen.copy(alpha = 0.15f)
                                            else Primary.copy(alpha = 0.1f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) { Text(if (task.isDone) "✅" else "⬜", fontSize = 18.sp) }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(task.title, fontWeight = FontWeight.SemiBold, maxLines = 1)
                                    if (task.description.isNotBlank()) {
                                        Text(task.description, style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ─── Progress Card ───
            item {
                FadeInOnAppear(delayMs = 700) {
                    SectionHeader(title = "Today's Progress", subtitle = "Keep it up!")
                }
            }

            item {
                FadeInOnAppear(delayMs = 800) {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).pressEffect(),
                        gradient = Brush.linearGradient(
                            listOf(Primary.copy(alpha = 0.9f), Secondary.copy(alpha = 0.7f))
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                BouncyEmoji("🎯", size = 32, delayMs = 900)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("$completedHabits", fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Text("Habits", style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f))
                            }
                            Box(modifier = Modifier.width(1.dp).height(50.dp)
                                .background(Color.White.copy(alpha = 0.3f)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                BouncyEmoji("✅", size = 32, delayMs = 1000)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${stats.completedTasks}", fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Text("Tasks", style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f))
                            }
                            Box(modifier = Modifier.width(1.dp).height(50.dp)
                                .background(Color.White.copy(alpha = 0.3f)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                BouncyEmoji("📋", size = 32, delayMs = 1100)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${stats.pendingTasks}", fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Text("Pending", style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f))
                            }
                        }
                    }
                }
            }

            // ─── Branding ───
            item {
                Spacer(modifier = Modifier.height(16.dp))
                FadeInOnAppear(delayMs = 1000) {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).pressEffect(),
                        gradient = Brush.linearGradient(listOf(Color(0xFF7C4DFF), Color(0xFFE040FB)))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                BouncyEmoji("🏠", size = 40, delayMs = 1100)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("LifeHub", fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Text("Your life, organized. One hub at a time. 💪",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
