package com.fizi.lifehub.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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

    // Generate days for current month
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF0277BD).copy(alpha = 0.06f), Color.Transparent)
                            )
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        "📅 Calendar",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "${upcomingEvents.size} upcoming events",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            floatingActionButton = {
                GradientFAB(
                    onClick = { showAddDialog = true },
                    icon = Icons.Default.Add,
                    gradient = GradientOcean
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Mini Calendar
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                displayFormat.format(calendar.time),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            // Day headers
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                                    Text(day, style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight(1f))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            // Days grid
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
                                                        isSelected -> Primary
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
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
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
                                                                if (isSelected) Color.White else Primary,
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

                // Selected date events
                if (eventsForDate.isNotEmpty()) {
                    item {
                        SectionHeader(title = "Events on $selectedDate", subtitle = "${eventsForDate.size} events")
                    }
                    items(eventsForDate) { event ->
                        EventItem(event) { viewModel.deleteEvent(event) }
                    }
                }

                // Upcoming events
                if (upcomingEvents.isNotEmpty()) {
                    item {
                        SectionHeader(title = "Upcoming", subtitle = "Next ${upcomingEvents.size} events")
                    }
                    items(upcomingEvents.take(5)) { event ->
                        val daysUntil = viewModel.getDaysUntil(event.date)
                        EventItemWithCountdown(event, daysUntil) { viewModel.deleteEvent(event) }
                    }
                }

                if (allEvents.isEmpty()) {
                    item {
                        EmptyState(
                            emoji = "📅",
                            title = "No events yet",
                            subtitle = "Tap + to add your first event!"
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false; newTitle = ""; newDesc = ""; newDate = ""; newTime = "" },
            title = { Text("📅 New Event", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newTitle, onValueChange = { newTitle = it },
                        label = { Text("Event name") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = newDate,
                        onValueChange = { newDate = it },
                        label = { Text("Date (YYYY-MM-DD)") }, singleLine = true,
                        placeholder = { Text(dateFormat.format(Date())) },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                    if (!newIsAllDay) {
                        OutlinedTextField(
                            value = newTime, onValueChange = { newTime = it },
                            label = { Text("Time (HH:MM)") }, singleLine = true,
                            placeholder = { Text("14:00") },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = newIsAllDay, onCheckedChange = { newIsAllDay = it })
                        Text("All day event")
                    }
                    OutlinedTextField(
                        value = newDesc, onValueChange = { newDesc = it },
                        label = { Text("Description (optional)") }, maxLines = 2,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank() && newDate.isNotBlank()) {
                            viewModel.addEvent(newTitle.trim(), newDesc.trim(), newDate.trim(), newTime.trim(), 0xFF00897B, newIsAllDay)
                            showAddDialog = false; newTitle = ""; newDesc = ""; newDate = ""; newTime = ""; newIsAllDay = false
                        }
                    },
                    enabled = newTitle.isNotBlank() && newDate.isNotBlank(),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Add Event") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false; newTitle = ""; newDesc = ""; newDate = ""; newTime = ""; newIsAllDay = false
                }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun EventItem(event: EventEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(8.dp, 40.dp)
                    .background(Color(event.color), RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(event.title, fontWeight = FontWeight.SemiBold)
                if (event.description.isNotBlank()) {
                    Text(event.description, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(
                    if (event.isAllDay) "All day" else event.time,
                    style = MaterialTheme.typography.labelSmall,
                    color = Primary
                )
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Delete, "Delete", modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun EventItemWithCountdown(event: EventEntity, daysUntil: Long, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(8.dp, 40.dp)
                    .background(Color(event.color), RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(event.title, fontWeight = FontWeight.SemiBold)
                Text(event.date, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (daysUntil <= 3) Error.copy(alpha = 0.1f)
                    else Primary.copy(alpha = 0.1f)
                )
            ) {
                Text(
                    if (daysUntil == 0L) "Today!" else if (daysUntil == 1L) "Tomorrow" else "${daysUntil}d",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (daysUntil <= 3) Error else Primary
                )
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Delete, "Delete", modifier = Modifier.size(16.dp))
            }
        }
    }
}
