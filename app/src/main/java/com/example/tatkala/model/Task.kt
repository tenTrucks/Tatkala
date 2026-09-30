package com.example.tatkala.model

data class Task(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val date: String,
    val startTime: String,
    val duration: String,
    val category: String,
    val isCollaborative: Boolean = false,
    val collaborators: String = "",
    val meetingLink: String = "",
    val location: String = "",
    val description: String = ""
)