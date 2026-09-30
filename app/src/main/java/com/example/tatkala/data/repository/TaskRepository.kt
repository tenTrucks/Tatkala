package com.example.tatkala.data.repository

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.example.tatkala.model.Task
import org.json.JSONArray
import org.json.JSONObject

object TaskRepository {

    private const val PREF_NAME = "tatakala_preferences"
    private const val TASK_KEY = "tasks"

    val tasks = mutableStateListOf<Task>()

    private var initialized = false

    fun init(context: Context) {
        if (initialized) return

        loadTasks(context.applicationContext)
        initialized = true
    }

    fun addTask(
        context: Context,
        task: Task
    ) {
        tasks.add(task)
        saveTasks(context.applicationContext)
    }

    fun deleteTask(
        context: Context,
        task: Task
    ) {
        tasks.remove(task)
        saveTasks(context.applicationContext)
    }

    private fun saveTasks(context: Context) {

        val array = JSONArray()

        tasks.forEach { task ->

            val json = JSONObject().apply {
                put("id", task.id)
                put("title", task.title)
                put("date", task.date)
                put("startTime", task.startTime)
                put("duration", task.duration)
                put("category", task.category)
                put("isCollaborative", task.isCollaborative)
                put("collaborators", task.collaborators)
                put("meetingLink", task.meetingLink)
                put("location", task.location)
                put("description", task.description)
            }

            array.put(json)
        }

        context
            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(TASK_KEY, array.toString())
            .apply()
    }

    private fun loadTasks(context: Context) {

        val prefs =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        val stored =
            prefs.getString(TASK_KEY, null)
                ?: return

        try {

            val array = JSONArray(stored)

            tasks.clear()

            for (index in 0 until array.length()) {

                val json = array.getJSONObject(index)

                tasks.add(
                    Task(
                        id = json.getLong("id"),
                        title = json.getString("title"),
                        date = json.getString("date"),
                        startTime = json.getString("startTime"),
                        duration = json.getString("duration"),
                        category = json.getString("category"),
                        isCollaborative =
                            json.getBoolean("isCollaborative"),
                        collaborators =
                            json.optString("collaborators"),
                        meetingLink =
                            json.optString("meetingLink"),
                        location =
                            json.optString("location"),
                        description =
                            json.optString("description")
                    )
                )
            }

        } catch (_: Exception) {
            tasks.clear()
        }
    }
}