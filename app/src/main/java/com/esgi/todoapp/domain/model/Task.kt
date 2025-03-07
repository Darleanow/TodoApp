package com.esgi.todoapp.domain.model
import java.util.Date

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String,
    val creationDate: Date = Date(),
    val completed: Boolean = false
)