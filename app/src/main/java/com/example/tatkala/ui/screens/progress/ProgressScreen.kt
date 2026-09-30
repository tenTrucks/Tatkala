package com.example.tatkala.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TatakalaPurple = Color(0xFF6D49AE)
private val TatakalaDarkPurple = Color(0xFF4C258C)
private val TatakalaLime = Color(0xFFE7FCA7)
private val TatakalaBackground = Color(0xFFF8F8F8)
private val TatakalaWhite = Color(0xFFFFFFFF)
private val TatakalaMuted = Color(0xFF777777)
private val TatakalaLightPurple = Color(0xFFF0EAF8)
private val TatakalaLightGray = Color(0xFFE9E9E9)

private data class DailyProgress(val day: String, val value: Int)

private data class HabitProgress(
    val name: String,
    val completed: Int,
    val total: Int
)

private data class ActivityItem(
    val title: String,
    val time: String
)

private val weeklyActivity = listOf(
    DailyProgress("Mon", 4),
    DailyProgress("Tue", 6),
    DailyProgress("Wed", 3),
    DailyProgress("Thu", 7),
    DailyProgress("Fri", 5),
    DailyProgress("Sat", 2),
    DailyProgress("Sun", 4)
)

private val habits = listOf(
    HabitProgress("Workout", 5, 7),
    HabitProgress("Reading", 4, 7),
    HabitProgress("Study", 6, 7),
    HabitProgress("Drink Water", 5, 7)
)

private val recentActivities = listOf(
    ActivityItem("Completed Mobile Computing", "Today, 10:30 AM"),
    ActivityItem("Completed Workout", "Today, 07:15 AM"),
    ActivityItem("Completed Read 20 Minutes", "Yesterday, 09:20 PM")
)

@Composable
fun ProgressScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = TatakalaBackground
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ProgressHeader() }
            item { WeeklyGoalCard(5, 7) }
            item { ActivityChartCard(weeklyActivity) }
            item { HabitStreakCard() }
            item { HabitSummaryCard(habits) }
            item { RecentActivityCard(recentActivities) }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ProgressHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Progress",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TatakalaDarkPurple
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Track your productivity and habits",
            style = MaterialTheme.typography.bodyMedium,
            color = TatakalaMuted
        )
    }
}

@Composable
private fun WeeklyGoalCard(completedDays: Int, totalDays: Int) {
    ProgressCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Weekly Goal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TatakalaDarkPurple
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Keep your daily routine consistent",
                        style = MaterialTheme.typography.bodySmall,
                        color = TatakalaMuted
                    )
                }
                Text(
                    text = "$completedDays / $totalDays days",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TatakalaPurple
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { completedDays.toFloat() / totalDays },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50)),
                color = TatakalaPurple,
                trackColor = TatakalaLightPurple
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${((completedDays.toFloat() / totalDays) * 100).toInt()}% completed",
                style = MaterialTheme.typography.labelSmall,
                color = TatakalaMuted
            )
        }
    }
}

@Composable
private fun ActivityChartCard(activity: List<DailyProgress>) {
    ProgressCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Activity",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TatakalaDarkPurple
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Tasks completed this week",
                        style = MaterialTheme.typography.bodySmall,
                        color = TatakalaMuted
                    )
                }

                Text(
                    text = "${activity.sumOf { it.value }} tasks",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = TatakalaPurple
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            ActivityBarChart(activity)
        }
    }
}

@Composable
private fun ActivityBarChart(activity: List<DailyProgress>) {
    val maxValue = 7

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        activity.forEach { item ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = item.value.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = TatakalaDarkPurple
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.62f)
                        .height(110.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(item.value.toFloat() / maxValue)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp,
                                    bottomStart = 4.dp,
                                    bottomEnd = 4.dp
                                )
                            )
                            .background(
                                if (item.value == maxValue) {
                                    TatakalaDarkPurple
                                } else {
                                    TatakalaPurple
                                }
                            )
                    )
                }

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = item.day,
                    style = MaterialTheme.typography.labelSmall,
                    color = TatakalaMuted
                )
            }
        }
    }
}

@Composable
private fun HabitStreakCard() {
    val days = listOf(
        "M" to true,
        "T" to true,
        "W" to true,
        "T" to true,
        "F" to true,
        "S" to false,
        "S" to true
    )

    ProgressCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Habit Streak",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TatakalaDarkPurple
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "🔥 14 Day Streak",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TatakalaDarkPurple
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(TatakalaLime),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "14",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TatakalaDarkPurple
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEach { (day, completed) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    if (completed) TatakalaPurple else TatakalaLightGray
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (completed) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = TatakalaWhite
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = day,
                            style = MaterialTheme.typography.labelSmall,
                            color = TatakalaMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HabitSummaryCard(habits: List<HabitProgress>) {
    ProgressCard {
        Column {
            Text(
                text = "Habit Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TatakalaDarkPurple
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Your progress across daily habits",
                style = MaterialTheme.typography.bodySmall,
                color = TatakalaMuted
            )
            Spacer(modifier = Modifier.height(16.dp))

            habits.forEachIndexed { index, habit ->
                HabitRow(habit)
                if (index < habits.lastIndex) {
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}

@Composable
private fun HabitRow(habit: HabitProgress) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(TatakalaLightPurple),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (habit.name) {
                        "Workout" -> Icons.Default.FitnessCenter
                        "Reading" -> Icons.Default.MenuBook
                        "Study" -> Icons.Default.School
                        else -> Icons.Default.WaterDrop
                    },
                    contentDescription = null,
                    modifier = Modifier.size(19.dp),
                    tint = TatakalaPurple
                )
            }

            Spacer(modifier = Modifier.width(11.dp))

            Text(
                text = habit.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )

            Text(
                text = "${habit.completed}/${habit.total}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = TatakalaPurple
            )
        }

        Spacer(modifier = Modifier.height(7.dp))

        LinearProgressIndicator(
            progress = { habit.completed.toFloat() / habit.total },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
            color = TatakalaPurple,
            trackColor = TatakalaLightPurple
        )
    }
}

@Composable
private fun RecentActivityCard(activities: List<ActivityItem>) {
    ProgressCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Recent Activity",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TatakalaDarkPurple
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Your latest completed tasks",
                        style = MaterialTheme.typography.bodySmall,
                        color = TatakalaMuted
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = TatakalaPurple
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            activities.forEachIndexed { index, activity ->
                ActivityRow(activity)
                if (index < activities.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun ActivityRow(activity: ActivityItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(TatakalaLime),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = TatakalaDarkPurple
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = activity.time,
                style = MaterialTheme.typography.labelSmall,
                color = TatakalaMuted
            )
        }
    }
}

@Composable
private fun ProgressCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = TatakalaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            content()
        }
    }
}
