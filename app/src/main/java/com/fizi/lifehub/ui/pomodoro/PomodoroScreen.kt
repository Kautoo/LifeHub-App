package com.fizi.lifehub.ui.pomodoro

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
    val gradient = if (isBreak) GradientForest else GradientPrimary

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
            // Header
            Text(
                if (isBreak) "☕ Break Time" else "🍅 Pomodoro",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Stats row
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatPill("sessions", "$sessionsToday", "🍅", Primary)
                StatPill("minutes", "$totalMinutesToday min", "⏱️", Tertiary)
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Timer Ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(260.dp)
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 12.dp,
                    color = if (isBreak) HabitGreen else Primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        timeText,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 56.sp,
                            letterSpacing = 4.sp
                        ),
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isBreak) HabitGreen else Primary
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

            Spacer(modifier = Modifier.height(48.dp))

            // Control Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset
                if (timerState != TimerState.IDLE) {
                    OutlinedButton(
                        onClick = { viewModel.resetTimer() },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(56.dp)
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (timerState) {
                            TimerState.RUNNING -> Color(0xFFFF7043)
                            else -> if (isBreak) HabitGreen else Primary
                        }
                    )
                ) {
                    Text(
                        when (timerState) {
                            TimerState.IDLE -> "▶ Start"
                            TimerState.RUNNING -> "⏸ Pause"
                            TimerState.PAUSED -> "▶ Resume"
                            TimerState.BREAK -> "↺ New Session"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Time Presets
            if (timerState == TimerState.IDLE) {
                Text(
                    "Quick Select",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(15, 25, 30, 45, 60).forEach { min ->
                        FilterChip(
                            selected = totalTime == min * 60,
                            onClick = { viewModel.setWorkMinutes(min) },
                            label = { Text("${min}m") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }
    }
}
