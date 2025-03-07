package com.esgi.todoapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.esgi.todoapp.domain.model.Task
import java.util.Date

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val creationDate: Long,
    val completed: Boolean = false
)

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        creationDate = Date(creationDate),
        completed = completed
    )
}

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        creationDate = creationDate.time,
        completed = completed
    )
}