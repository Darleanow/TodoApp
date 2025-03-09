package com.esgi.todoapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.esgi.todoapp.domain.model.Task
import java.util.Date

/**
 * Database entity representing a Task in the Room database.
 * This class maps directly to the 'tasks' table in the database.
 *
 * @property id Unique identifier for the task, auto-generated if not provided.
 * @property title The title of the task.
 * @property description A detailed description of the task.
 * @property creationDate The Unix timestamp (milliseconds) when the task was created.
 * @property completed Indicates whether the task has been completed.
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val creationDate: Long,
    val completed: Boolean = false
)

/**
 * Extension function to convert a TaskEntity to a domain Task model.
 * This enables separation between the database layer and domain layer.
 *
 * @return Domain Task model converted from this TaskEntity
 */
fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        creationDate = Date(creationDate),
        completed = completed
    )
}

/**
 * Extension function to convert a domain Task model to a TaskEntity.
 * This enables separation between the domain layer and database layer.
 *
 * @return TaskEntity converted from this domain Task model
 */
fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        creationDate = creationDate.time,
        completed = completed
    )
}