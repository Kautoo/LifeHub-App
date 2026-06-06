package com.fizi.lifehub.ui.water

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WaterDrop
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

// ═══════════════════════════════════════════
// 💧 WaterScreen — Google Stitch Design
// ═══════════════════════════════════════════

@Composable
fun WaterScreen(viewModel: WaterViewModel = hiltViewModel()) {
    val todayLog by viewModel.todayLog.collectAsState()
    val weekLogs by viewModel.weekLogs.collectAsState()

    val progress = if (todayLog.target > 0) todayLog.glasses.toFloat() / todayLog.target else 0f
    val goalMet = todayLog.glasses >= todayLog.target

    // Wave animation
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = LinearEasing)),
        label = "wave"
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
                            "Water Tracker",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Stay hydrated!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(WaterBlue.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.WaterDrop,
                            contentDescription = null,
                            tint = WaterBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Water Circle ──
            FadeInOnAppear(delayMs = 100) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp)) {
                    CircularProgressIndicator(
                        progress = { progress.coerceAtMost(1f) },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 14.dp,
                        color = WaterBlue,
                        trackColor = WaterBlue.copy(alpha = 0.1f)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💧", fontSize = 40.sp)
                        Text(
                            "${todayLog.glasses}/${todayLog.target}",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold
                            ),
                            color = WaterBlue
                        )
                        Text(
                            "glasses",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Add/Remove buttons ──
            FadeInOnAppear(delayMs = 200) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { viewModel.removeGlass() },
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        enabled = todayLog.glasses > 0,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) { Text("−", fontSize = 28.sp) }

                    Button(
                        onClick = { viewModel.addGlass() },
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(12.dp, CircleShape),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        listOf(WaterBlue, WaterBlue.copy(alpha = 0.7f))
                                    ),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💧", fontSize = 36.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = { viewModel.addGlass() },
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) { Text("+", fontSize = 28.sp) }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (goalMet) {
                FadeInOnAppear(delayMs = 300) {
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
                        Text(
                            "Daily goal reached!",
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = HabitGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Week history ──
            FadeInOnAppear(delayMs = 400) {
                Column {
                    StitchWaterSectionHeader(
                        title = "This Week",
                        count = weekLogs.count { it.glasses >= it.target },
                        color = WaterBlue
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                Color.White.copy(alpha = 0.05f),
                                RoundedCornerShape(20.dp)
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SurfaceContainerLow.copy(alpha = 0.4f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            val todayIndex = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK) - 2

                            weekLogs.takeLast(7).forEachIndexed { index, log ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .width(32.dp)
                                            .height(80.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(
                                                    (log.glasses.toFloat() / log.target).coerceAtMost(1f)
                                                )
                                                .align(Alignment.BottomCenter)
                                                .background(
                                                    if (log.glasses >= log.target) WaterBlue
                                                    else WaterBlue.copy(alpha = 0.4f),
                                                    RoundedCornerShape(8.dp)
                                                )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        dayNames.getOrElse(index) { "" },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (index == todayIndex) Primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Tip: Aim for 8 glasses (2 liters) daily",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ═══════════════════════════════════════════
// 📋 Section Header
// ═══════════════════════════════════════════

@Composable
private fun StitchWaterSectionHeader(
    title: String,
    count: Int,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color.copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "$count/7",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
    }
}
