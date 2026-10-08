package com.example.tatkala.model

data class Habit(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val category: String,
    val targetPerWeek: Int,
    val reminderTime: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
