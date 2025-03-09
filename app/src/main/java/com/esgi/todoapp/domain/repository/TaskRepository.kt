package com.esgi.todoapp.domain.repository

import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun getTaskById(id: Int): Result<Task?>
    suspend fun insertTask(task: Task): Result<Unit>
    suspend fun updateTask(task: Task): Result<Unit>
    suspend fun deleteTask(task: Task): Result<Unit>
    suspend fun deleteAllTasks(): Result<Unit>
}