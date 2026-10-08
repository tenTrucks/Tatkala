package com.example.tatkala.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: Long = System.currentTimeMillis(),
    val name: String,
    val category: String,
    val targetPerWeek: Int,
    val reminderTime: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "habit_logs")
data class HabitLogEntity(
    @PrimaryKey val id: Long = System.currentTimeMillis(),
    val habitId: Long,
    val date: String,
    val completed: Boolean,
    val completedAt: Long? = null
)
