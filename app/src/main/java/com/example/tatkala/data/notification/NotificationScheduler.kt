package com.example.tatkala.data.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.tatkala.data.local.entity.HabitEntity
import com.example.tatkala.data.local.entity.TaskEntity
import com.example.tatkala.data.repository.SettingsRepository
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

object NotificationScheduler {

    fun scheduleTaskReminder(context: Context, task: TaskEntity) {
        val settings = SettingsRepository.notifications.value
        val uniqueName = "task-${task.id}"
        if (!settings.master || !settings.taskReminder) {
            WorkManager.getInstance(context.applicationContext).cancelUniqueWork(uniqueName)
            return
        }

        val reminderAt = runCatching {
            LocalDateTime.of(LocalDate.parse(task.date), LocalTime.parse(task.startTime))
        }.getOrNull() ?: return

        enqueue(
            context = context,
            uniqueName = uniqueName,
            title = "Task: ${task.title}",
            message = "${task.startTime} • ${task.durationMinutes} min • ${task.category}",
            triggerAt = reminderAt,
            notificationId = task.id.hashCodeId()
        )
    }

    fun scheduleHabitReminder(context: Context, habit: HabitEntity) {
        val settings = SettingsRepository.notifications.value
        val uniqueName = "habit-${habit.id}"
        if (!settings.master || !settings.habitReminder || habit.reminderTime.isNullOrBlank()) {
            WorkManager.getInstance(context.applicationContext).cancelUniqueWork(uniqueName)
            return
        }

        val time = runCatching { LocalTime.parse(habit.reminderTime) }.getOrNull() ?: return
        val now = LocalDateTime.now()
        val today = LocalDateTime.of(LocalDate.now(), time)
        val triggerAt = if (today.isAfter(now)) today else today.plusDays(1)

        enqueueDaily(
            context = context,
            uniqueName = uniqueName,
            title = "Habit: ${habit.name}",
            message = "Swipe right or mark complete after you finish it.",
            triggerAt = triggerAt,
            notificationId = habit.id.hashCodeId()
        )
    }

    private fun enqueue(
        context: Context,
        uniqueName: String,
        title: String,
        message: String,
        triggerAt: LocalDateTime,
        notificationId: Int
    ) {
        val delayMillis = Duration.between(LocalDateTime.now(), triggerAt).toMillis()
        if (delayMillis <= 0) return

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    ReminderWorker.KEY_TITLE to title,
                    ReminderWorker.KEY_MESSAGE to message,
                    ReminderWorker.KEY_NOTIFICATION_ID to notificationId
                )
            )
            .build()

        WorkManager.getInstance(context.applicationContext)
            .enqueueUniqueWork(uniqueName, ExistingWorkPolicy.REPLACE, request)
    }

    private fun enqueueDaily(
        context: Context,
        uniqueName: String,
        title: String,
        message: String,
        triggerAt: LocalDateTime,
        notificationId: Int
    ) {
        val delayMillis = Duration.between(LocalDateTime.now(), triggerAt).toMillis()
        if (delayMillis <= 0) return

        val request = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    ReminderWorker.KEY_TITLE to title,
                    ReminderWorker.KEY_MESSAGE to message,
                    ReminderWorker.KEY_NOTIFICATION_ID to notificationId
                )
            )
            .build()

        WorkManager.getInstance(context.applicationContext)
            .enqueueUniquePeriodicWork(uniqueName, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    private fun Long.hashCodeId(): Int = hashCode().absoluteValue.coerceAtLeast(1)
}
