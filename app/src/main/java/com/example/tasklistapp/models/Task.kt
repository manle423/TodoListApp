package com.example.tasklistapp.models

enum class TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED
}

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val status: TaskStatus,
)
