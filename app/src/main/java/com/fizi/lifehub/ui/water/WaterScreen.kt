package com.fizi.lifehub.ui.water

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
            Text("💧 Water Tracker", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Stay hydrated!", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(40.dp))

            // Water Circle
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
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = WaterBlue
                    )
                    Text("glasses", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Add/Remove buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { viewModel.removeGlass() },
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    enabled = todayLog.glasses > 0
                ) { Text("−", fontSize = 28.sp) }

                Button(
                    onClick = { viewModel.addGlass() },
                    modifier = Modifier.size(100.dp).shadow(12.dp, CircleShape),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = WaterBlue)
                ) { Text("💧", fontSize = 36.sp) }

                OutlinedButton(
                    onClick = { viewModel.addGlass() },
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape
                ) { Text("+", fontSize = 28.sp) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (goalMet) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = HabitGreen.copy(alpha = 0.1f))
                ) {
                    Text(
                        "🎉 Daily goal reached!",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        fontWeight = FontWeight.Bold,
                        color = HabitGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Week history
            SectionHeader(title = "This Week", subtitle = "${weekLogs.count { it.glasses >= it.target }}/7 days goal met")
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
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
                                .background(MaterialTheme.colorScheme.surfaceVariant)
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            dayNames.getOrElse(index) { "" },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (index == todayIndex) Primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Tip: Aim for 8 glasses (2 liters) daily 💪",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
