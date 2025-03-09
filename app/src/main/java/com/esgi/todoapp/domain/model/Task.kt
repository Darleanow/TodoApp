package com.esgi.todoapp.domain.model
import java.util.Date

/**
 * Domain model representing a Task in the application.
 * This class encapsulates all the data related to a task.
 *
 * @property id Unique identifier for the task, auto-generated if not provided.
 * @property title The title of the task. Cannot be empty.
 * @property description A detailed description of the task.
 * @property creationDate The date and time when the task was created.
 * @property completed Whether the task has been completed or not.
 */
data class Task(
    val id: Int = 0,
    val title: String,
    val description: String,
    val creationDate: Date = Date(),
    val completed: Boolean = false
)