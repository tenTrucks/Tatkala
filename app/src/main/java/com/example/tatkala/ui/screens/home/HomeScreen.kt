package com.example.tatkala.ui.screens.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.tatkala.data.local.entity.TaskEntity
import com.example.tatkala.data.repository.TaskRepository
import com.example.tatkala.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

private enum class CalendarMode { TODAY, WEEK, MONTH }

@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onEditTask: (Long) -> Unit
) {
    val tasks by TaskRepository.allTasksState().collectAsState()
    val profile by UserRepository.profile.collectAsState()
    var mode by remember { mutableStateOf(CalendarMode.TODAY) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTask by remember { mutableStateOf<TaskEntity?>(null) }
    val tasksForSelectedDate = tasks
        .filter { it.date == selectedDate.toString() }
        .sortedWith(compareBy<TaskEntity> { it.startTime }.thenBy { it.createdAt })

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add schedule")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HomeHeader(
                    displayName = profile.displayName,
                    date = selectedDate
                )
            }
            item {
                CalendarSwitcher(
                    selected = mode,
                    onSelected = { mode = it }
                )
            }
            item {
                when (mode) {
                    CalendarMode.TODAY -> TodayPicker(
                        selectedDate = selectedDate,
                        onToday = { selectedDate = LocalDate.now() }
                    )
                    CalendarMode.WEEK -> WeekCalendar(
                        selectedDate = selectedDate,
                        tasks = tasks,
                        onSelectedDate = { selectedDate = it }
                    )
                    CalendarMode.MONTH -> MonthCalendar(
                        selectedDate = selectedDate,
                        tasks = tasks,
                        onSelectedDate = { selectedDate = it }
                    )
                }
            }
            item {
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH)),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            if (tasksForSelectedDate.isEmpty()) {
                item {
                    EmptyState(
                        title = "No task on this date",
                        message = "Use Add to create a schedule or collaborative task."
                    )
                }
            } else {
                items(tasksForSelectedDate, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onClick = { selectedTask = task }
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }

    selectedTask?.let { task ->
        TaskDetailDialog(
            task = task,
            onDismiss = { selectedTask = null },
            onEdit = {
                selectedTask = null
                onEditTask(task.id)
            }
        )
    }
}

@Composable
private fun HomeHeader(displayName: String, date: LocalDate) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Good day, ${displayName.ifBlank { "Tatakala User" }}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CalendarSwitcher(
    selected: CalendarMode,
    onSelected: (CalendarMode) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CalendarMode.entries.forEach { mode ->
            FilterChip(
                selected = selected == mode,
                onClick = { onSelected(mode) },
                label = { Text(mode.name.lowercase().replaceFirstChar { it.titlecase() }) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TodayPicker(
    selectedDate: LocalDate,
    onToday: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = if (selectedDate == LocalDate.now()) "Today" else "Selected date",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        OutlinedButton(onClick = onToday) {
            Text("Jump to today")
        }
    }
}

@Composable
private fun WeekCalendar(
    selectedDate: LocalDate,
    tasks: List<TaskEntity>,
    onSelectedDate: (LocalDate) -> Unit
) {
    val weekStart = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val days = (0..6).map { weekStart.plusDays(it.toLong()) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        CalendarPagerHeader(
            title = "${weekStart.format(DateTimeFormatter.ofPattern("d MMM"))} - ${weekStart.plusDays(6).format(DateTimeFormatter.ofPattern("d MMM yyyy"))}",
            onPrevious = { onSelectedDate(selectedDate.minusWeeks(1)) },
            onNext = { onSelectedDate(selectedDate.plusWeeks(1)) }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            days.forEach { day ->
                DateBubble(
                    date = day,
                    selected = day == selectedDate,
                    hasTask = tasks.any { it.date == day.toString() },
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectedDate(day) }
                )
            }
        }
    }
}

@Composable
private fun MonthCalendar(
    selectedDate: LocalDate,
    tasks: List<TaskEntity>,
    onSelectedDate: (LocalDate) -> Unit
) {
    val month = YearMonth.from(selectedDate)
    val first = month.atDay(1)
    val offset = first.dayOfWeek.value - 1
    val cells = List(offset) { null } + (1..month.lengthOfMonth()).map { month.atDay(it) }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CalendarPagerHeader(
            title = month.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)),
            onPrevious = { onSelectedDate(selectedDate.minusMonths(1).withDayOfMonth(1)) },
            onNext = { onSelectedDate(selectedDate.plusMonths(1).withDayOfMonth(1)) }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        cells.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                (week + List(7 - week.size) { null }).forEach { date ->
                    if (date == null) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        DateBubble(
                            date = date,
                            selected = date == selectedDate,
                            hasTask = tasks.any { it.date == date.toString() },
                            modifier = Modifier.weight(1f),
                            onClick = { onSelectedDate(date) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarPagerHeader(
    title: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = onPrevious) { Text("Prev") }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onNext) { Text("Next") }
    }
}

@Composable
private fun DateBubble(
    date: LocalDate,
    selected: Boolean,
    hasTask: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val isToday = date == LocalDate.now()
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .background(
                when {
                    selected -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).take(3),
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(if (hasTask) MaterialTheme.colorScheme.secondary else Color.Transparent)
        )
    }
}

@Composable
private fun TaskCard(task: TaskEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${task.startTime} • ${task.durationMinutes} min • ${task.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (task.isCollaborative) {
                    Spacer(modifier = Modifier.height(8.dp))
                    AssistChip(onClick = {}, label = { Text("Collaboration") })
                }
            }
            Icon(
                imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.Edit,
                contentDescription = if (task.isCompleted) "Completed" else "Open task",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun EmptyState(title: String, message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun TaskDetailDialog(
    task: TaskEntity,
    onDismiss: () -> Unit,
    onEdit: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var confirmDelete by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(task.title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("${task.date} at ${task.startTime} • ${task.durationMinutes} min")
                Text("Category: ${task.category}")
                Text(if (task.isCollaborative) "Collaborative: ${task.collaborators.ifBlank { "No collaborator listed" }}" else "Personal task")
                if (task.description.isNotBlank()) Text("Notes: ${task.description}")
                if (task.meetingLink.isNotBlank()) {
                    TextButton(onClick = { openUri(context, task.meetingLink) }) {
                        Icon(Icons.Default.Link, contentDescription = null)
                        Text("Open meeting link")
                    }
                }
                if (task.location.isNotBlank()) {
                    TextButton(onClick = { openLocation(context, task.location) }) {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                        Text("Open location")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    scope.launch {
                        TaskRepository.setCompleted(task.id, !task.isCompleted)
                        onDismiss()
                    }
                }
            ) {
                Text(if (task.isCompleted) "Mark incomplete" else "Mark complete")
            }
        },
        dismissButton = {
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit task")
                }
                IconButton(onClick = { confirmDelete = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete task")
                }
            }
        }
    )

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete task?") },
            text = { Text("This removes the schedule from local storage.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            TaskRepository.deleteTask(task)
                            confirmDelete = false
                            onDismiss()
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("Cancel") }
            }
        )
    }
}

private fun openUri(context: android.content.Context, raw: String) {
    val uri = if (raw.startsWith("http://") || raw.startsWith("https://")) raw else "https://$raw"
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    }.recoverCatching {
        if (it is ActivityNotFoundException) Unit else throw it
    }
}

private fun openLocation(context: android.content.Context, raw: String) {
    val uri = if (raw.startsWith("http://") || raw.startsWith("geo:")) {
        Uri.parse(raw)
    } else {
        Uri.parse("geo:0,0?q=${Uri.encode(raw)}")
    }
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}
