package com.example.tatkala.data.repository

import android.content.Context
import com.example.tatkala.data.local.database.TatkalaDatabase
import com.example.tatkala.data.local.dao.HabitDao
import com.example.tatkala.data.local.entity.HabitEntity
import com.example.tatkala.data.local.entity.HabitLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate

object HabitRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var dao: HabitDao
    private var initialized = false

    fun init(context: Context) {
        if (initialized) return
        dao = TatkalaDatabase.getInstance(context.applicationContext).habitDao()
        initialized = true
        scope.launch { seedIfEmpty() }
    }

    fun observeHabits(): Flow<List<HabitEntity>> = dao.observeActiveHabits()

    fun observeCompletedLogs(): Flow<List<HabitLogEntity>> = dao.observeCompletedLogs()

    fun observeCompletedLogsBetween(startDate: String, endDate: String): Flow<List<HabitLogEntity>> =
        dao.observeCompletedLogsBetween(startDate, endDate)

    suspend fun getHabit(id: Long): HabitEntity? = dao.getHabit(id)

    suspend fun upsertHabit(habit: HabitEntity) {
        dao.upsertHabit(habit)
    }

    suspend fun archiveHabit(id: Long) {
        dao.archiveHabit(id)
    }

    suspend fun setHabitCompleted(habitId: Long, date: String, completed: Boolean) {
        val existing = dao.getLog(habitId, date)
        dao.upsertLog(
            HabitLogEntity(
                id = existing?.id ?: System.currentTimeMillis(),
                habitId = habitId,
                date = date,
                completed = completed,
                completedAt = if (completed) System.currentTimeMillis() else null
            )
        )
    }

    suspend fun currentStreakForHabit(habitId: Long, today: LocalDate = LocalDate.now()): Int {
        val dates = dao.completedLogsForHabit(habitId)
            .filter { it.completed }
            .mapNotNull { runCatching { LocalDate.parse(it.date) }.getOrNull() }
            .toSet()
        return StreakCalculator.currentStreak(dates, today)
    }

    suspend fun clear() {
        dao.clearLogs()
        dao.clearHabits()
    }

    private suspend fun seedIfEmpty() {
        if (dao.countHabits() > 0) return

        val now = System.currentTimeMillis()
        val habits = listOf(
            HabitEntity(id = now + 1, name = "Reading", category = "Focus", targetPerWeek = 7, createdAt = now + 1),
            HabitEntity(id = now + 2, name = "Workout", category = "Health", targetPerWeek = 4, createdAt = now + 2),
            HabitEntity(id = now + 3, name = "Deep Study", category = "Study", targetPerWeek = 6, createdAt = now + 3),
            HabitEntity(id = now + 4, name = "Drink Water", category = "Health", targetPerWeek = 7, createdAt = now + 4)
        )
        habits.forEach { dao.upsertHabit(it) }

        val today = LocalDate.now()
        val logs = mutableListOf<HabitLogEntity>()
        for (offset in 0..13) {
            logs.add(
                HabitLogEntity(
                    id = now + 100 + offset,
                    habitId = habits.first().id,
                    date = today.minusDays(offset.toLong()).toString(),
                    completed = true,
                    completedAt = now - offset * 86_400_000L
                )
            )
        }
        for (offset in listOf(0, 1, 2, 4, 6)) {
            logs.add(HabitLogEntity(now + 200 + offset, habits[1].id, today.minusDays(offset.toLong()).toString(), true, now))
        }
        for (offset in listOf(0, 2, 3, 5)) {
            logs.add(HabitLogEntity(now + 300 + offset, habits[2].id, today.minusDays(offset.toLong()).toString(), true, now))
        }
        for (offset in 0..6) {
            logs.add(HabitLogEntity(now + 400 + offset, habits[3].id, today.minusDays(offset.toLong()).toString(), offset != 3, now))
        }
        dao.insertLogs(logs)
    }
}

object StreakCalculator {
    fun currentStreak(completedDates: Set<LocalDate>, today: LocalDate = LocalDate.now()): Int {
        if (completedDates.isEmpty()) return 0
        val start = when {
            today in completedDates -> today
            today.minusDays(1) in completedDates -> today.minusDays(1)
            else -> return 0
        }
        var streak = 0
        var cursor = start
        while (cursor in completedDates) {
            streak += 1
            cursor = cursor.minusDays(1)
        }
        return streak
    }
}
