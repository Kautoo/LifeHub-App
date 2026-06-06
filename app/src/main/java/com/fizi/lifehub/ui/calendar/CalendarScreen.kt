package com.fizi.lifehub.ui.calendar

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Event
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
import com.fizi.lifehub.data.local.entity.EventEntity
import com.fizi.lifehub.ui.components.*
import com.fizi.lifehub.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

// ═══════════════════════════════════════════
// 📅 CalendarScreen — Google Stitch Design
// ═══════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: CalendarViewModel = hiltViewModel()) {
    val allEvents by viewModel.allEvents.collectAsState()
    val upcomingEvents by viewModel.upcomingEvents.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val eventsForDate by viewModel.eventsForDate.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var newDate by remember { mutableStateOf("") }
    var newTime by remember { mutableStateOf("") }
    var newIsAllDay by remember { mutableStateOf(false) }

    val calendar = remember { Calendar.getInstance() }
    val displayFormat = remember { SimpleDateFormat("MMM yyyy", Locale.getDefault()) }
    val dayFormat = remember { SimpleDateFormat("d", Locale.getDefault()) }
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val daysInMonth = remember {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        (1..maxDay).map { day ->
            cal.set(Calendar.DAY_OF_MONTH, day)
            dateFormat.format(cal.time)
        }
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
                            .background(
                                Brush.verticalGradient(
                                    listOf(Primary.copy(alpha = 0.06f), Color.Transparent)
                                )
                            )
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Calendar",
                                    style = MaterialTheme.typography.displaySmall.copy(
                                        fontFamily = SpaceGrotesk,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "${upcomingEvents.size} upcoming events",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.CalendarMonth,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                SlideInFromBottom(delayMs = 400) {
                    GradientFAB(
                        onClick = { showAddDialog = true },
                        icon = Icons.Default.Add,
                        gradient = GradientFab
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // ── Mini Calendar Card ──
                item {
                    FadeInOnAppear(delayMs = 100) {
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
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    displayFormat.format(calendar.time),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = SpaceGrotesk,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // Day headers
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                    listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                                        Text(
                                            day,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                val firstDayOffset = remember {
                                    val cal = Calendar.getInstance()
                                    cal.set(Calendar.DAY_OF_MONTH, 1)
                                    cal.get(Calendar.DAY_OF_WEEK) - 1
                                }
                                val allDays = List(firstDayOffset) { "" } + daysInMonth
                                allDays.chunked(7).forEach { week ->
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                        week.forEach { date ->
                                            val isSelected = date == selectedDate
                                            val hasEvent = allEvents.any { it.date == date }
                                            val isToday = date == dateFormat.format(Date())
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .aspectRatio(1f)
                                                    .padding(2.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        when {
                                                            isSelected -> PrimaryContainer
                                                            isToday -> Primary.copy(alpha = 0.1f)
                                                            else -> Color.Transparent
                                                        }
                                                    )
                                                    .clickable { if (date.isNotEmpty()) viewModel.selectDate(date) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(
                                                        date.takeIf { it.isNotEmpty() }?.let {
                                                            dateFormat.parse(it)?.let { d -> dayFormat.format(d) }
                                                        } ?: "",
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            fontFamily = PlusJakartaSans,
                                                            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                                                        ),
                                                        color = when {
                                                            isSelected -> Color.White
                                                            isToday -> Primary
                                                            else -> MaterialTheme.colorScheme.onSurface
                                                        }
                                                    )
                                                    if (hasEvent && date.isNotEmpty()) {
                                                        Box(
                                                            modifier = Modifier.size(4.dp)
                                                                .background(
                                                                    if (isSelected) Color.White else PrimaryContainer,
                                                                    CircleShape
                                                                )
                                                        )
                                                    } else {
                                                        Spacer(modifier = Modifier.height(4.dp))
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

                // ── Selected date events ──
                if (eventsForDate.isNotEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 200) {
                            StitchCalendarSectionHeader(
                                title = "Events on $selectedDate",
                                count = eventsForDate.size,
                                color = Primary
                            )
                        }
                    }
                    items(eventsForDate) { event ->
                        FadeInOnAppear(delayMs = 250) {
                            StitchEventItem(event) { viewModel.deleteEvent(event) }
                        }
                    }
                }

                // ── Upcoming events ──
                if (upcomingEvents.isNotEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 300) {
                            StitchCalendarSectionHeader(
                                title = "Upcoming",
                                count = upcomingEvents.size,
                                color = Secondary
                            )
                        }
                    }
                    items(upcomingEvents.take(5)) { event ->
                        val daysUntil = viewModel.getDaysUntil(event.date)
                        FadeInOnAppear(delayMs = 350) {
                            StitchEventItemWithCountdown(event, daysUntil) { viewModel.deleteEvent(event) }
                        }
                    }
                }

                if (allEvents.isEmpty()) {
                    item {
                        FadeInOnAppear(delayMs = 200) {
                            EmptyState(
                                emoji = "📅",
                                title = "No events yet",
                                subtitle = "Tap + to add your first event!"
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showAddDialog) {
        StitchCalendarDialog(
            newTitle = newTitle,
            newDesc = newDesc,
            newDate = newDate,
            newTime = newTime,
            newIsAllDay = newIsAllDay,
            onTitleChange = { newTitle = it },
            onDescChange = { newDesc = it },
            onDateChange = { newDate = it },
            onTimeChange = { newTime = it },
            onAllDayChange = { newIsAllDay = it },
            onDismiss = {
                showAddDialog = false; newTitle = ""; newDesc = ""; newDate = ""; newTime = ""; newIsAllDay = false
            },
            onConfirm = {
                if (newTitle.isNotBlank() && newDate.isNotBlank()) {
                    viewModel.addEvent(newTitle.trim(), newDesc.trim(), newDate.trim(), newTime.trim(), 0xFF00897B, newIsAllDay)
                    showAddDialog = false; newTitle = ""; newDesc = ""; newDate = ""; newTime = ""; newIsAllDay = false
                }
            },
            dateFormat = dateFormat
        )
    }
}

// ═══════════════════════════════════════════
// 📋 Section Header
// ═══════════════════════════════════════════

@Composable
private fun StitchCalendarSectionHeader(
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
                count.toString(),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
    }
}

// ═══════════════════════════════════════════
// 🃏 Stitch Event Item
// ═══════════════════════════════════════════

@Composable
fun StitchEventItem(event: EventEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceContainerLow.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp, 40.dp)
                    .background(PrimaryContainer, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    event.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (event.description.isNotBlank()) {
                    Text(
                        event.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    if (event.isAllDay) "All day" else event.time,
                    style = MaterialTheme.typography.labelMedium,
                    color = Primary
                )
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.Default.Delete, "Delete",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ═══════════════════════════════════════════
// 🃏 Stitch Event Item with Countdown
// ═══════════════════════════════════════════

@Composable
fun StitchEventItemWithCountdown(event: EventEntity, daysUntil: Long, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceContainerLow.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp, 40.dp)
                    .background(PrimaryContainer, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    event.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    event.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (daysUntil <= 3) TertiaryContainer.copy(alpha = 0.15f)
                    else PrimaryContainer.copy(alpha = 0.15f)
                )
            ) {
                Text(
                    if (daysUntil == 0L) "Today!" else if (daysUntil == 1L) "Tomorrow" else "${daysUntil}d",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (daysUntil <= 3) TertiaryContainer else Primary
                )
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.Default.Delete, "Delete",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ═══════════════════════════════════════════
// 💬 Stitch Calendar Dialog
// ═══════════════════════════════════════════

@Composable
private fun StitchCalendarDialog(
    newTitle: String,
    newDesc: String,
    newDate: String,
    newTime: String,
    newIsAllDay: Boolean,
    onTitleChange: (String) -> Unit,
    onDescChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    onAllDayChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    dateFormat: SimpleDateFormat
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceContainer,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                "New Event",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = newTitle, onValueChange = onTitleChange,
                    label = { Text("Event name") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary
                    )
                )
                OutlinedTextField(
                    value = newDate,
                    onValueChange = onDateChange,
                    label = { Text("Date (YYYY-MM-DD)") }, singleLine = true,
                    placeholder = { Text(dateFormat.format(Date())) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary
                    )
                )
                if (!newIsAllDay) {
                    OutlinedTextField(
                        value = newTime, onValueChange = onTimeChange,
                        label = { Text("Time (HH:MM)") }, singleLine = true,
                        placeholder = { Text("14:00") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            focusedLabelColor = Primary
                        )
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = newIsAllDay,
                        onCheckedChange = onAllDayChange,
                        colors = CheckboxDefaults.colors(checkedColor = Primary)
                    )
                    Text(
                        "All day event",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                OutlinedTextField(
                    value = newDesc, onValueChange = onDescChange,
                    label = { Text("Description (optional)") }, maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = newTitle.isNotBlank() && newDate.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer)
            ) {
                Text("Add Event", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
