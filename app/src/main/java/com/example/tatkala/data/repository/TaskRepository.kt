package com.example.tatkala.data.repository

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.example.tatkala.data.local.database.TatkalaDatabase
import com.example.tatkala.data.local.dao.TaskDao
import com.example.tatkala.data.local.entity.TaskEntity
import com.example.tatkala.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

object TaskRepository {

    private const val PREF_NAME = "tatakala_preferences"
    private const val TASK_KEY = "tasks"
    private const val MIGRATED_KEY = "tasks_migrated_to_room"

    val tasks = mutableStateListOf<Task>()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _allTasks = MutableStateFlow<List<TaskEntity>>(emptyList())

    private lateinit var dao: TaskDao
    private var initialized = false

    fun init(context: Context) {
        if (initialized) return

        val appContext = context.applicationContext
        dao = TatkalaDatabase.getInstance(appContext).taskDao()
        initialized = true

        scope.launch {
            migrateLegacyTasksIfNeeded(appContext)
        }

        scope.launch {
            dao.observeTasks().collect { entities ->
                _allTasks.value = entities
                withContext(Dispatchers.Main) {
                    tasks.clear()
                    tasks.addAll(entities.map { it.toTask() })
                }
            }
        }
    }

    fun observeTasks(): Flow<List<TaskEntity>> = dao.observeTasks()

    fun observeTasksByDate(date: String): Flow<List<TaskEntity>> =
        dao.observeTasksByDate(date)

    fun observeCollaborativeTasks(): Flow<List<TaskEntity>> =
        dao.observeCollaborativeTasks()

    fun allTasksState() = _allTasks.asStateFlow()

    suspend fun getTask(id: Long): TaskEntity? = dao.getTask(id)

    suspend fun upsertTask(task: TaskEntity) {
        dao.upsert(task)
    }

    suspend fun addTask(task: Task) {
        dao.upsert(task.toEntity())
    }

    fun addTask(context: Context, task: Task) {
        init(context)
        scope.launch { addTask(task) }
    }

    suspend fun updateTask(task: TaskEntity) {
        dao.update(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        dao.delete(task)
    }

    fun deleteTask(context: Context, task: Task) {
        init(context)
        scope.launch { dao.getTask(task.id)?.let { dao.delete(it) } }
    }

    suspend fun setCompleted(id: Long, completed: Boolean) {
        dao.setCompleted(id, completed)
    }

    suspend fun clear() {
        dao.clear()
    }

    private suspend fun migrateLegacyTasksIfNeeded(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(MIGRATED_KEY, false)) return
        if (dao.getAllOnce().isNotEmpty()) {
            prefs.edit().putBoolean(MIGRATED_KEY, true).apply()
            return
        }

        val stored = prefs.getString(TASK_KEY, null)
        if (stored.isNullOrBlank()) {
            prefs.edit().putBoolean(MIGRATED_KEY, true).apply()
            return
        }

        runCatching {
            val array = JSONArray(stored)
            val imported = mutableListOf<TaskEntity>()
            for (index in 0 until array.length()) {
                val json = array.getJSONObject(index)
                imported.add(
                    TaskEntity(
                        id = json.optLong("id", System.currentTimeMillis() + index),
                        title = json.optString("title", "Untitled task"),
                        date = normalizeLegacyDate(json.optString("date")),
                        startTime = json.optString("startTime", "09:00"),
                        durationMinutes = parseDurationMinutes(json.optString("duration")),
                        category = json.optString("category", "Personal"),
                        isCollaborative = json.optBoolean("isCollaborative", false),
                        collaborators = json.optString("collaborators"),
                        meetingLink = json.optString("meetingLink"),
                        location = json.optString("location"),
                        description = json.optString("description"),
                        isCompleted = json.optBoolean("isCompleted", false)
                    )
                )
            }
            if (imported.isNotEmpty()) {
                dao.insertAll(imported)
            }
        }

        prefs.edit().putBoolean(MIGRATED_KEY, true).apply()
    }

    private fun Task.toEntity(): TaskEntity = TaskEntity(
        id = id,
        title = title,
        date = normalizeLegacyDate(date),
        startTime = startTime.ifBlank { "09:00" },
        durationMinutes = if (durationMinutes > 0) durationMinutes else parseDurationMinutes(duration),
        category = category.ifBlank { "Personal" },
        description = description,
        isCollaborative = isCollaborative,
        collaborators = collaborators,
        meetingLink = meetingLink,
        location = location,
        isCompleted = isCompleted,
        createdAt = createdAt
    )

    private fun parseDurationMinutes(raw: String): Int =
        raw.filter { it.isDigit() }.toIntOrNull()?.coerceAtLeast(1) ?: 60

    private fun normalizeLegacyDate(raw: String): String {
        if (raw.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) return raw
        val parts = raw.split("/", "-", ".").mapNotNull { it.toIntOrNull() }
        return if (parts.size == 3) {
            val day = parts[0].coerceIn(1, 31)
            val month = parts[1].coerceIn(1, 12)
            val year = parts[2]
            "%04d-%02d-%02d".format(year, month, day)
        } else {
            java.time.LocalDate.now().toString()
        }
    }
}
