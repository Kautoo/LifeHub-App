package com.fizi.lifehub.ui.pomodoro

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*

// ═══════════════════════════════════════════
// 🍅 PomodoroScreen — Google Stitch Design
// ═══════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel = hiltViewModel()) {
    val timerState by viewModel.timerState.collectAsState()
    val timeRemaining by viewModel.timeRemaining.collectAsState()
    val totalTime by viewModel.totalTime.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val sessionsToday by viewModel.sessionsToday.collectAsState()
    val totalMinutesToday by viewModel.totalMinutesToday.collectAsState()

    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val timeText = "%02d:%02d".format(minutes, seconds)

    val isBreak = timerState == TimerState.BREAK
    val accentColor = if (isBreak) HabitGreen else Primary

    // Pulse animation for running state
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (timerState == TimerState.RUNNING) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        DecorativeBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Header ──
            FadeInOnAppear(delayMs = 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            if (isBreak) "Break Time" else "Pomodoro",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            if (isBreak) "Relax and recharge" else "Stay focused",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(accentColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Timer,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Stats row ──
            FadeInOnAppear(delayMs = 100) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StitchStatPill("sessions", "$sessionsToday", "🍅", Primary)
                    StitchStatPill("minutes", "$totalMinutesToday min", "⏱️", Tertiary)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Timer Ring ──
            FadeInOnAppear(delayMs = 200) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(260.dp)
                        .graphicsLayer {
                            scaleX = pulseScale
                            scaleY = pulseScale
                        }
                ) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 14.dp,
                        color = accentColor,
                        trackColor = accentColor.copy(alpha = 0.1f)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            timeText,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 56.sp,
                                letterSpacing = 4.sp
                            ),
                            color = accentColor
                        )
                        Text(
                            when (timerState) {
                                TimerState.IDLE -> "Ready"
                                TimerState.RUNNING -> if (isBreak) "Relaxing..." else "Focusing..."
                                TimerState.PAUSED -> "Paused"
                                TimerState.BREAK -> "Break"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Control Buttons ──
            FadeInOnAppear(delayMs = 300) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Reset
                    if (timerState != TimerState.IDLE) {
                        OutlinedButton(
                            onClick = { viewModel.resetTimer() },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(56.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text("↺", fontSize = 24.sp)
                        }
                    }

                    // Main Button
                    Button(
                        onClick = {
                            when (timerState) {
                                TimerState.IDLE, TimerState.PAUSED -> viewModel.startTimer()
                                TimerState.RUNNING -> viewModel.pauseTimer()
                                TimerState.BREAK -> viewModel.resetTimer()
                            }
                        },
                        modifier = Modifier
                            .height(64.dp)
                            .widthIn(min = 180.dp)
                            .shadow(12.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    when (timerState) {
                                        TimerState.RUNNING -> Brush.linearGradient(
                                            listOf(Color(0xFFFF7043), Color(0xFFE64A19))
                                        )
                                        else -> if (isBreak)
                                            Brush.linearGradient(listOf(HabitGreen, HabitGreen.copy(alpha = 0.7f)))
                                        else
                                            Brush.linearGradient(listOf(PrimaryContainer, SecondaryContainer))
                                    },
                                    RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                when (timerState) {
                                    TimerState.IDLE -> "▶ Start"
                                    TimerState.RUNNING -> "⏸ Pause"
                                    TimerState.PAUSED -> "▶ Resume"
                                    TimerState.BREAK -> "↺ New Session"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Time Presets ──
            if (timerState == TimerState.IDLE) {
                FadeInOnAppear(delayMs = 400) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Quick Select",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = SurfaceContainerLow.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier.border(
                                1.dp,
                                Color.White.copy(alpha = 0.05f),
                                RoundedCornerShape(20.dp)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf(15, 25, 30, 45, 60).forEach { min ->
                                    val isSelected = totalTime == min * 60
                                    Surface(
                                        onClick = { viewModel.setWorkMinutes(min) },
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isSelected) Primary.copy(alpha = 0.2f) else Color.Transparent
                                    ) {
                                        Text(
                                            "${min}m",
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            ),
                                            color = if (isSelected) Primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════
// 📊 Stat Pill
// ═══════════════════════════════════════════

@Composable
private fun StitchStatPill(
    label: String,
    value: String,
    icon: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceContainerLow.copy(alpha = 0.4f)
        ),
        modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(icon, fontSize = 16.sp)
            Text(
                value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
