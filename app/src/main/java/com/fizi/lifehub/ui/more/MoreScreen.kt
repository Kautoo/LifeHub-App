package com.fizi.lifehub.ui.more

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*

// ═══════════════════════════════════════════
// 📱 MoreScreen — Google Stitch Design
// ═══════════════════════════════════════════

data class MoreMenuItem(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color,
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
        MoreMenuItem("🍅", "Pomodoro", "Focus timer", Icons.Outlined.Timer, Primary, onNavigateToPomodoro),
        MoreMenuItem("📅", "Calendar", "Events & countdown", Icons.Outlined.CalendarMonth, Secondary, onNavigateToCalendar),
        MoreMenuItem("📓", "Journal", "Daily reflection", Icons.Outlined.MenuBook, Primary, onNavigateToJournal),
        MoreMenuItem("🎯", "Goals", "Track progress", Icons.Outlined.Flag, HabitGreen, onNavigateToGoals),
        MoreMenuItem("💧", "Water", "Stay hydrated", Icons.Outlined.WaterDrop, WaterBlue, onNavigateToWater),
        MoreMenuItem("💰", "Budget", "Income & expenses", Icons.Outlined.AccountBalanceWallet, IncomeColor, onNavigateToBudget),
        MoreMenuItem("⚙️", "Settings", "App preferences", Icons.Outlined.Settings, Outline, onNavigateToSettings),
    )

    Box(modifier = Modifier.fillMaxSize()) {
        DecorativeBackground()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                FadeInOnAppear(delayMs = 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 4.dp, vertical = 16.dp)
                    ) {
                        Text(
                            "More",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "All features at a glance",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            items(menuItems.size) { index ->
                val item = menuItems[index]
                FadeInOnAppear(delayMs = 100 + index * 80) {
                    StitchMoreMenuItem(item)
                }
            }

            item { Spacer(modifier = Modifier.height(120.dp)) }
        }
    }
}

// ═══════════════════════════════════════════
// 🃏 Stitch Menu Item
// ═══════════════════════════════════════════

@Composable
private fun StitchMoreMenuItem(item: MoreMenuItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .clickable { item.onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceContainerLow.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon container with accent
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(item.accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(item.emoji, fontSize = 22.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
