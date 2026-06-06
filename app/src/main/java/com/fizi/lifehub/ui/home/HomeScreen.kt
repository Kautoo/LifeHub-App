package com.fizi.lifehub.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
            "Small steps every day!",
            "You're doing great!",
            "Stay focused, stay strong!",
            "Today is your day!",
            "Keep pushing forward!",
            "Progress, not perfection!"
        )
    }
    val todayQuote = remember { motivationalQuotes.random() }

    val totalTasks = stats.completedTasks + stats.pendingTasks
    val progressPercent = if (totalTasks > 0) (stats.completedTasks * 100 / totalTasks) else 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // ─── Header ───
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = greeting,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Let's make today productive.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ─── Today's Progress Card (Stitch style) ───
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.05f)
                            )
                        )
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "TODAY'S PROGRESS",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "You're on a ${stats.completedTasks}-day streak!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        // Streak badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(100.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🔥", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${stats.completedTasks}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Big percentage
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$progressPercent",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 64.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 64.sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "%",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "completed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color.Black.copy(alpha = 0.2f))
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(100.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction = progressPercent / 100f)
                                .clip(RoundedCornerShape(100.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }

        // ─── Stat Cards (Horizontal scroll) ───
        item {
            Spacer(modifier = Modifier.height(24.dp))
            val statItems = listOf(
                StatItem(Icons.Outlined.Schedule, "Pending Tasks", stats.pendingTasks.toString(), MaterialTheme.colorScheme.tertiaryContainer),
                StatItem(Icons.Outlined.CheckCircle, "Completed", stats.completedTasks.toString(), MaterialTheme.colorScheme.secondary),
                StatItem(Icons.Outlined.Refresh, "Habits", "${completedHabits}/${stats.totalHabits}", MaterialTheme.colorScheme.primary),
                StatItem(Icons.Outlined.AccountBalanceWallet, "Balance (RM)", "%.0f".format(stats.balance), MaterialTheme.colorScheme.primaryContainer),
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(statItems) { index, stat ->
                    StitchStatCard(stat = stat, delayMs = index * 80)
                }
            }
        }

        // ─── Quick Access Grid ───
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "QUICK ACCESS",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StitchQuickAccessCard(
                        emoji = "📝", title = "Todo List", subtitle = "${stats.pendingTasks} priorities today",
                        iconBg = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToTodo
                    )
                    StitchQuickAccessCard(
                        emoji = "📓", title = "Brain Dump", subtitle = null,
                        iconBg = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToNotes
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StitchQuickAccessCard(
                        emoji = "💰", title = "Budget", subtitle = null,
                        iconBg = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToBudget
                    )
                    StitchQuickAccessCard(
                        emoji = "🔄", title = "Habits", subtitle = null,
                        iconBg = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToHabits
                    )
                }
            }
        }

        // ─── Recent Tasks ───
        if (recentTasks.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT TASKS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "View All",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            itemsIndexed(recentTasks.take(5)) { index, task ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(200L + index * 60)
                    visible = true
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(400)) + slideInHorizontally(
                        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
                        initialOffsetX = { it / 4 }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.4f))
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                            .clickable { }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (task.isDone) MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (task.isDone) Icons.Outlined.CheckCircle else Icons.Outlined.Schedule,
                                contentDescription = null,
                                tint = if (task.isDone) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (task.description.isNotBlank()) {
                                Text(
                                    text = task.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // ─── Branding Card ───
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.08f)
                            )
                        )
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🏠", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "LifeHub",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Your life, organized. One hub at a time.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// ─── Helper composables ───

private data class StatItem(
    val icon: ImageVector,
    val label: String,
    val value: String,
    val iconTint: Color
)

@Composable
private fun StitchStatCard(stat: StatItem, delayMs: Int) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(200L + delayMs)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)) + scaleIn(
            spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
            initialScale = 0.8f
        )
    ) {
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.4f))
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(stat.iconTint.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = stat.icon,
                        contentDescription = null,
                        tint = stat.iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = stat.value,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stat.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StitchQuickAccessCard(
    emoji: String,
    title: String,
    subtitle: String?,
    iconBg: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.4f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
