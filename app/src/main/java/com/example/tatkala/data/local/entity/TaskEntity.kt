package com.example.tatkala.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tatkala.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: Long = System.currentTimeMillis(),
    val title: String,
    val date: String,
    val startTime: String,
    val durationMinutes: Int,
    val category: String,
    val description: String = "",
    val isCollaborative: Boolean = false,
    val collaborators: String = "",
    val meetingLink: String = "",
    val location: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toTask(): Task = Task(
        id = id,
        title = title,
        date = date,
        startTime = startTime,
        duration = "$durationMinutes min",
        durationMinutes = durationMinutes,
        category = category,
        isCollaborative = isCollaborative,
        collaborators = collaborators,
        meetingLink = meetingLink,
        location = location,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}
