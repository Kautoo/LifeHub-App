package com.fizi.lifehub.ui.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*

data class MoreMenuItem(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val gradient: Brush,
    val onClick: () -> Unit
)

@Composable
fun MoreScreen(
    onNavigateToBudget: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToJournal: () -> Unit = {},
    onNavigateToGoals: () -> Unit = {},
    onNavigateToWater: () -> Unit = {},
    onNavigateToPomodoro: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val menuItems = listOf(
        MoreMenuItem("🍅", "Pomodoro", "Focus timer", GradientPrimary, onNavigateToPomodoro),
        MoreMenuItem("📅", "Calendar", "Events & countdown", GradientOcean, onNavigateToCalendar),
        MoreMenuItem("📓", "Journal", "Daily reflection", GradientPurple, onNavigateToJournal),
        MoreMenuItem("🎯", "Goals", "Track progress", GradientSunset, onNavigateToGoals),
        MoreMenuItem("💧", "Water", "Stay hydrated", Brush.linearGradient(listOf(WaterBlue, WaterBlue.copy(alpha = 0.7f))), onNavigateToWater),
        MoreMenuItem("💰", "Budget", "Income & expenses", GradientForest, onNavigateToBudget),
        MoreMenuItem("⚙️", "Settings", "App preferences", Brush.linearGradient(listOf(Color(0xFF78909C), Color(0xFF546E7A))), onNavigateToSettings),
    )

    Box(modifier = Modifier.fillMaxSize()) {
        DecorativeBackground()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 4.dp, vertical = 16.dp)
                ) {
                    Text(
                        "More",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "All features at a glance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(menuItems.size) { index ->
                val item = menuItems[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .shadow(6.dp, RoundedCornerShape(20.dp))
                        .clickable { item.onClick() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(item.gradient)
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(item.emoji, fontSize = 32.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    item.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Text("→", color = Color.White.copy(alpha = 0.6f), fontSize = 20.sp)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(120.dp)) }
        }
    }
}
