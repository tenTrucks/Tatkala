package com.example.tatkala.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.tatkala.data.local.entity.HabitEntity
import com.example.tatkala.data.local.entity.HabitLogEntity
import com.example.tatkala.data.local.entity.TaskEntity
import com.example.tatkala.data.repository.HabitRepository
import com.example.tatkala.data.repository.StreakCalculator
import com.example.tatkala.data.repository.TaskRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

private enum class ActivityRange { WEEK, MONTH }
private data class DailyProgress(val day: String, val value: Int)
private data class HabitProgress(val habit: HabitEntity, val completed: Int, val streak: Int)
private data class ActivityItem(val title: String, val date: String)

@Composable
fun ProgressScreen() {
    val tasks by TaskRepository.allTasksState().collectAsState()
    val habits by HabitRepository.observeHabits().collectAsState(initial = emptyList())
    val logs by HabitRepository.observeCompletedLogs().collectAsState(initial = emptyList())
    val today = LocalDate.now()
    var activityRange by remember { mutableStateOf(ActivityRange.WEEK) }
    var activityAnchor by remember { mutableStateOf(today) }
    val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val weekDates = (0..6).map { weekStart.plusDays(it.toLong()) }
    val chartData = buildActivityData(
        tasks = tasks,
        logs = logs,
        range = activityRange,
        anchor = activityAnchor
    )
    val chartTitle = when (activityRange) {
        ActivityRange.WEEK -> {
            val start = activityAnchor.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            "${start.format(DateTimeFormatter.ofPattern("d MMM"))} - ${start.plusDays(6).format(DateTimeFormatter.ofPattern("d MMM yyyy"))}"
        }
        ActivityRange.MONTH -> YearMonth.from(activityAnchor).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH))
    }

    val completedTasksThisWeek = tasks.filter {
        it.isCompleted && runCatching { LocalDate.parse(it.date) }.getOrNull() in weekDates
    }
    val completedLogsThisWeek = logs.filter {
        runCatching { LocalDate.parse(it.date) }.getOrNull() in weekDates
    }
    val weeklyGoal = (tasks.count { runCatching { LocalDate.parse(it.date) }.getOrNull() in weekDates } +
        habits.sumOf { it.targetPerWeek }).coerceAtLeast(1)
    val completedActivities = completedTasksThisWeek.size + completedLogsThisWeek.size
    val habitProgress = habits.map { habit ->
        val habitLogs = logs.filter { it.habitId == habit.id && it.completed }
        HabitProgress(
            habit = habit,
            completed = habitLogs.count { runCatching { LocalDate.parse(it.date) }.getOrNull() in weekDates },
            streak = StreakCalculator.currentStreak(habitLogs.mapNotNull { runCatching { LocalDate.parse(it.date) }.getOrNull() }.toSet(), today)
        )
    }
    val bestStreak = habitProgress.maxByOrNull { it.streak }
    val recent = buildList {
        addAll(completedTasksThisWeek.map { ActivityItem("Completed task: ${it.title}", it.date) })
        addAll(logs.sortedByDescending { it.completedAt ?: 0L }.take(8).map { log ->
            val name = habits.firstOrNull { it.id == log.habitId }?.name ?: "Habit"
            ActivityItem("Completed habit: $name", log.date)
        })
    }.sortedByDescending { it.date }.take(5)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { ProgressHeader() }
        item { WeeklyGoalCard(completedActivities, weeklyGoal) }
        item {
            ActivityChartCard(
                activity = chartData,
                range = activityRange,
                title = chartTitle,
                onRangeChanged = {
                    activityRange = it
                    activityAnchor = today
                },
                onPrevious = {
                    activityAnchor = when (activityRange) {
                        ActivityRange.WEEK -> activityAnchor.minusWeeks(1)
                        ActivityRange.MONTH -> activityAnchor.minusMonths(1)
                    }
                },
                onNext = {
                    activityAnchor = when (activityRange) {
                        ActivityRange.WEEK -> activityAnchor.plusWeeks(1)
                        ActivityRange.MONTH -> activityAnchor.plusMonths(1)
                    }
                }
            )
        }
        item { HabitStreakCard(bestStreak) }
        item { HabitSummaryCard(habitProgress) }
        item { RecentActivityCard(recent) }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun ProgressHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Progress",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Actual tasks, habits, and streaks from local data",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun WeeklyGoalCard(completed: Int, goal: Int) {
    ProgressCard {
        val percent = (completed.toFloat() / goal).coerceIn(0f, 1f)
        Text("Weekly Goal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("$completed completed • ${(goal - completed).coerceAtLeast(0)} remaining", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(14.dp))
        LinearProgressIndicator(
            progress = { percent },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("${(percent * 100).toInt()}% completed", style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun ActivityChartCard(
    activity: List<DailyProgress>,
    range: ActivityRange,
    title: String,
    onRangeChanged: (ActivityRange) -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    ProgressCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Daily Activity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    text = if (range == ActivityRange.WEEK) "Completed activities this week" else "Completed activities this month",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text("${activity.sumOf { it.value }} total", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPrevious) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous range")
            }
            Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            IconButton(onClick = onNext) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next range")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ActivityRange.entries.forEach {
                FilterChip(
                    selected = range == it,
                    onClick = { onRangeChanged(it) },
                    label = { Text(it.name.lowercase().replaceFirstChar { c -> c.titlecase() }) }
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        ActivityBarChart(activity)
    }
}

@Composable
private fun ActivityBarChart(activity: List<DailyProgress>) {
    val maxValue = activity.maxOfOrNull { it.value }?.coerceAtLeast(1) ?: 1
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        activity.forEach { item ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(item.value.toString(), style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.62f)
                        .height(102.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(item.value.toFloat() / maxValue)
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
                Spacer(modifier = Modifier.height(7.dp))
                Text(item.day, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun HabitStreakCard(best: HabitProgress?) {
    ProgressCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Habit Streak", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    text = if (best == null) "No streak yet" else "${best.habit.name}: ${best.streak} day streak",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Text((best?.streak ?: 0).toString(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}

@Composable
private fun HabitSummaryCard(habits: List<HabitProgress>) {
    ProgressCard {
        Text("Habit Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Weekly completion by habit", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        if (habits.isEmpty()) {
            Text("No habit yet. Add a habit from the Add tab.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            habits.forEachIndexed { index, habit ->
                HabitRow(habit)
                if (index < habits.lastIndex) Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
private fun HabitRow(progress: HabitProgress) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        progress.habit.name.contains("work", ignoreCase = true) -> Icons.Default.FitnessCenter
                        progress.habit.name.contains("read", ignoreCase = true) -> Icons.Default.MenuBook
                        progress.habit.name.contains("study", ignoreCase = true) -> Icons.Default.School
                        else -> Icons.Default.WaterDrop
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.size(11.dp))
            Text(
                text = progress.habit.name,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${progress.completed}/${progress.habit.targetPerWeek}",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(7.dp))
        LinearProgressIndicator(
            progress = { (progress.completed.toFloat() / progress.habit.targetPerWeek).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun RecentActivityCard(activities: List<ActivityItem>) {
    ProgressCard {
        Text("Recent Activity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(14.dp))
        if (activities.isEmpty()) {
            Text("No recent activity yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            activities.forEachIndexed { index, activity ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.size(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(activity.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(activity.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                if (index < activities.lastIndex) Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun ProgressCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp), content = content)
    }
}

private fun buildActivityData(
    tasks: List<TaskEntity>,
    logs: List<HabitLogEntity>,
    range: ActivityRange,
    anchor: LocalDate
): List<DailyProgress> {
    return when (range) {
        ActivityRange.WEEK -> {
            val weekStart = anchor.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            (0..6).map { offset ->
                val date = weekStart.plusDays(offset.toLong())
                DailyProgress(
                    day = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).take(3),
                    value = tasks.count { it.isCompleted && it.date == date.toString() } +
                        logs.count { it.date == date.toString() && it.completed }
                )
            }
        }
        ActivityRange.MONTH -> {
            val month = YearMonth.from(anchor)
            val first = month.atDay(1)
            val last = month.atEndOfMonth()
            generateSequence(first.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))) {
                it.plusWeeks(1)
            }
                .takeWhile { it <= last }
                .mapIndexed { index, start ->
                    val dates = (0..6).map { start.plusDays(it.toLong()) }
                        .filter { YearMonth.from(it) == month }
                    DailyProgress(
                        day = "W${index + 1}",
                        value = dates.sumOf { date ->
                            tasks.count { it.isCompleted && it.date == date.toString() } +
                                logs.count { it.date == date.toString() && it.completed }
                        }
                    )
                }
                .toList()
        }
    }
}
